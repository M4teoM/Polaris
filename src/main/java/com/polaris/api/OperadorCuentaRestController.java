package com.polaris.api;

import com.polaris.model.Cuenta;
import com.polaris.model.ItemCuenta;
import com.polaris.repository.ICuentaRepository;
import com.polaris.service.ContratacionServicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/operador/cuentas")
@CrossOrigin(origins = "http://localhost:4200")
public class OperadorCuentaRestController {

    private final ICuentaRepository cuentaRepository;
    private final ContratacionServicioService contratacionService;

    public OperadorCuentaRestController(ICuentaRepository cuentaRepository,
                                        ContratacionServicioService contratacionService) {
        this.cuentaRepository = cuentaRepository;
        this.contratacionService = contratacionService;
    }

    // ── DTOs planos: evitan los ciclos de serialización Jackson ──────────────

    public record ItemDto(
            Long id,
            Long cuentaId,
            Long servicioId,
            String servicioNombre,
            Double servicioPrecio,
            String fechaConsumo,
            Boolean pagado
    ) {}

    public record CuentaDto(
            Long id,
            Long reservaId,
            Long clienteId,
            Boolean pagada,
            String reservaHabitacionNumero,
            String clienteNombre,
            String clienteApellido,
            List<ItemDto> items
    ) {}

    // ── Conversores ──────────────────────────────────────────────────────────

    private ItemDto toItemDto(ItemCuenta item) {
        return new ItemDto(
                item.getId(),
                item.getCuenta() != null ? item.getCuenta().getId() : null,
                item.getServicio() != null ? item.getServicio().getId() : null,
                item.getServicio() != null ? item.getServicio().getNombre() : null,
                item.getServicio() != null ? item.getServicio().getPrecio() : null,
                item.getFechaConsumo() != null ? item.getFechaConsumo().toString() : null,
                item.getPagado()
        );
    }

    private CuentaDto toCuentaDto(Cuenta cuenta) {
        List<ItemDto> items = cuenta.getItems() == null
                ? List.of()
                : cuenta.getItems().stream().map(this::toItemDto).collect(Collectors.toList());

        String habitacionNumero = null;
        String clienteNombre   = null;
        String clienteApellido = null;

        if (cuenta.getReserva() != null && cuenta.getReserva().getHabitacion() != null) {
            habitacionNumero = cuenta.getReserva().getHabitacion().getNumero();
        }
        if (cuenta.getCliente() != null) {
            clienteNombre   = cuenta.getCliente().getNombre();
            clienteApellido = cuenta.getCliente().getApellido();
        }

        // Pagada = true solo si TODOS los ítems están pagados individualmente
        boolean pagada = items.isEmpty()
                ? Boolean.TRUE.equals(cuenta.getPagada())
                : items.stream().allMatch(i -> Boolean.TRUE.equals(i.pagado()));

        return new CuentaDto(
                cuenta.getId(),
                cuenta.getReserva() != null ? cuenta.getReserva().getId() : null,
                cuenta.getCliente() != null ? cuenta.getCliente().getId() : null,
                pagada,
                habitacionNumero,
                clienteNombre,
                clienteApellido,
                items
        );
    }

    // ── Endpoints ────────────────────────────────────────────────────────────

    // GET http://localhost:8080/api/operador/cuentas
    @GetMapping
    public List<CuentaDto> listar() {
        return cuentaRepository.findAllConDetalle()
                .stream()
                .map(this::toCuentaDto)
                .collect(Collectors.toList());
    }

    // PUT http://localhost:8080/api/operador/cuentas/item/{itemId}/pagar
    @PutMapping("/item/{itemId}/pagar")
    public ResponseEntity<?> pagarItem(@PathVariable Long itemId) {
        try {
            contratacionService.pagarItem(itemId);
            return ResponseEntity.ok(Map.of("mensaje", "Servicio pagado correctamente."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE http://localhost:8080/api/operador/cuentas/{cuentaId}/item/{itemId}
    @DeleteMapping("/{cuentaId}/item/{itemId}")
    public ResponseEntity<?> eliminarItem(@PathVariable Long cuentaId,
                                          @PathVariable Long itemId) {
        try {
            contratacionService.eliminarItemCuenta(cuentaId, itemId);
            return ResponseEntity.ok(Map.of("mensaje", "Servicio eliminado de la cuenta."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}