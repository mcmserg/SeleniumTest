package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeleniumValidationOfInvalidTests {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldcheckForIncompletePhoneFieldEntry() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Сергей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79201355");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();


        WebElement element = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        assertTrue(element.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", element.getText().trim());
    }
    @Test
    void shouldEmptyFieldPhoneCheck() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Сергей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(" ");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();


        WebElement element = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        assertTrue(element.isDisplayed());
        assertEquals("Поле обязательно для заполнения", element.getText().trim());
    }
    @Test
    void shouldcheckingThePhoneFieldForLetters() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Сергей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("PHONE");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();


        WebElement element = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        assertTrue(element.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", element.getText().trim());
    }
    @Test
    void shouldCheckingNameFieldsInLatin() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Petov");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79201355621");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();


        WebElement element = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        assertTrue(element.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", element.getText().trim());
    }
    @Test
    void shouldCheckingTheNameFieldForSpaces() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("  ");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79201355621");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();


        WebElement element = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        assertTrue(element.isDisplayed());
        assertEquals("Поле обязательно для заполнения", element.getText().trim());
    }
    @Test
    void shouldUncheckedCheckboxTest() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Сергей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79201355621");
        driver.findElement(By.tagName("button")).click();


        WebElement element = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));

        assertTrue(element.isDisplayed());
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", element.getText().trim());
    }
}