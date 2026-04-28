/**
 * Interfaz que representa el perfil del cliente desde el Portal de Usuario.
 *
 * Contiene solo los campos necesarios para mostrar la información personal
 * del cliente en la pantalla "Mi Perfil". No incluye listas de reservas
 * para mantener la estructura plana sin anidamientos.
 */
export interface ClientePerfilDTO {
  id: number;
  nombre: string;
  apellido: string;
  correo: string;
  cedula?: string;
  telefono?: string;
}
