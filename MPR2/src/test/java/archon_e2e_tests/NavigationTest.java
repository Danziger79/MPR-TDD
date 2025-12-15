package archon_e2e_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class NavigationTest {

    private static final String BASE_URL = "http://localhost/ProjectArchon/";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Checks if redirection to about work correctly")
    void shouldRedirectToAboutSection() {
//        given
        WebElement about = driver.findElement(By.linkText("O nas"));
//        when
        about.click();
        wait.until(ExpectedConditions.urlContains("/about"));
//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("/about")),
                () -> Assertions.assertTrue(
                        driver.findElement(By.linkText("O nas"))
                                .getAttribute("class")
                                .contains("active")
                )
        );
    }

    @Test
    @DisplayName("Checks if redirection to news work correctly")
    void shouldRedirectToNewsSection() {
        //        given
        WebElement about = driver.findElement(By.linkText("Aktualności"));
//        when
        about.click();
        wait.until(ExpectedConditions.urlContains("/news"));
//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("/news")),
                () -> Assertions.assertTrue(
                        driver.findElement(By.linkText("Aktualności"))
                                .getAttribute("class")
                                .contains("active")
                )
        );
    }

    @Test
    @DisplayName("Checks if redirection to project work correctly")
    void shouldRedirectToProjectSection() {
        //        given
        WebElement about = driver.findElement(By.linkText("Projekt Działań"));
//        when
        about.click();
        wait.until(ExpectedConditions.urlContains("/project"));
//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("/project")),
                () -> Assertions.assertTrue(
                        driver.findElement(By.linkText("Projekt Działań"))
                                .getAttribute("class")
                                .contains("active")
                )
        );
    }

    @Test
    @DisplayName("Checks if redirection to stories work correctly")
    void shouldRedirectToStoriesSection() {
        //        given
        WebElement about = driver.findElement(By.linkText("Archiwum"));
//        when
        about.click();
        wait.until(ExpectedConditions.urlContains("/stories"));
//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("/stories")),
                () -> Assertions.assertTrue(
                        driver.findElement(By.linkText("Archiwum"))
                                .getAttribute("class")
                                .contains("active")
                )
        );
    }

    @Test
    @DisplayName("Checks if redirection to contact work correctly")
    void shouldRedirectToContactSection() {
        //        given
        WebElement about = driver.findElement(By.linkText("Kontakt"));
//        when
        about.click();
        wait.until(ExpectedConditions.urlContains("/contact"));
//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("/contact")),
                () -> Assertions.assertTrue(
                        driver.findElement(By.linkText("Kontakt"))
                                .getAttribute("class")
                                .contains("active")
                )
        );
    }

    @Test
    @DisplayName("Checks if redirection to login work correctly")
    void shouldRedirectToLogin() {
//        given
        WebElement loginRedirect = driver.findElement(By.linkText("Dla Pracownika"));
//        when
        loginRedirect.click();
        wait.until(ExpectedConditions.urlContains("/login"));
//        then
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Test
    @DisplayName("Checks if redirection to chosen news works correctly")
    void shouldRedirectToNews() {
        //        given
        WebElement about = driver.findElement(By.cssSelector(".news-card"));
//        when
        about.click();
        wait.until(ExpectedConditions.urlContains("/news/59"));

//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("/news/59")),
                () -> Assertions.assertTrue(
                        driver.findElement(By.linkText("Aktualności"))
                                .getAttribute("class")
                                .contains("active")
                )
        );
    }
}
