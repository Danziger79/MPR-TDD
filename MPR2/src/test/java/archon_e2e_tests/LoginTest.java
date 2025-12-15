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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginTest {
    private static final String BASE_URL = "http://localhost/ProjectArchon/login";
    private static final By LOGIN_INPUT = By.name("login");
    private static final By PASSWORD_INPUT = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By LOGOUT_BUTTON = By.cssSelector(".dashboard-action-card.dashboard-action-card--danger");

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
    @DisplayName("Checks if login form is displayed correctly")
    void shouldDisplayLoginForm() {
//        given
        WebElement loginInput = driver.findElement(LOGIN_INPUT);
        WebElement passwordInput = driver.findElement(PASSWORD_INPUT);
//        when, then
        Assertions.assertAll(
                () -> Assertions.assertTrue(loginInput.isDisplayed()),
                () -> Assertions.assertTrue(passwordInput.isDisplayed())
        );
    }

    @Test
    @DisplayName("Checks if user was redirected to admin dashboard after successful login")
    void shouldRedirectWhenLoginSuccessful() {
//        given
        driver.findElement(LOGIN_INPUT).sendKeys("Admin");
        driver.findElement(PASSWORD_INPUT).sendKeys("1234");
        driver.findElement(LOGIN_BUTTON).click();
//        when
        wait.until(ExpectedConditions.urlContains("/admin/dashboard"));
//        then
        assertThat(driver.getCurrentUrl()).contains("/admin/dashboard");
    }

    @Test
    @DisplayName("Checks if admin dashboard is displayed after successful login")
    void shouldDisplayDashBoardUIAfterSuccessfulLogin() {
//        given
        driver.findElement(LOGIN_INPUT).sendKeys("Admin");
        driver.findElement(PASSWORD_INPUT).sendKeys("1234");
        driver.findElement(LOGIN_BUTTON).click();
//        when
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON));
        List<WebElement> dashboardCards = driver.findElements(By.cssSelector(".dashboard-action-card"));

//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(logoutButton.isDisplayed()),
                () -> Assertions.assertEquals(6, dashboardCards.size())
        );

    }

    @Test
    @DisplayName("Checks if user was redirected after clicking logout button")
    void shouldRedirectAfterLogout() {
//        given
        driver.findElement(LOGIN_INPUT).sendKeys("Admin");
        driver.findElement(PASSWORD_INPUT).sendKeys("1234");
        driver.findElement(LOGIN_BUTTON).click();
//        when
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON));
        logoutButton.click();
        wait.until(ExpectedConditions.urlContains("/login"));
//        then
        assertThat(driver.getCurrentUrl()).contains("/login");

    }

    @Test
    @DisplayName("Checks if user still remains on login page after failing to login")
    void shouldRemainOnLoginPageWhenFailToLogin() {
//        given
        driver.findElement(LOGIN_INPUT).sendKeys("Admin1");
        driver.findElement(PASSWORD_INPUT).sendKeys("1234");
        driver.findElement(LOGIN_BUTTON).click();
//        when
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error")));
        WebElement message = driver.findElement(By.cssSelector(".error"));
//        then
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.getCurrentUrl().contains("/login")),
                () -> Assertions.assertEquals("Nieprawidłowy login lub hasło", message.getText())
        );
    }


}
