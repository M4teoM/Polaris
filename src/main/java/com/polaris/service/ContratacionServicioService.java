package com.polaris.service;

import com.polaris.model.Cliente;
import com.polaris.model.Cuenta;
import com.polaris.model.ItemCuenta;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.Servicio;
import com.polaris.repository.ICuentaRepository;
import com.polaris.repository.IItemCuentaRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import com.polaris.repository.IServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.polaris.errors.ErrorReservaException;

import java.time.LocalDate;
import java.util.List;


/**
 * Servicio de negocio para la contratación de servicios del hotel por parte del operador.
 * Gestiona la búsqueda de clientes por habitación, la creación de cuentas y
 * la adición de ítems a la cuenta de una estadía activa.
 */
@Service
public class ContratacionServicioService {

    private final IReservaHabitacionRepository reservaRepository;
    private final IServicioRepository servicioRepository;
    private final ICuentaRepository cuentaRepository;
    private final IItemCuentaRepository itemCuentaRepository;


    /** Inyección por constructor para facilitar pruebas y claridad de dependencias. */
    public ContratacionServicioService(IReservaHabitacionRepository reservaRepository,
                                       IServicioRepository servicioRepository,
                                       ICuentaRepository cuentaRepository,
                                       IItemCuentaRepository itemCuentaRepository) {
        this.reservaRepository = reservaRepository;
        this.servicioRepository = servicioRepository;
        this.cuentaRepository = cuentaRepository;
        this.itemCuentaRepository = itemCuentaRepository;
    }


    /**
     * Busca la información del cliente con una reserva activa en la habitación indicada.
     * El operador ingresa el número de habitación y este método retorna los datos
     * del cliente y el total actual de su cuenta, para confirmar antes de contratar.
     * Lanza IllegalArgumentException si no hay reserva activa en esa habitación.
     */
    @Transactional(readOnly = true)
    public ContratacionInfo obtenerInfoPorNumeroHabitacion(String numeroHabitacion) {
        String numero = numeroHabitacion == null ? "" : numeroHabitacion.trim();
        if (numero.isEmpty())
            throw new IllegalArgumentException("Debes ingresar el número de habitación.");

        ReservaHabitacion reserva = reservaRepository
                .findReservasActivasPorNumeroHabitacion(numero)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay una reserva activa para esa habitación."));

        Cliente cliente = reserva.getCliente();
        Cuenta cuenta = cuentaRepository.findByReservaId(reserva.getId())
                .orElse(null);

        Double totalCuenta = cuenta == null ? 0d : calcularTotal(cuenta.getId());
        Long cuentaId = cuenta == null ? null : cuenta.getId();

        return new ContratacionInfo(
                reserva.getId(),
                reserva.getHabitacion().getId(),
                reserva.getHabitacion().getNumero(),
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getCedula(),
                cliente.getTelefono(),
                cuentaId,
                totalCuenta
        );
    }


    /**
     * Contrata un servicio para la estadía activa de una habitación.
     * Si la cuenta no existe, la crea automáticamente.
     * Agrega el servicio como ítem a la cuenta y retorna el total actualizado.
     */
    @Transactional
    public ContratacionResultado contratarServicio(String numeroHabitacion, Long servicioId) {
        ContratacionInfo info = obtenerInfoPorNumeroHabitacion(numeroHabitacion);

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado."));

        ReservaHabitacion reserva = reservaRepository.findById(info.reservaId())
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada."));


        // Crear la cuenta si aún no existe para esta reserva
        Cuenta cuenta = cuentaRepository.findByReservaId(reserva.getId())
                .orElseGet(() -> cuentaRepository.save(Cuenta.builder()
                        .reserva(reserva)
                        .cliente(reserva.getCliente())
                        .pagada(false)
                        .build()));

        // Agregar el servicio como ítem a la cuenta
        ItemCuenta item = ItemCuenta.builder()
                .cuenta(cuenta)
                .servicio(servicio)
                .fechaConsumo(LocalDate.now())
                .build();
        itemCuentaRepository.save(item);

        cuenta.setPagada(false);
        cuentaRepository.save(cuenta);

        double totalActualizado = calcularTotal(cuenta.getId());
        return new ContratacionResultado(
                cuenta.getId(),
                servicio.getId(),
                servicio.getNombre(),
                servicio.getPrecio(),
                totalActualizado,
                "Servicio contratado y agregado a la cuenta."
        );
    }

    /**
     * Marca una cuenta como pagada.
     * Lanza error si la cuenta ya fue pagada anteriormente.
     */
    @Transactional
    public void pagarCuenta(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada."));
        if (Boolean.TRUE.equals(cuenta.getPagada()) &&
                itemCuentaRepository.findByCuentaId(cuentaId).stream().allMatch(i -> Boolean.TRUE.equals(i.getPagado())))
            throw new IllegalArgumentException("Esta cuenta ya está completamente pagada.");

        // Marcar todos los ítems como pagados
        List<ItemCuenta> items = itemCuentaRepository.findByCuentaId(cuentaId);
        items.forEach(item -> item.setPagado(true));
        itemCuentaRepository.saveAll(items);

        // Marcar la cuenta como pagada
        cuenta.setPagada(true);
        cuentaRepository.save(cuenta);
    }

    /**
     * Marca un ítem individual de la cuenta como pagado.
     * Lanza error si el ítem ya fue pagado.
     */
    @Transactional
    public void pagarItem(Long itemId) {
        ItemCuenta item = itemCuentaRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado."));
        if (item.getPagado())
            throw new IllegalArgumentException("Este servicio ya fue pagado.");
        item.setPagado(true);
        itemCuentaRepository.save(item);
    }


    /**
     * Elimina un ítem de una cuenta específica.
     * No permite modificar una cuenta que ya fue pagada.
     * Verifica que el ítem pertenezca a la cuenta indicada.
     */
    @Transactional
    public void eliminarItemCuenta(Long cuentaId, Long itemId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada."));
        if (cuenta.getPagada())
            throw new IllegalArgumentException("No se puede modificar una cuenta ya pagada.");
        ItemCuenta item = itemCuentaRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado."));
        if (!item.getCuenta().getId().equals(cuentaId))
            throw new IllegalArgumentException("El item no pertenece a esta cuenta.");
        itemCuentaRepository.delete(item);
    }


    /**
     * Calcula el total de una cuenta sumando el precio de todos sus ítems.
     * Ítems con precio nulo se cuentan como 0.
     */
    private double calcularTotal(Long cuentaId) {
        List<ItemCuenta> items = itemCuentaRepository.findByCuentaId(cuentaId);
        return items.stream()
                .map(ItemCuenta::getServicio)
                .mapToDouble(s -> s.getPrecio() == null ? 0d : s.getPrecio())
                .sum();
    }



    /** DTO de respuesta con la información del cliente y su cuenta para el operador. */
    public record ContratacionInfo(
            Long reservaId,
            Long habitacionId,
            String habitacionNumero,
            Long clienteId,
            String clienteNombre,
            String clienteApellido,
            String clienteCorreo,
            String clienteCedula,
            String clienteTelefono,
            Long cuentaId,
            Double totalCuenta
    ) {
    }


    /** DTO de respuesta con el resultado de contratar un servicio y el total actualizado. */
    public record ContratacionResultado(
            Long cuentaId,
            Long servicioId,
            String servicioNombre,
            Double servicioPrecio,
            Double totalCuenta,
            String mensaje
    ) {
    }
}
