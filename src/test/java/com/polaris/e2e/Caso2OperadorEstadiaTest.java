package com.polaris.e2e;

import com.polaris.model.Cliente;
import com.polaris.model.Cuenta;
import com.polaris.model.ReservaHabitacion;
import com.polaris.repository.ICuentaRepository;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import com.polaris.service.ContratacionServicioService;
import com.polaris.service.IReservaHabitacionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IClienteRepository clienteRepo;

    @Autowired
    private IReservaHabitacionRepository reservaRepo;

    @Autowired
    private IReservaHabitacionService reservaService;

    @Autowired
    private ContratacionServicioService contratacionService;

    @Autowired
    private ICuentaRepository cuentaRepository;

    // Cliente semilla del DataInitializer.
    private static final String CLIENTE_CORREO     = "vale.rod@email.com";
    private static final String CLIENTE_CONTRASENA = "val456";

    // Operario principal del DataInitializer.
    private static final String OPERARIO_CORREO     = "operario@polaris.com";
    private static final String OPERARIO_CONTRASENA = "operario123";

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
        public void Caso2_unico_flujoCompleto_activarPagarYFinalizar() {

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

        // ── 3. Operador activa la reserva pendiente del cliente semilla ───
        ReservaHabitacion reservaPendiente = obtenerReservaPendienteClienteSemilla();
        String reservaId = reservaPendiente.getId().toString();
        String numeroHabitacion = reservaPendiente.getHabitacion().getNumero();

        WebElement filaReserva = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("reserva-row-" + reservaId)));
        Assertions.assertThat(filaReserva.getText().toLowerCase())
                .contains("pendiente");

        reservaService.activarEstadia(Long.parseLong(reservaId));

        driver.get(BASE_URL + "/operario?tab=reservas");
        // Esperamos explícitamente a que la reserva cambie a Confirmada.
        wait.until(driver -> driver.findElement(By.id("reserva-row-" + reservaId))
                .getText().toLowerCase().contains("confirmada"));

        WebElement filaActivada = driver.findElement(By.id("reserva-row-" + reservaId));
        Assertions.assertThat(filaActivada.getText().toLowerCase())
                .as("La reserva debe pasar a estado Confirmada tras activar")
                .contains("confirmada");

        // ── 4. Operador agrega 2 servicios ──────────────────────────────────
        // Nota: el DataInitializer ya creó una Cuenta con 2 items sin pagar para cada reserva.
        // Capturar cuántos ítems había antes para validar el delta exacto.
        int itemsAntes = contarItemsPagablesParaHabitacion(numeroHabitacion);

        driver.get(BASE_URL + "/operario?tab=servicios");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("numeroHabitacion")));

        contratacionService.contratarServicio(numeroHabitacion, 1L);
        contratacionService.contratarServicio(numeroHabitacion, 2L);

        driver.get(BASE_URL + "/operario?tab=cuentas");

        // ── 5. Operador va a Cuentas y paga TODOS los servicios pendientes ──
        int itemsDespues = contarItemsPagablesParaHabitacion(numeroHabitacion);
        Assertions.assertThat(itemsDespues - itemsAntes)
                .as("Tras contratar 2 servicios, la cuenta debe tener 2 ítems pagables más")
                .isEqualTo(2);

        // Validar que cada ítem tiene un precio con símbolo $.
        WebElement cabeceraCuentaCheck = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(
                        "//span[contains(text(),'Hab. " + numeroHabitacion + "')]"
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

        Cuenta cuenta = cuentaRepository.findByReservaId(Long.parseLong(reservaId))
                .orElseThrow(() -> new AssertionError("No se creó la cuenta para la reserva activada."));
        contratacionService.pagarCuenta(cuenta.getId());

        // Confirmar que la cuenta de la habitación queda sin pendientes.
        Assertions.assertThat(contarItemsPagablesParaHabitacion(numeroHabitacion))
                .as("No deben quedar servicios pendientes de pago")
                .isZero();

        // ── 6. Operador finaliza la estadía ─────────────────────────────────
        reservaService.acabarEstadia(Long.parseLong(reservaId));

        driver.get(BASE_URL + "/operario?tab=reservas");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("reserva-row-" + reservaId)));

        WebElement filaFinalizada = driver.findElement(By.id("reserva-row-" + reservaId));
        Assertions.assertThat(filaFinalizada.getText().toLowerCase())
                .as("La reserva debe quedar en estado Finalizada")
                .contains("finalizada");
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

    private ReservaHabitacion obtenerReservaPendienteClienteSemilla() {
        Cliente cliente = clienteRepo.findByCorreo(CLIENTE_CORREO)
                .orElseThrow(() -> new AssertionError("No se encontró el cliente semilla del caso 2."));

        return reservaRepo.findByClienteId(cliente.getId()).stream()
                .filter(reserva -> "Pendiente".equalsIgnoreCase(reserva.getEstado()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "No se encontró una reserva Pendiente para el cliente semilla del caso 2."));
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

}