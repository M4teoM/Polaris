/**
 * Interfaz que representa una reserva desde la perspectiva del Portal de Usuario.
 *
 * Estructura plana que incluye datos de cliente, habitación y tipo de habitación
 * como campos simples (strings/números) para evitar objetos anidados.
 */
export interface ReservaDTO {
  id: number;
  fechaCheckIn: string; // formato ISO: YYYY-MM-DD
  fechaCheckOut: string; // formato ISO: YYYY-MM-DD
  estado: string; // 'Activa', 'Inactiva', 'CANCELADO', etc.
  numeroHuespedes: number;
  clienteId: number;
  clienteNombreCompleto: string;
  habitacionId: number;
  habitacionNumero: string;
  tipoHabitacionId: number;
  tipoHabitacionNombre: string;
}

/**
 * Interfaz para la solicitud de actualización de una reserva.
 * Permite cambiar fechas y opcionalmente el número de huéspedes.
 */
export interface ActualizarReservaDTO {
  fechaCheckIn: string;
  fechaCheckOut: string;
  numeroHuespedes?: number;
}
