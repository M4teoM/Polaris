package com.polaris.service;

import com.polaris.dto.ActualizarReservaDTO;
import com.polaris.dto.ClientePerfilDTO;
import com.polaris.dto.ReservaDTO;

import java.util.List;

public interface IPortalUsuarioService {

    /**
     * Devuelve el perfil del cliente en formato plano para el portal de usuario.
     */
    ClientePerfilDTO obtenerPerfilCliente(Long clienteId);

    /**
     * Lista las reservas activas de un cliente, calculadas por fecha/estado.
     */
    List<ReservaDTO> obtenerReservasActivas(Long clienteId);

    /**
     * Lista el historial de reservas (finalizadas o canceladas) de un cliente.
     */
    List<ReservaDTO> obtenerHistorialReservas(Long clienteId);

    /**
     * Cancela una reserva del cliente cambiando su estado a CANCELADO.
     */
    ReservaDTO cancelarReserva(Long clienteId, Long reservaId);

    /**
     * Actualiza las fechas (y opcionalmente el número de huéspedes) de una reserva.
     */
    ReservaDTO actualizarReserva(Long clienteId, Long reservaId, ActualizarReservaDTO request);
}
