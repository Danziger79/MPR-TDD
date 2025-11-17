package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


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
}
