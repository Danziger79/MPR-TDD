package exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

// Import statyczny dla AssertJ
import static org.assertj.core.api.Assertions.assertThat;

// Klasa testowa weryfikująca poprawność konstrukcji niestandardowego wyjątku ApiException.
// Sprawdza, czy obiekt wyjątku poprawnie przechowuje komunikaty błędów oraz ich przyczyny (root cause).
class ApiExceptionTest {

    @Test
    @DisplayName("Konstruktor z samą wiadomością powinien poprawnie ustawić komunikat błędu")
    void shouldCreateExceptionWithMessageOnly() {
        // Arrange
        String expectedMessage = "Nieudane połączenie z bramką płatności";

        // Act
        ApiException exception = new ApiException(expectedMessage);

        // Assert
        // Weryfikacja, czy wiadomość została przekazana do klasy nadrzędnej (Throwable)
        assertThat(exception)
                .as("Wyjątek powinien zawierać przekazany komunikat")
                .hasMessage(expectedMessage)
                .hasNoCause(); // Upewniamy się, że nie ma przypisanej przyczyny
    }

    @Test
    @DisplayName("Konstruktor z wiadomością i przyczyną powinien zachować łańcuch wyjątków (Exception Chaining)")
    void shouldCreateExceptionWithMessageAndCause() {
        // Arrange
        String expectedMessage = "Błąd krytyczny podczas pobierania danych";
        // Symulujemy wyjątek niskopoziomowy (np. błąd IO), który jest przyczyną
        Throwable rootCause = new IOException("Connection refused: port 8080");

        // Act
        // Tworzymy nasz wyjątek, "opakowując" w niego przyczynę
        ApiException exception = new ApiException(expectedMessage, rootCause);

        // Assert
        assertThat(exception)
                .as("Wyjątek nadrzędny musi zawierać własny komunikat")
                .hasMessage(expectedMessage);

        assertThat(exception.getCause())
                .as("Wyjątek musi przechowywać pierwotną przyczynę błędu (root cause)")
                .isNotNull()
                .isEqualTo(rootCause) // Sprawdzamy referencję
                .isInstanceOf(IOException.class)
                .hasMessage("Connection refused: port 8080");
    }
}