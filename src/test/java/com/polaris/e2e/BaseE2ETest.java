package com.polaris.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase base para las pruebas E2E de Polaris.
 *
 * Polaris es una app Spring Boot + Thymeleaf, por lo que todo el frontend y backend
 * corren en el mismo puerto (8080). No hay un servidor Angular separado.
 *
 * Esta clase configura:
 *  - WebDriver de Chrome con WebDriverManager (descarga el driver automáticamente)
 *  - WebDriverWait con timeout de 10s
 *  - Helpers para:
 *      * cerrar diálogos confirm() nativos (los usa el panel del operario)
 *      * escribir fechas en inputs type="date" (Chrome requiere JS para portabilidad)
 */
public abstract class BaseE2ETest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /** Polaris corre en 8080 (Spring Boot + Thymeleaf, sin frontend separado). */
    protected static final String BASE_URL = "http://localhost:8080";
    protected static final Duration TIMEOUT = Duration.ofSeconds(10);

    /** Formato ISO que usa el modelo (LocalDate.parse lo espera así en el controller). */
    protected static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void init() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        // Descomentar para correr sin abrir ventana del navegador:
        // options.addArguments("--headless=new");

        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ─── Helpers ────────────────────────────────────────────────────────────

    /**
     * Acepta un diálogo confirm() nativo si está presente.
     * Polaris usa `onclick="return confirm(...)"` en los botones Activar/Acabar/Pagar/Eliminar
     * del panel del operario, por lo que cada click muestra un Alert.
     */
    protected void aceptarAlertSiExiste() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {
            // No había alert pendiente; nada que hacer.
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // Tampoco había alert; algunos botones no muestran confirm().
        }
    }

    /**
     * Escribe una fecha en un &lt;input type="date"&gt; vía JavaScript.
     *
     * Los inputs type="date" en Chrome son sensibles al locale y rechazan sendKeys() directo
     * con un string ISO. Asignamos el valor vía JS y disparamos el evento `change` para que
     * cualquier listener (como `onchange="calcularTotal()"` en el formulario de reserva) corra.
     */
    protected void setFecha(WebElement input, LocalDate fecha) {
        String iso = fecha.format(ISO_DATE);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                input, iso);
    }
}