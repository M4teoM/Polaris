Polaris 🌟
Hotel Polaris es un sistema web para la gestión de un hotel. Permite administrar los tipos de habitaciones disponibles, las habitaciones físicas del hotel, los clientes registrados y los servicios que ofrece el establecimiento.
El proyecto fue desarrollado como parte de un ejercicio académico usando Spring Boot y Thymeleaf, con persistencia en memoria.

¿Qué puede hacer el sistema?
Habitaciones
El hotel maneja dos conceptos separados:

Tipos de habitación: definen las características generales de una categoría (nombre, descripción, precio por noche, área, capacidad y tipo de cama). Por ejemplo: Habitación Estándar, Suite Ejecutiva, Penthouse.
Habitaciones físicas: son las habitaciones reales del hotel, con número, piso y estado (Disponible, Ocupada o en Mantenimiento). Cada habitación física pertenece a un tipo.

Desde el panel de administración se pueden crear, editar y eliminar tanto los tipos como las habitaciones físicas.
Clientes
El sistema permite registrar clientes con su nombre, apellido y correo. Los clientes pueden iniciar sesión y ver su perfil.
Servicios
El hotel ofrece servicios adicionales que se pueden consultar desde la página principal.

¿Cómo correr el proyecto?
Se necesita tener instalado Java 17 y Maven.
bashgit clone https://github.com/M4teoM/Polaris.git
cd Polaris
./mvnw spring-boot:run
Luego abrir el navegador en http://localhost:8080.

Autores
Desarrollado por estudiantes de ingeniería de sistemas como proyecto académico.

M4teoM
Santiagom27
Dasuniel
Samt81
