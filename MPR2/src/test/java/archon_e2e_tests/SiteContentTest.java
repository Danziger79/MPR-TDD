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

public class SiteContentTest {
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
    @DisplayName("Checks if news carousel is displayed correctly")
    void shouldCheckIfCarouselIsDisplayedCorrectly() {
//        given
        List<WebElement> newsCards = driver.findElements(By.cssSelector(".swiper-slide"));
        WebElement first = newsCards.get(0);
        WebElement second = newsCards.get(1);
        WebElement third = newsCards.get(2);
//        when, then
        Assertions.assertAll(
                () -> Assertions.assertTrue(first.isDisplayed()),
                () -> Assertions.assertTrue(second.isDisplayed()),
                () -> Assertions.assertTrue(third.isDisplayed())
        );
    }

    @Test
    @DisplayName("Checks if posts image is displayed")
    void shouldCheckIfPostImageIsDisplayed() {
//        given
        WebElement withImagePost = driver.findElement(By.cssSelector(".swiper-slide"));
//        when
        withImagePost.click();
        wait.until(ExpectedConditions.urlContains("news/59"));
        WebElement image = driver.findElement(By.cssSelector("img.news-image"));
//        when, then
        Assertions.assertTrue(image.isDisplayed());
    }

    @Test
    @DisplayName("Checks if posts image is displayed")
    void checksIfNewsTitleIsCorrect() {
//        given
        WebElement withImagePost = driver.findElement(By.cssSelector(".swiper-slide"));
//        when
        withImagePost.click();
        wait.until(ExpectedConditions.urlContains("news/59"));
        String title = driver.findElement(By.cssSelector("h1")).getText();
//        when, then
        Assertions.assertEquals("SeleniumTest", title);
    }


}

//dodawanie i usuwanie
