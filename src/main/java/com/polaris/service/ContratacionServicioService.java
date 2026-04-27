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

import java.time.LocalDate;
import java.util.List;

@Service
public class ContratacionServicioService {

    private final IReservaHabitacionRepository reservaRepository;
    private final IServicioRepository servicioRepository;
    private final ICuentaRepository cuentaRepository;
    private final IItemCuentaRepository itemCuentaRepository;

    public ContratacionServicioService(IReservaHabitacionRepository reservaRepository,
                                       IServicioRepository servicioRepository,
                                       ICuentaRepository cuentaRepository,
                                       IItemCuentaRepository itemCuentaRepository) {
        this.reservaRepository = reservaRepository;
        this.servicioRepository = servicioRepository;
        this.cuentaRepository = cuentaRepository;
        this.itemCuentaRepository = itemCuentaRepository;
    }

    @Transactional(readOnly = true)
    public ContratacionInfo obtenerInfoPorNumeroHabitacion(String numeroHabitacion) {
        String numero = numeroHabitacion == null ? "" : numeroHabitacion.trim();
        if (numero.isEmpty()) {
            throw new IllegalArgumentException("Debes ingresar el número de habitación.");
        }

        ReservaHabitacion reserva = reservaRepository
                .findReservasVigentesPorNumeroHabitacion(numero, LocalDate.now())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay una reserva vigente para esa habitación."));

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

    @Transactional
    public ContratacionResultado contratarServicio(String numeroHabitacion, Long servicioId) {
        ContratacionInfo info = obtenerInfoPorNumeroHabitacion(numeroHabitacion);

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado."));

        ReservaHabitacion reserva = reservaRepository.findById(info.reservaId())
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada."));

        Cuenta cuenta = cuentaRepository.findByReservaId(reserva.getId())
                .orElseGet(() -> cuentaRepository.save(Cuenta.builder()
                        .reserva(reserva)
                        .cliente(reserva.getCliente())
                        .pagada(false)
                        .build()));

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

    private double calcularTotal(Long cuentaId) {
        List<ItemCuenta> items = itemCuentaRepository.findByCuentaId(cuentaId);
        return items.stream()
                .map(ItemCuenta::getServicio)
                .mapToDouble(s -> s.getPrecio() == null ? 0d : s.getPrecio())
                .sum();
    }

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
