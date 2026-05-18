package com.polaris.e2e;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

/**
 * Caso 1 (Hotel — guía del profesor):
 *
 *  "Un nuevo usuario llega al landing page. Como aún no tiene cuenta se registra.
 *   La primera vez el registro sale mal porque no pone un correo válido.
 *   Se verifica el mensaje de error. Tras corregir, se registra correctamente,
 *   va a la página de reservas y genera una primera reserva para la siguiente semana.
 *   Se genera la reserva y se asigna una habitación. El usuario intenta luego una
 *   segunda reserva con fechas que se solapan con la primera. Debe poder realizarla,
 *   pero el número de habitación asignado debe ser diferente al primero."
 *
 * Adaptaciones a Polaris:
 *  - Polaris usa Thymeleaf en :8080 (no Angular en :4200).
 *  - El formulario de registro está en /clientes/nuevo (no /registro).
 *  - El formulario solo tiene nombre/apellido/correo/contrasena (sin confirmación).
 *  - La validación de correo vacío/mal formado la hace el navegador (HTML5 type="email"),
 *    y la validación de correo duplicado la hace el servidor (mensaje #errorCorreo).
 *  - El form /reservas/nueva requiere ?tipoHabitacionId=X&clienteId=Y (entra desde detalle
 *    de habitación). Para el test podemos saltarnos el flujo de UI hasta llegar al form.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Caso1RegistroYReservaTest extends BaseE2ETest {

    /** Correo único por ejecución para no chocar con el DataInitializer. */
    private final String CORREO_NUEVO = "e2e." + System.currentTimeMillis() + "@polaris.com";
    private static final String CONTRASENA = "Test1234";

        /**
         * Caso 1 — Flujo completo en un único test:
         *   1. Intento de registro con correo inválido: el navegador bloquea el submit.
         *   2. Registro exitoso de cliente nuevo.
         *   3. Primera reserva para la próxima semana.
         *   4. Segunda reserva con solape de fechas y habitación distinta.
         */
        @Test
        public void Caso1_unico_flujoCompleto_registroYDosReservasSolapadas() {
                // ── 0. Intento inválido de registro (correo mal formado) ───────────
                driver.get(BASE_URL + "/clientes/nuevo");
                driver.findElement(By.id("nombre")).sendKeys("Test");
                driver.findElement(By.id("apellido")).sendKeys("E2E");
                driver.findElement(By.id("correo")).sendKeys("esto-no-es-un-correo");
                driver.findElement(By.id("contrasena")).sendKeys(CONTRASENA);
                driver.findElement(By.cssSelector(".btn-guardar")).click();

                Assertions.assertThat(driver.getCurrentUrl())
                                .as("La validación HTML5 debe impedir el envío del formulario")
                                .endsWith("/clientes/nuevo");

        // ── 1. Registro ────────────────────────────────────────────────────
        driver.get(BASE_URL + "/clientes/nuevo");
        driver.findElement(By.id("nombre")).sendKeys("Usuario");
        driver.findElement(By.id("apellido")).sendKeys("E2E");
        driver.findElement(By.id("correo")).sendKeys(CORREO_NUEVO);
        driver.findElement(By.id("contrasena")).sendKeys(CONTRASENA);
        driver.findElement(By.cssSelector(".btn-guardar")).click();

        // ClienteController redirige a /clientes/ver/{id} tras crear el cliente.
        wait.until(ExpectedConditions.urlContains("/clientes/ver/"));
        String urlPerfil = driver.getCurrentUrl();
        Long clienteId = Long.parseLong(urlPerfil.substring(urlPerfil.lastIndexOf('/') + 1));

        Assertions.assertThat(clienteId)
                .as("El cliente recién creado debe tener un id asignado")
                .isPositive();

        // ── 2. Primera reserva (saltamos el flujo home → habitaciones → detalle
        //       e ingresamos directo al form, que es accesible solo si conocemos
        //       tipoHabitacionId y clienteId) ────────────────────────────────
        Long tipoHabitacionId = 1L; // Habitación Estándar (primer tipo del DataInitializer)

        LocalDate checkIn1  = LocalDate.now().plusDays(7);
        LocalDate checkOut1 = LocalDate.now().plusDays(10);

        crearReserva(tipoHabitacionId, clienteId, checkIn1, checkOut1, "1");

        // Tras crear la reserva el ReservaController redirige a /clientes/ver/{clienteId}.
        wait.until(ExpectedConditions.urlContains("/clientes/ver/" + clienteId));

        // ── 3. Capturar habitación asignada en la primera reserva ──────────
        List<WebElement> filas = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(".perfil-card table tbody tr")));

        Assertions.assertThat(filas)
                .as("Después de crear la primera reserva, debe haber al menos 1 fila")
                .hasSizeGreaterThanOrEqualTo(1);

        String habitacion1 = filas.get(0).findElements(By.tagName("td")).get(0).getText();

        // ── 4. Segunda reserva con fechas solapadas ────────────────────────
        LocalDate checkIn2  = LocalDate.now().plusDays(8);  // se solapa con la 1ª
        LocalDate checkOut2 = LocalDate.now().plusDays(12);

        crearReserva(tipoHabitacionId, clienteId, checkIn2, checkOut2, "1");
        wait.until(ExpectedConditions.urlContains("/clientes/ver/" + clienteId));

        // ── 5. Verificar que la 2ª reserva está creada con habitación distinta ─
        List<WebElement> filasFinales = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(".perfil-card table tbody tr")));

        Assertions.assertThat(filasFinales)
                .as("El perfil debe mostrar las 2 reservas")
                .hasSize(2);

        String habitacion2 = filasFinales.get(1).findElements(By.tagName("td")).get(0).getText();

        Assertions.assertThat(habitacion2)
                .as("La segunda reserva debe asignar una habitación diferente a la primera")
                .isNotEqualTo(habitacion1);
    }

    /**
     * Crea una reserva ingresando directamente al formulario.
     * /reservas/nueva acepta tipoHabitacionId y clienteId como query params.
     */
    private void crearReserva(Long tipoHabitacionId, Long clienteId,
                              LocalDate checkIn, LocalDate checkOut, String huespedes) {

        driver.get(BASE_URL + "/reservas/nueva?tipoHabitacionId="
                + tipoHabitacionId + "&clienteId=" + clienteId);

        WebElement inputCheckIn  = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("fechaCheckIn")));
        WebElement inputCheckOut = driver.findElement(By.id("fechaCheckOut"));

        setFecha(inputCheckIn,  checkIn);
        setFecha(inputCheckOut, checkOut);

        new Select(driver.findElement(By.id("numeroHuespedes")))
                .selectByValue(huespedes);

        driver.findElement(By.cssSelector("button.btn-guardar")).click();
    }
}