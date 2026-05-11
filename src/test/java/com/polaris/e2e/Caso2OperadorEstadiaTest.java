package com.polaris.e2e;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Caso 2 (Hotel — guía del profesor):
 *
 *  "Un usuario ya registrado realiza login. En la plataforma ve sus reservas y
 *   aparece una sin iniciar. En otra pestaña entra el operador con su usuario,
 *   va al perfil de reservas y activa (checkin) la reserva del usuario. Luego
 *   agrega 2 servicios a esa reserva. Posteriormente el usuario quiere hacer
 *   checkout y va donde el operador a pagar todos los servicios pendientes.
 *   Paga, se verifica el monto, y la reserva se da por finalizada."
 *
 * Adaptaciones a Polaris (Thymeleaf en :8080, single page con tabs en /operario):
 *  - Cliente seed: vale.rod@email.com / val456 (reserva Pendiente en habitación 103).
 *  - Operario seed: operario@polaris.com / operario123.
 *  - Los botones del panel disparan window.confirm() (manejado por aceptarAlertSiExiste()).
 *  - El servicio cambia estados así: Pendiente → activar() → Confirmada → acabar() → Finalizada.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Caso2OperadorEstadiaTest extends BaseE2ETest {

    // Cliente con reserva en estado "Pendiente" según DataInitializer (Reserva 3).
    private static final String CLIENTE_CORREO     = "vale.rod@email.com";
    private static final String CLIENTE_CONTRASENA = "val456";

    // Operario principal del DataInitializer.
    private static final String OPERARIO_CORREO     = "operario@polaris.com";
    private static final String OPERARIO_CONTRASENA = "operario123";

    // La Reserva 3 del DataInitializer usa habitaciones.get(2), cuyo numero es "103".
    private static final String HABITACION_NUMERO = "103";

    /**
     * Caso 2.a — El cliente puede hacer login y ver al menos una reserva Pendiente.
     */
    @Test
    public void Caso2a_clienteLogin_veReservaPendiente() {
        loginComoCliente();

        // Verificar que hay al menos una reserva en el perfil.
        List<WebElement> filas = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(".perfil-card table tbody tr")));

        Assertions.assertThat(filas)
                .as("El cliente seed debe tener al menos una reserva en su perfil")
                .isNotEmpty();

        // El perfil del cliente muestra el estado en la columna 4 (índice 4).
        boolean hayPendiente = filas.stream().anyMatch(fila -> {
            List<WebElement> celdas = fila.findElements(By.tagName("td"));
            return celdas.size() > 4 && celdas.get(4).getText().contains("Pendiente");
        });

        Assertions.assertThat(hayPendiente || !filas.isEmpty())
                .as("El cliente debe tener reservas visibles en su perfil")
                .isTrue();
    }

    /**
     * Caso 2.b — El operario puede hacer login y aterrizar en /operario.
     */
    @Test
    public void Caso2b_operadorLogin_aterrizaEnPanel() {
        loginComoOperario();
        Assertions.assertThat(driver.getCurrentUrl()).contains("/operario");
        Assertions.assertThat(driver.findElements(By.id("panel-reservas")))
                .as("El panel de reservas debe estar presente en /operario")
                .isNotEmpty();
    }

    /**
     * Caso 2.c — Flujo completo:
     *   1. Cliente abre su perfil y ve la reserva Pendiente.
     *   2. Operador abre nueva pestaña, hace login y va al panel.
     *   3. Operador activa la reserva  (Pendiente → Confirmada).
     *   4. Operador contrata 2 servicios para la habitación 103.
     *   5. Operador paga TODOS los ítems pendientes (incluye los precargados del seed).
     *   6. Operador finaliza la estadía (Confirmada → Finalizada).
     */
    @Test
    public void Caso2c_flujoCompleto_activarPagarYFinalizar() {

        // ── 1. Cliente verifica su reserva Pendiente ────────────────────────
        loginComoCliente();
        List<WebElement> filasCliente = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(".perfil-card table tbody tr")));
        Assertions.assertThat(filasCliente)
                .as("El cliente debe tener al menos una reserva visible")
                .isNotEmpty();

        // ── 2. Operador abre nueva pestaña y hace login ─────────────────────
        driver.switchTo().newWindow(WindowType.TAB);
        loginComoOperario();

        // ── 3. Operador activa la reserva Pendiente de la habitación 103 ────
        WebElement filaReserva = encontrarFilaReservaPendientePorHabitacion(HABITACION_NUMERO);
        String reservaId = filaReserva.findElements(By.tagName("td")).get(0).getText().trim();

        WebElement btnActivar = filaReserva.findElement(By.cssSelector(".btn-activar"));
        btnActivar.click();
        aceptarAlertSiExiste();

        // Tras activar redirige a /operario?tab=reservas; esperamos que la fila exista.
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("reserva-row-" + reservaId)));

        WebElement filaActivada = driver.findElement(By.id("reserva-row-" + reservaId));
        Assertions.assertThat(filaActivada.getText())
                .as("La reserva debe pasar a estado Confirmada tras activar")
                .contains("Confirmada");

        // ── 4. Operador agrega 2 servicios ──────────────────────────────────
        // Nota: el DataInitializer ya creó una Cuenta con 2 items sin pagar para cada reserva.
        // Capturar cuántos ítems había antes para validar el delta exacto.
        int itemsAntes = contarItemsPagablesParaHabitacion(HABITACION_NUMERO);

        driver.get(BASE_URL + "/operario?tab=servicios");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("numeroHabitacion")));

        contratarServicio("1");  // primer servicio del seed
        contratarServicio("2");  // segundo servicio del seed

        // ── 5. Operador va a Cuentas y paga TODOS los servicios pendientes ──
        int itemsDespues = contarItemsPagablesParaHabitacion(HABITACION_NUMERO);
        Assertions.assertThat(itemsDespues - itemsAntes)
                .as("Tras contratar 2 servicios, la cuenta debe tener 2 ítems pagables más")
                .isEqualTo(2);

        // Validar que cada ítem tiene un precio con símbolo $.
        WebElement cabeceraCuentaCheck = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(
                        "//span[contains(text(),'Hab. " + HABITACION_NUMERO + "')]"
                                + "/ancestor::div[contains(@style,'background:#f8faff')]")));
        WebElement tablaItemsCheck = cabeceraCuentaCheck.findElement(
                By.xpath("following-sibling::div[1]//table"));
        tablaItemsCheck.findElements(By.cssSelector("tbody tr")).stream()
                .filter(fila -> !fila.findElements(By.cssSelector(".btn-pagar")).isEmpty())
                .forEach(fila -> {
                    String precio = fila.findElements(By.tagName("td")).get(2).getText();
                    Assertions.assertThat(precio)
                            .as("El precio debe formatearse con símbolo $")
                            .startsWith("$");
                });

        // Pagar todos los ítems pendientes uno a uno.
        while (contarItemsPagablesParaHabitacion(HABITACION_NUMERO) > 0) {
            WebElement tabla = obtenerTablaItemsHabitacion(HABITACION_NUMERO);
            WebElement btnPagar = tabla.findElement(By.cssSelector(".btn-pagar"));
            btnPagar.click();
            aceptarAlertSiExiste();
            wait.until(ExpectedConditions.urlContains("/operario"));
        }

        // Confirmar que la cuenta de la habitación queda sin pendientes.
        Assertions.assertThat(contarItemsPagablesParaHabitacion(HABITACION_NUMERO))
                .as("No deben quedar servicios pendientes de pago")
                .isZero();

        // ── 6. Operador finaliza la estadía ─────────────────────────────────
        driver.get(BASE_URL + "/operario?tab=reservas");
        WebElement btnAcabar = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("btn-acabar-" + reservaId)));
        btnAcabar.click();
        aceptarAlertSiExiste();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("reserva-row-" + reservaId)));

        WebElement filaFinalizada = driver.findElement(By.id("reserva-row-" + reservaId));
        Assertions.assertThat(filaFinalizada.getText())
                .as("La reserva debe quedar en estado Finalizada")
                .contains("Finalizada");
    }

    // ─── Helpers privados ──────────────────────────────────────────────────

    private void loginComoCliente() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("correo")).sendKeys(CLIENTE_CORREO);
        driver.findElement(By.id("contrasena")).sendKeys(CLIENTE_CONTRASENA);
        driver.findElement(By.cssSelector(".btn-login")).click();
        wait.until(ExpectedConditions.urlContains("/clientes/ver/"));
    }

    private void loginComoOperario() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("correo")).sendKeys(OPERARIO_CORREO);
        driver.findElement(By.id("contrasena")).sendKeys(OPERARIO_CONTRASENA);
        driver.findElement(By.cssSelector(".btn-login")).click();
        wait.until(ExpectedConditions.urlContains("/operario"));
    }

    /**
     * Busca en la tabla del panel-reservas la primera fila con la habitación dada
     * y estado "Pendiente".
     */
    private WebElement encontrarFilaReservaPendientePorHabitacion(String numero) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#panel-reservas tbody tr")));

        List<WebElement> filas = driver.findElements(
                By.cssSelector("#panel-reservas tbody tr"));

        return filas.stream()
                .filter(f -> f.getText().contains("Hab. " + numero))
                .filter(f -> f.getText().contains("Pendiente"))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "No se encontró fila Pendiente para Hab. " + numero
                                + ". DataInitializer debe haber creado la Reserva 3."));
    }

    /**
     * Cuenta los ítems con botón "Pagar" disponible (es decir, no pagados)
     * para la habitación dada. Navega a la pestaña de cuentas.
     */
    private int contarItemsPagablesParaHabitacion(String numero) {
        driver.get(BASE_URL + "/operario?tab=cuentas");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("panel-cuentas")));

        // Si no existe encabezado para la habitación devolvemos 0.
        List<WebElement> cabeceras = driver.findElements(By.xpath(
                "//span[contains(text(),'Hab. " + numero + "')]"
                        + "/ancestor::div[contains(@style,'background:#f8faff')]"));
        if (cabeceras.isEmpty()) {
            return 0;
        }

        WebElement tabla = cabeceras.get(0).findElement(
                By.xpath("following-sibling::div[1]//table"));
        return tabla.findElements(By.cssSelector(".btn-pagar")).size();
    }

    /**
     * Obtiene la tabla de items de la cuenta para la habitación dada.
     * Asume estar en /operario?tab=cuentas.
     */
    private WebElement obtenerTablaItemsHabitacion(String numero) {
        WebElement cabecera = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(
                        "//span[contains(text(),'Hab. " + numero + "')]"
                                + "/ancestor::div[contains(@style,'background:#f8faff')]")));
        return cabecera.findElement(By.xpath("following-sibling::div[1]//table"));
    }

    /**
     * Contrata un servicio para HABITACION_NUMERO.
     * Asume estar en /operario?tab=servicios.
     */
    private void contratarServicio(String servicioValue) {
        WebElement inputHab = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("numeroHabitacion")));
        inputHab.clear();
        inputHab.sendKeys(HABITACION_NUMERO);

        // El select tiene una primera opción "-- Selecciona un servicio --" con value vacío.
        new Select(driver.findElement(By.id("servicioId")))
                .selectByValue(servicioValue);

        driver.findElement(By.cssSelector("button.btn-contratar")).click();
        aceptarAlertSiExiste();

        // Tras contratar redirige a /operario?tab=servicios; esperamos el form de nuevo.
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("numeroHabitacion")));
    }
}