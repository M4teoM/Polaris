package com.polaris.api;

import com.polaris.dto.ActualizarReservaDTO;
import com.polaris.dto.ClientePerfilDTO;
import com.polaris.dto.ReservaDTO;
import com.polaris.service.IPortalUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST del Portal de Usuario.
 *
 * Este controlador concentra los endpoints necesarios para que un cliente pueda:
 * 1) consultar su perfil,
 * 2) revisar sus reservas activas,
 * 3) revisar su historial,
 * 4) cancelar una reserva,
 * 5) actualizar fechas de una reserva.
 *
 * Todas las respuestas devuelven DTOs planos para evitar exponer entidades
 * con relaciones anidadas en la API.
 */
@RestController
@RequestMapping("/api/portal-usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class PortalUsuarioRestController {

    @Autowired
    private IPortalUsuarioService portalUsuarioService;

    /**
     * Obtiene la información personal del cliente según su ID.
     *
     * @param clienteId identificador único del cliente.
     * @return perfil del cliente en DTO plano.
     */
    // GET http://localhost:8080/api/portal-usuario/clientes/{clienteId}/perfil
    @GetMapping("/clientes/{clienteId}/perfil")
    public ResponseEntity<ClientePerfilDTO> obtenerPerfil(@PathVariable Long clienteId) {
        return ResponseEntity.ok(portalUsuarioService.obtenerPerfilCliente(clienteId));
    }

    /**
     * Lista las reservas activas del cliente.
     *
     * La definición de activa se resuelve en la capa de servicio/repositorio
     * con base en estado y vigencia de fechas.
     *
     * @param clienteId identificador del cliente.
     * @return listado de reservas activas en formato plano.
     */
    // GET http://localhost:8080/api/portal-usuario/clientes/{clienteId}/reservas/activas
    @GetMapping("/clientes/{clienteId}/reservas/activas")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasActivas(@PathVariable Long clienteId) {
        return ResponseEntity.ok(portalUsuarioService.obtenerReservasActivas(clienteId));
    }

    /**
     * Lista el historial de reservas del cliente.
     *
     * Incluye reservas finalizadas por fecha y reservas canceladas.
     *
     * @param clienteId identificador del cliente.
     * @return listado histórico de reservas en DTO plano.
     */
    // GET http://localhost:8080/api/portal-usuario/clientes/{clienteId}/reservas/historial
    @GetMapping("/clientes/{clienteId}/reservas/historial")
    public ResponseEntity<List<ReservaDTO>> obtenerHistorial(@PathVariable Long clienteId) {
        return ResponseEntity.ok(portalUsuarioService.obtenerHistorialReservas(clienteId));
    }

    /**
     * Cancela una reserva del cliente cambiando su estado a CANCELADO.
     *
     * @param clienteId cliente dueño de la reserva.
     * @param reservaId reserva que se desea cancelar.
     * @return reserva actualizada en DTO plano.
     */
    // PUT http://localhost:8080/api/portal-usuario/clientes/{clienteId}/reservas/{reservaId}/cancelar
    @PutMapping("/clientes/{clienteId}/reservas/{reservaId}/cancelar")
    public ResponseEntity<ReservaDTO> cancelarReserva(@PathVariable Long clienteId,
                                                      @PathVariable Long reservaId) {
        return ResponseEntity.ok(portalUsuarioService.cancelarReserva(clienteId, reservaId));
    }

    /**
     * Actualiza las fechas de una reserva del cliente.
     *
     * También permite actualizar el número de huéspedes de forma opcional.
     * La validación de reglas de negocio se realiza en la capa de servicio.
     *
     * @param clienteId cliente dueño de la reserva.
     * @param reservaId reserva a modificar.
     * @param request DTO con nuevas fechas y datos opcionales.
     * @return reserva actualizada en formato plano.
     */
    // PUT http://localhost:8080/api/portal-usuario/clientes/{clienteId}/reservas/{reservaId}
    @PutMapping("/clientes/{clienteId}/reservas/{reservaId}")
    public ResponseEntity<ReservaDTO> actualizarReserva(@PathVariable Long clienteId,
                                                        @PathVariable Long reservaId,
                                                        @RequestBody ActualizarReservaDTO request) {
        return ResponseEntity.ok(portalUsuarioService.actualizarReserva(clienteId, reservaId, request));
    }
}
