package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

// Import statyczny dla AssertJ
import static org.assertj.core.api.Assertions.*;

// Klasa testowa weryfikująca logikę obliczania stażu pracy (TenureService).
// Skupia się na poprawności obliczeń kalendarzowych oraz obsłudze dat granicznych i przyszłych.
class TenureServiceTest {

    private TenureService tenureService;
    private Employee employee;

    // Stała data odniesienia ("Dzisiaj") używana w testach, aby zapewnić determinizm wyników
    // niezależnie od faktycznej daty uruchomienia testu.
    private final LocalDate FIXED_CURRENT_DATE = LocalDate.of(2025, 11, 17);

    @BeforeEach
    void setUp() {
        tenureService = new TenureService();
    }

    @Test
    @DisplayName("Pracownik zatrudniony dokładnie 2 lata temu powinien posiadać 2 lata stażu")
    void shouldCalculateTenureExactlyInYears() {
        // Arrange
        // Scenariusz "Happy Path": Data zatrudnienia przypada dokładnie w rocznicę.
        LocalDate hireDate = LocalDate.of(2023, 11, 17);

        employee = new Employee("Anna Nowak", "anna@tech.pl", "TechCorp",
                Position.PROGRAMISTA, 9000, hireDate);

        // Act
        long tenure = tenureService.getTenureInYears(employee, FIXED_CURRENT_DATE);

        // Assert (AssertJ)
        assertThat(tenure)
                .as("Staż liczony w pełnych latach powinien wynosić dokładnie 2")
                .isEqualTo(2L);
    }

    /**
     * Testy parametryzowane pokrywające przypadki brzegowe (Boundary Value Analysis).
     * Weryfikują zachowanie serwisu dla:
     * 1. Dnia zatrudnienia (0 lat).
     * 2. Dnia przed pełną rocznicą (nadal 0 lat - nie zaokrąglamy w górę).
     * 3. Pełnej rocznicy (1 rok).
     * 4. Daty przyszłej (zabezpieczenie przed wartościami ujemnymi).
     */
    @ParameterizedTest(name = "Zatrudniony {0}, Data Raportu {1} -> Oczekiwany Staż: {2} lat")
    @CsvSource({
            "2025-11-17, 2025-11-17, 0", // Scenariusz: Zatrudniony w dniu raportu
            "2024-11-18, 2025-11-17, 0", // Scenariusz: 364 dni stażu (1 dzień przed rocznicą)
            "2024-11-17, 2025-11-17, 1", // Scenariusz: Dokładnie rok stażu
            "2027-01-01, 2025-11-17, 0"  // Scenariusz: Data zatrudnienia w przyszłości
    })
    void shouldCalculateTenureForEdgeCases(String hireDateStr, String reportDateStr, long expectedYears) {
        // Arrange
        LocalDate hireDate = LocalDate.parse(hireDateStr);
        LocalDate reportDate = LocalDate.parse(reportDateStr);

        Employee testEmployee = new Employee("Test User", "test@test.pl", "Tech",
                Position.STAZYSTA, 3000, hireDate);

        // Act
        long tenure = tenureService.getTenureInYears(testEmployee, reportDate);

        // Assert
        assertThat(tenure)
                .as("Błędne wyliczenie stażu dla daty zatrudnienia: %s i daty raportu: %s", hireDate, reportDate)
                .isEqualTo(expectedYears);
    }
}