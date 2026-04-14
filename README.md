# 🌟 Hotel Polaris

<p align="center">
  <img src="https://github.com/user-attachments/assets/51efa03b-3ba3-45e6-b3fe-e8d424030e41" width="350" alt="Hotel Polaris Logo">
</p>
> **Sistema web integral para la gestión hotelera.**
> Optimiza la administración de habitaciones, clientes y servicios desde una interfaz intuitiva y moderna.

---

## 📖 Descripción

<img width="960" height="234" alt="Captura de pantalla 2026-03-12 a la(s) 10 28 48 a m" src="https://github.com/user-attachments/assets/2726c765-5ae2-4f38-98d4-58826acc0f0c" />
**Hotel Polaris** es una plataforma desarrollada como proyecto académico para facilitar la gestión operativa de un establecimiento hotelero. El sistema separa de forma lógica las categorías de alojamiento de las unidades físicas, permitiendo un control detallado sobre la disponibilidad y el inventario del hotel.

### ✨ Características Principales

#### 🛌 Gestión de Habitaciones

El sistema implementa un modelo de jerarquía para el control de inventario:

- **Tipos de Habitación:** Configuración de categorías (Estándar, Suite, Penthouse) definiendo precio, capacidad, tipo de cama y dimensiones.
- **Unidades Físicas:** Gestión de habitaciones reales asignadas a un número y piso, con estados en tiempo real (_Disponible, Ocupada, Mantenimiento_).

#### 👥 Módulo de Clientes

- Registro y autenticación de usuarios.
- Gestión de perfiles personales para consulta de información.

#### 🛠️ Servicios Adicionales

- Catálogo visual de servicios ofrecidos por el hotel accesible desde la página principal.

---

## 🛠️ Stack Tecnológico

El proyecto utiliza tecnologías robustas para garantizar un desarrollo ágil y escalable:

- **Framework:** [Spring Boot](https://spring.io/projects/spring-boot)
- **Motor de Plantillas:** [Thymeleaf](https://www.thymeleaf.org/)
- **Persistencia:** H2 Database / En memoria
- **Lenguaje:** Java 17

---

## 🚀 Instalación y Uso

Sigue estos pasos para desplegar el proyecto localmente:

### Requisitos previos

- Java 17 o superior.
- Maven 3.6+.

### Pasos de ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/M4teoM/Polaris.git](https://github.com/M4teoM/Polaris.git)
   cd Polaris
   ```

````
2. **Compilar y ejecutar:**
```bash
./mvnw spring-boot:run
````

## 🗃️ Migración de Base de Datos (Unificación a Servicio)

Para completar la unificación del dominio y eliminar la entidad **Tratamiento**, se incluye este script:

- sql/migrations/2026-04-06_drop_tratamiento.sql

### Cómo ejecutarlo en H2

1. Inicia la aplicación.
2. Abre la consola H2 en /h2.
3. Conéctate a la base con la URL configurada en application.properties.
4. Ejecuta el contenido del script para eliminar la tabla tratamiento si existe.
