package model;

// Importujemy adnotacje do testów parametryzowanych (z wykładu!)
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    // To jest ten super test z wykładu.
    // Zamiast pisać 5 testów, piszemy JEDEN, który odpali się 5 razy
    // dla każdego zestawu danych z @CsvSource.
    @ParameterizedTest
    @DisplayName("Sprawdzenie atrybutów stanowisk")
    @CsvSource({
            // Format: "NAZWA_ENUMA, OCZEKIWANA_PENSJA, OCZEKIWANY_POZIOM"
            "PREZES,     25000, 1",
            "WICEPREZES, 18000, 2",
            "MANAGER,    12000, 3",
            "PROGRAMISTA, 8000, 4",
            "STAZYSTA,    3000, 5"
    })
    void testPositionAttributes(String positionName, double expectedSalary, int expectedLevel) {
        // Arrange
        // Konwertujemy String "PREZES" na enum Position.PREZES
        Position pos = Position.valueOf(positionName);

        // Act (pobieramy wartości z enuma)
        double actualSalary = pos.getBaseSalary();
        int actualLevel = pos.getHierarchyLevel();

        // Assert (sprawdzamy obie wartości)
        assertEquals(expectedSalary, actualSalary, "Bazowa pensja dla " + positionName + " się nie zgadza");
        assertEquals(expectedLevel, actualLevel, "Poziom hierarchii dla " + positionName + " się nie zgadza");
    }
}