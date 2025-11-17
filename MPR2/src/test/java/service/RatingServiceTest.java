package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RatingServiceTest {

    private RatingService ratingService;
    private Employee pracownik;

    @BeforeEach
    void setUp() {
        // Klasa jeszcze nie istnieje!
        ratingService = new RatingService();
        pracownik = new Employee("Anna Nowak", "anna@tech.pl", "TechCorp",
                Position.PROGRAMISTA, 9000);
    }

    @Test
    @DisplayName("Dodanie nowej oceny (1-5) powinno dodać ją do historii pracownika")
    void shouldAddValidRatingToEmployeeHistory() {
        // Arrange
        int ocena = 4;

        // Act
        // Metoda jeszcze nie istnieje!
        ratingService.addRating(pracownik, ocena);


        assertThat(pracownik.getRatingHistory())
                .as("Historia ocen powinna zawierać nową ocenę")
                .isNotEmpty() // Sprawdzamy, czy nie jest pusta
                .hasSize(1)     // Sprawdzamy, czy ma 1 element
                .contains(4);   // Sprawdzamy, czy ten element to 4
    }
    @Test
    @DisplayName("Obliczanie średniej oceny powinno zwrócić poprawną wartość")
    void shouldCalculateAverageRatingCorrectly() {
        // Arrange


        pracownik.addRating(3);
        pracownik.addRating(4);
        pracownik.addRating(5);
        // Oczekiwana średnia: (3+4+5) / 3 = 4.0

        // Act
        // Ta metoda jeszcze nie istnieje! Spowoduje błąd kompilacji.
        double average = ratingService.getAverageRating(pracownik);

        // Assert (Używamy AssertJ)
        assertThat(average)
                .as("Średnia ocen jest niepoprawna")
                .isEqualTo(4.0); // Asercja AssertJ
}
    @Test
    @DisplayName("Obliczanie średniej dla pracownika bez ocen powinno zwrócić 0.0")
    void shouldReturnZeroAverageForEmployeeWithNoRatings() {
        // Arrange


        // Act

        double average = ratingService.getAverageRating(pracownik);

        assertThat(average)
                .as("Średnia dla braku ocen powinna wynosić 0.0")
                .isEqualTo(0.0);
    }
    @ParameterizedTest(name = "Ocena {0} jest nieprawidłowa i powinna rzucić wyjątkiem")
    @ValueSource(ints = { -5, 0, 6, 100 }) // 4 przypadki
    void shouldThrowExceptionForInvalidRating(int invalidRating) {
        // Assert
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    ratingService.addRating(pracownik, invalidRating);
                })
                .withMessage("Ocena musi być w skali 1-5");
    }
    @ParameterizedTest(name = "Średnia dla ocen {0} powinna wynosić {1}")
    @MethodSource("provideRatingLists")
    void shouldCalculateAverageForVariousRatingLists(List<Integer> ratings, double expectedAverage) {

        for (int r : ratings) {
            pracownik.addRating(r);
        }


        double average = ratingService.getAverageRating(pracownik);


        assertThat(average).isEqualTo(expectedAverage);
    }


    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> provideRatingLists() {
        return java.util.stream.Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(List.of(5, 5, 5), 5.0),
                org.junit.jupiter.params.provider.Arguments.of(List.of(1, 2, 3), 2.0),
                org.junit.jupiter.params.provider.Arguments.of(List.of(1, 5), 3.0),
                org.junit.jupiter.params.provider.Arguments.of(List.of(4), 4.0)
        );
    }
}
