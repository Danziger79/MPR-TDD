package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.*;

class TenureServiceTest {

    private TenureService tenureService;
    private Employee pracownik;


    private final LocalDate TODAY = LocalDate.of(2025, 11, 17);

    @BeforeEach
    void setUp() {
        // Klasa jeszcze nie istnieje!
        tenureService = new TenureService();
    }

    @Test
    @DisplayName("Pracownik zatrudniony dokładnie 2 lata temu powinien mieć 2 lata stażu")
    void shouldCalculateTenureExactlyInYears() {
        // Arrange
        // Pracownik zatrudniony DOKŁADNIE 2 lata temu
        LocalDate dataZatrudnienia = LocalDate.of(2023, 11, 17);
        pracownik = new Employee("Anna Nowak", "anna@tech.pl", "TechCorp",
                Position.PROGRAMISTA, 9000, dataZatrudnienia);

        // Act
        // Metoda jeszcze nie istnieje!
        long staz = tenureService.getTenureInYears(pracownik, TODAY);


        assertThat(staz)
                .as("Staż w latach powinien wynosić 2")
                .isEqualTo(2L); // 'L' na końcu, bo to typ 'long'
    }
}