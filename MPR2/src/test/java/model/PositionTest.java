package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

// Klasa testowa weryfikująca poprawność konfiguracji stałych wyliczeniowych w enum Position.
// Zapewnia integralność danych dotyczących widełek płacowych oraz struktury hierarchicznej firmy.
class PositionTest {

    /**
     * Weryfikuje spójność atrybutów (wynagrodzenie bazowe, poziom hierarchii) dla zdefiniowanych stanowisk.
     * Wykorzystuje podejście Data-Driven Testing (testy sparametryzowane) w celu walidacji wielu przypadków
     * przy użyciu jednej metody testowej, co eliminuje redundancję kodu.
     *
     * @param positionName Nazwa stałej wyliczeniowej (String).
     * @param expectedSalary Oczekiwana wartość wynagrodzenia bazowego.
     * @param expectedLevel Oczekiwany poziom w hierarchii organizacyjnej.
     */
    @ParameterizedTest
    @DisplayName("Weryfikacja atrybutów konfiguracyjnych stanowisk")
    @CsvSource({
            // Definicja zestawu danych testowych w formacie: "NAZWA_STANOWISKA, OCZEKIWANA_PENSJA, POZIOM_HIERARCHII"
            "PREZES,     25000, 1",
            "WICEPREZES, 18000, 2",
            "MANAGER,    12000, 3",
            "PROGRAMISTA, 8000, 4",
            "STAZYSTA,    3000, 5"
    })
    void testPositionAttributes(String positionName, double expectedSalary, int expectedLevel) {
        // Dynamiczna konwersja dostarczonej nazwy tekstowej na instancję typu wyliczeniowego Position.
        // W przypadku braku dopasowania nazwy, metoda valueOf rzuci IllegalArgumentException (co również obleje test).
        Position pos = Position.valueOf(positionName);

        // Pobranie rzeczywistych wartości skonfigurowanych w kodzie enum.
        double actualSalary = pos.getBaseSalary();
        int actualLevel = pos.getHierarchyLevel();

        // Weryfikacja zgodności konfiguracji enuma z oczekiwaniami biznesowymi.
        assertEquals(expectedSalary, actualSalary, "Niezgodność wynagrodzenia bazowego dla stanowiska: " + positionName);
        assertEquals(expectedLevel, actualLevel, "Niezgodność poziomu hierarchii dla stanowiska: " + positionName);
    }
}