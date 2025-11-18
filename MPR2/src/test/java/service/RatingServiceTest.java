package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

// Import statyczny dla biblioteki AssertJ
import static org.assertj.core.api.Assertions.*;

// Klasa testowa weryfikująca logikę systemu oceniania (RatingService).
// Sprawdza poprawność dodawania ocen, walidację zakresu oraz algorytmy obliczania średniej.
class RatingServiceTest {

    private RatingService ratingService;
    private Employee employee;

    // Inicjalizacja środowiska testowego.
    @BeforeEach
    void setUp() {
        ratingService = new RatingService();
        // Utworzenie przykładowego pracownika do testów
        employee = new Employee("Anna Nowak", "anna@tech.pl", "TechCorp",
                Position.PROGRAMISTA, 9000);
    }

    @Test
    @DisplayName("Dodanie nowej oceny (1-5) powinno poprawnie zaktualizować historię ocen pracownika")
    void shouldAddValidRatingToEmployeeHistory() {
        // Arrange
        int validRating = 4;

        // Act
        ratingService.addRating(employee, validRating);

        // Assert (AssertJ)
        // Weryfikacja stanu listy ocen w obiekcie pracownika.
        assertThat(employee.getRatingHistory())
                .as("Historia ocen powinna zawierać dodaną notę")
                .isNotEmpty()
                .hasSize(1)
                .contains(validRating);
    }

    @Test
    @DisplayName("Obliczanie średniej oceny powinno zwrócić poprawną wartość arytmetyczną")
    void shouldCalculateAverageRatingCorrectly() {
        // Arrange
        // Ręczne dodanie ocen do pracownika (z pominięciem serwisu, aby izolować test obliczeń)
        employee.addRating(3);
        employee.addRating(4);
        employee.addRating(5);
        // Oczekiwana kalkulacja: (3+4+5) / 3 = 12 / 3 = 4.0

        // Act
        double average = ratingService.getAverageRating(employee);

        // Assert
        assertThat(average)
                .as("Obliczona średnia ocen jest niepoprawna")
                .isEqualTo(4.0);
    }

    @Test
    @DisplayName("Obliczanie średniej dla pracownika bez historii ocen powinno zwrócić 0.0")
    void shouldReturnZeroAverageForEmployeeWithNoRatings() {
        // Arrange
        // Pracownik 'employee' jest świeżo utworzony w setUp(), więc ma pustą listę ocen.

        // Act
        double average = ratingService.getAverageRating(employee);

        // Assert
        assertThat(average)
                .as("Średnia dla pustej historii ocen powinna wynosić 0.0 (uniknięcie NaN)")
                .isEqualTo(0.0);
    }

    /**
     * Test sparametryzowany weryfikujący walidację danych wejściowych.
     * Sprawdza, czy system odrzuca oceny spoza dozwolonego zakresu 1-5.
     *
     * @param invalidRating Wartość oceny, która powinna wywołać błąd.
     */
    @ParameterizedTest(name = "Próba dodania oceny {0} powinna skutkować wyjątkiem walidacji")
    @ValueSource(ints = { -5, 0, 6, 100 }) // Zbiór wartości brzegowych i niepoprawnych
    void shouldThrowExceptionForInvalidRating(int invalidRating) {
        // Assert
        assertThatIllegalArgumentException()
                .as("Serwis powinien rzucić wyjątek dla oceny spoza skali 1-5")
                .isThrownBy(() -> {
                    ratingService.addRating(employee, invalidRating);
                })
                .withMessage("Ocena musi być w skali 1-5");
    }

    /**
     * Zaawansowany test sparametryzowany (Data-Driven Test).
     * Weryfikuje poprawność obliczania średniej dla różnych zestawów danych dostarczanych przez metodę pomocniczą.
     */
    @ParameterizedTest(name = "Dla listy ocen {0} średnia powinna wynosić {1}")
    @MethodSource("provideRatingLists")
    void shouldCalculateAverageForVariousRatingLists(List<Integer> ratings, double expectedAverage) {
        // Arrange
        for (int r : ratings) {
            employee.addRating(r);
        }

        // Act
        double average = ratingService.getAverageRating(employee);

        // Assert
        assertThat(average)
                .as("Błędna średnia dla zestawu danych: " + ratings)
                .isEqualTo(expectedAverage);
    }

    // Metoda dostarczająca strumień argumentów do testu sparametryzowanego.
    // Każdy element strumienia to para: (Lista Ocen, Oczekiwana Średnia).
    private static Stream<Arguments> provideRatingLists() {
        return Stream.of(
                Arguments.of(List.of(5, 5, 5), 5.0),       // Same piątki
                Arguments.of(List.of(1, 2, 3), 2.0),       // Prosta sekwencja
                Arguments.of(List.of(1, 5), 3.0),          // Skrajne wartości
                Arguments.of(List.of(4), 4.0)              // Pojedyncza ocena
        );
    }
}