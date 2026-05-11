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
     * Caso 1.a — Registro con correo inválido (formato sin @).
     * La validación HTML5 del input type="email" bloquea el submit, así que
     * el form NO navega y seguimos en /clientes/nuevo.
     */
    @Test
    public void Caso1a_registroCorreoInvalido_permaneceEnFormulario() {
        driver.get(BASE_URL + "/clientes/nuevo");

        driver.findElement(By.id("nombre")).sendKeys("Test");
        driver.findElement(By.id("apellido")).sendKeys("E2E");
        driver.findElement(By.id("correo")).sendKeys("esto-no-es-un-correo");
        driver.findElement(By.id("contrasena")).sendKeys(CONTRASENA);
        driver.findElement(By.cssSelector(".btn-guardar")).click();

        // El navegador bloquea el submit: la URL no cambia.
        Assertions.assertThat(driver.getCurrentUrl())
                .as("La validación HTML5 debe impedir el envío")
                .endsWith("/clientes/nuevo");
    }

    /**
     * Caso 1.b — Registro con correo ya existente (validación de servidor).
     * El service lanza IllegalArgumentException("Este correo ya está registrado.")
     * y el controller re-renderiza la vista con el mensaje en el span #errorCorreo.
     */
    @Test
    public void Caso1b_registroCorreoDuplicado_muestraErrorEnServidor() {
        // samutovar10@gmail.com es un cliente seed del DataInitializer.
        driver.get(BASE_URL + "/clientes/nuevo");

        driver.findElement(By.id("nombre")).sendKeys("Test");
        driver.findElement(By.id("apellido")).sendKeys("E2E");
        driver.findElement(By.id("correo")).sendKeys("samutovar10@gmail.com");
        driver.findElement(By.id("contrasena")).sendKeys(CONTRASENA);
        driver.findElement(By.cssSelector(".btn-guardar")).click();

        // El servidor re-renderiza el form con el mensaje de error visible.
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#correo + span"), "ya está registrado"));

        WebElement msg = driver.findElement(By.cssSelector("#correo + span"));
        Assertions.assertThat(msg.getText())
                .as("Debe mostrarse el mensaje de correo duplicado del servidor")
                .contains("ya está registrado");
    }

    /**
     * Caso 1.c — Flujo completo:
     *   1. Registro exitoso → redirige al perfil del cliente recién creado.
     *   2. Primera reserva (próxima semana) → habitación H1 asignada.
     *   3. Segunda reserva con fechas solapadas → debe poder realizarse,
     *      con habitación H2 distinta de H1.
     */
    @Test
    public void Caso1c_registroYDosReservasSolapadas_habitacionesDistintas() {
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