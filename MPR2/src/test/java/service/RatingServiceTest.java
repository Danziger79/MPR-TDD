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
}