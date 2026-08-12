package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeleniumOrderTestv3 {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSubmitRequest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1. Заполняем поля
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Сергей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79201355621");

        // 2. Чекбокс - кликаем по видимому элементу
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-test-id='agreement']")
        ));
        checkbox.click();

        // 3. Кнопка - пробуем разные способы клика
        WebElement button = driver.findElement(By.cssSelector(".button.button_view_extra.button_theme_alfa-on-white"));

        // Способ 1: Обычный клик
        try {
            button.click();
            System.out.println("Обычный клик сработал");
        } catch (Exception e) {
            System.out.println("Обычный клик не сработал: " + e.getMessage());

            // Способ 2: Клик через JavaScript
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                System.out.println("JavaScript клик сработал");
            } catch (Exception e2) {
                // Способ 3: Клик через Actions
                try {
                    org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                    actions.moveToElement(button).click().perform();
                    System.out.println("Actions клик сработал");
                } catch (Exception e3) {
                    System.out.println("Все способы клика не сработали");
                }
            }
        }

        // 4. Ждём и проверяем результат
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем, появилось ли сообщение об успехе (ищем разными способами)
        WebElement element = null;

        // Способ 1: по классу alert-success
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("alert-success")
            ));
            System.out.println("Найдено по alert-success");
        } catch (Exception e) {
            System.out.println("Не найдено по alert-success");
        }

        // Способ 2: по XPath
        if (element == null) {
            try {
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(), 'успешно отправлена')]")
                ));
                System.out.println("Найдено по XPath");
            } catch (Exception e) {
                System.out.println("Не найдено по XPath");
            }
        }

        // Способ 3: по CSS с частичным совпадением
        if (element == null) {
            try {
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[class*='alert']")
                ));
                System.out.println("Найдено по [class*='alert']");
            } catch (Exception e) {
                System.out.println("Не найдено по [class*='alert']");
            }
        }

        // Если ничего не найдено - выводим всю страницу
        if (element == null) {
            System.out.println("=== СООБЩЕНИЕ НЕ НАЙДЕНО ===");
            System.out.println("HTML страницы:");
            System.out.println(driver.getPageSource());
            assertTrue(false, "Сообщение об успехе не найдено");
        }

        assertTrue(element.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                element.getText().trim());
    }
}
