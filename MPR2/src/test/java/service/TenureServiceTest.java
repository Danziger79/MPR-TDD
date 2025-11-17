package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    @ParameterizedTest(name = "Zatrudniony {0}, dzisiaj {1} -> Staż = {2} lat")
    @CsvSource({
            "2025-11-17, 2025-11-17, 0", // Scenariusz 1: Zatrudniony dzisiaj
            "2024-11-18, 2025-11-17, 0", // Scenariusz 2: Zatrudniony 364 dni temu
            "2024-11-17, 2025-11-17, 1", // Scenariusz 3: Zatrudniony dokładnie rok temu
            "2027-01-01, 2025-11-17, 0"  // Scenariusz 4: Zatrudniony w przyszłości (oczekuje 0, nie -2)
    })
    void shouldCalculateTenureForEdgeCases(String hireDateStr, String todayStr, long expectedYears) {
        // Arrange
        LocalDate hireDate = LocalDate.parse(hireDateStr);
        LocalDate today = LocalDate.parse(todayStr);

        Employee testEmployee = new Employee("Test User", "test@test.pl", "Tech", Position.STAZYSTA, 3000, hireDate);


        long staz = tenureService.getTenureInYears(testEmployee, today);


        assertThat(staz)
                .as("Staż dla daty %s", hireDate)
                .isEqualTo(expectedYears);
    }
}