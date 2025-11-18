package service;

import model.CompanyStatistics;
import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// Klasa testowa weryfikująca integralność i poprawność logiki biznesowej serwisu EmployeeService.
// Pokrywa scenariusze pozytywne (Happy Path), negatywne oraz przypadki brzegowe (Edge Cases).
class EmployeeServiceTest {

    // System Under Test (SUT)
    private EmployeeService service;

    // Dane testowe (Fixtures)
    private Employee e1_anna_techcorp;
    private Employee e2_piotr_devhouse;
    private Employee e3_jan_techcorp;
    private Employee e4_ewa_underpaid;

    // Inicjalizacja środowiska testowego przed każdym uruchomieniem metody testowej.
    // Zapewnia izolację testów poprzez resetowanie stanu serwisu i obiektów domenowych.
    @BeforeEach
    void setUp() {
        service = new EmployeeService();

        // Inicjalizacja przykładowych pracowników
        e1_anna_techcorp = new Employee("Anna Nowak", "anna@techcorp.pl", "TechCorp", Position.PROGRAMISTA, 9000);
        e2_piotr_devhouse = new Employee("Piotr Wisniewski", "piotr@devhouse.pl", "DevHouse", Position.MANAGER, 12500);
        e3_jan_techcorp = new Employee("Jan Kowalski", "jan@techcorp.pl", "TechCorp", Position.PROGRAMISTA, 8500);

        // Pracownik z wynagrodzeniem poniżej minimum dla stanowiska (Baza dla STAZYSTA: 3000)
        e4_ewa_underpaid = new Employee("Ewa Zielinska", "ewa@techcorp.pl", "TechCorp", Position.STAZYSTA, 2500);
    }

    // --- Testy: Operacje CRUD (Create, Read) ---

    @Test
    @DisplayName("Powinien pomyślnie dodać nowego pracownika (unikalny email)")
    void addEmployee_NewEmployee_ShouldReturnTrue() {
        // Act
        boolean result = service.addEmployee(e1_anna_techcorp);

        // Assert
        assertTrue(result, "Metoda powinna zwrócić true po dodaniu unikalnego pracownika.");
    }

    @Test
    @DisplayName("Nie powinien dodać pracownika, jeśli email już istnieje (duplikat)")
    void addEmployee_DuplicateEmail_ShouldReturnFalse() {
        // Arrange
        service.addEmployee(e1_anna_techcorp);
        Employee duplicate = new Employee("Imposter", "anna@techcorp.pl", "InnaFirma", Position.PREZES, 99999);

        // Act
        boolean result = service.addEmployee(duplicate);

        // Assert
        assertFalse(result, "System powinien odrzucić próbę dodania pracownika z istniejącym adresem email.");
    }

    // --- Testy: Wyszukiwanie i Filtrowanie ---

    @Test
    @DisplayName("Powinien zwrócić listę pracowników przefiltrowaną po nazwie firmy")
    void findByCompany_ShouldReturnCorrectEmployees() {
        // Arrange
        service.addEmployee(e1_anna_techcorp);
        service.addEmployee(e2_piotr_devhouse);
        service.addEmployee(e3_jan_techcorp);

        // Act
        List<Employee> techCorpList = service.findByCompany("TechCorp");
        List<Employee> devHouseList = service.findByCompany("DevHouse");

        // Assert
        assertEquals(2, techCorpList.size(), "Nieprawidłowa liczba pracowników dla TechCorp.");
        assertTrue(techCorpList.contains(e1_anna_techcorp));
        assertTrue(techCorpList.contains(e3_jan_techcorp));

        assertEquals(1, devHouseList.size(), "Nieprawidłowa liczba pracowników dla DevHouse.");
        assertTrue(devHouseList.contains(e2_piotr_devhouse));
    }

    @Test
    @DisplayName("Wyszukiwanie po firmie powinno ignorować wielkość liter")
    void findByCompany_CaseInsensitive_ShouldReturnEmployees() {
        // Arrange
        service.addEmployee(e1_anna_techcorp);

        // Act
        List<Employee> result = service.findByCompany("techcorp");

        // Assert
        assertEquals(1, result.size(), "Mechanizm wyszukiwania powinien być case-insensitive.");
    }

    @Test
    @DisplayName("Wyszukiwanie nieistniejącej firmy powinno zwrócić pustą listę (nie null)")
    void findByCompany_NonExistentCompany_ShouldReturnEmptyList() {
        // Arrange
        service.addEmployee(e1_anna_techcorp);

        // Act
        List<Employee> result = service.findByCompany("NonExistentCorp");

        // Assert
        assertNotNull(result, "Metoda nie powinna zwracać wartości null.");
        assertTrue(result.isEmpty(), "Dla nieznanej firmy lista powinna być pusta.");
    }

    // --- Testy: Sortowanie i Agregacja ---

    @Test
    @DisplayName("Powinien posortować listę pracowników alfabetycznie według imienia")
    void sortByName_ShouldReturnSortedList() {
        // Arrange
        service.addEmployee(e2_piotr_devhouse); // Piotr
        service.addEmployee(e1_anna_techcorp);  // Anna
        service.addEmployee(e3_jan_techcorp);   // Jan

        // Act
        List<Employee> sorted = service.sortByName();

        // Assert
        assertEquals(3, sorted.size());
        assertEquals(e1_anna_techcorp, sorted.get(0), "Błąd sortowania: Anna powinna być pierwsza.");
        assertEquals(e3_jan_techcorp, sorted.get(1), "Błąd sortowania: Jan powinien być drugi.");
        assertEquals(e2_piotr_devhouse, sorted.get(2), "Błąd sortowania: Piotr powinien być trzeci.");
    }

    @Test
    @DisplayName("Powinien poprawnie zgrupować pracowników według stanowiska")
    void groupByPosition_ShouldGroupCorrectly() {
        // Arrange
        service.addEmployee(e1_anna_techcorp); // PROGRAMISTA
        service.addEmployee(e2_piotr_devhouse); // MANAGER
        service.addEmployee(e3_jan_techcorp);  // PROGRAMISTA

        // Act
        Map<Position, List<Employee>> grouped = service.groupByPosition();

        // Assert
        assertEquals(2, grouped.size(), "Oczekiwano grup dla 2 różnych stanowisk.");
        assertTrue(grouped.containsKey(Position.PROGRAMISTA));
        assertTrue(grouped.containsKey(Position.MANAGER));

        assertEquals(2, grouped.get(Position.PROGRAMISTA).size(), "Grupa PROGRAMISTA powinna zawierać 2 osoby.");
        assertEquals(1, grouped.get(Position.MANAGER).size(), "Grupa MANAGER powinna zawierać 1 osobę.");
    }

    @Test
    @DisplayName("Powinien zwrócić poprawne liczniki pracowników dla każdego stanowiska")
    void countByPosition_ShouldCountCorrectly() {
        // Arrange
        service.addEmployee(e1_anna_techcorp);
        service.addEmployee(e2_piotr_devhouse);
        service.addEmployee(e3_jan_techcorp);

        // Act
        Map<Position, Integer> counts = service.countByPosition();

        // Assert
        assertEquals(2, counts.size());
        assertEquals(2, counts.get(Position.PROGRAMISTA));
        assertEquals(1, counts.get(Position.MANAGER));
    }

    // --- Testy: Analityka i Raportowanie ---

    @Test
    @DisplayName("Powinien obliczyć poprawną średnią pensję dla wszystkich pracowników")
    void averageSalary_ShouldReturnCorrectAverage() {
        // Arrange
        service.addEmployee(e1_anna_techcorp); // 9000
        service.addEmployee(e2_piotr_devhouse); // 12500
        service.addEmployee(e3_jan_techcorp);  // 8500

        // Calculation: (9000 + 12500 + 8500) / 3 = 10000
        double expectedAverage = 10000.0;

        // Act
        double actualAverage = service.averageSalary();

        // Assert
        assertEquals(expectedAverage, actualAverage, 0.001, "Błąd obliczeń średniej arytmetycznej.");
    }

    @Test
    @DisplayName("Powinien zidentyfikować pracownika z najwyższym wynagrodzeniem")
    void findHighestSalary_ShouldReturnCorrectEmployee() {
        // Arrange
        service.addEmployee(e1_anna_techcorp); // 9000
        service.addEmployee(e2_piotr_devhouse); // 12500 (Max)
        service.addEmployee(e3_jan_techcorp);  // 8500

        // Act
        Employee highest = service.findHighestSalary();

        // Assert
        assertNotNull(highest);
        assertEquals(e2_piotr_devhouse, highest, "Zidentyfikowano niewłaściwego pracownika.");
    }

    @Test
    @DisplayName("Powinien wykryć niespójności w wynagrodzeniach (poniżej widełek)")
    void validateSalaryConsistency_ShouldFindInconsistentEmployees() {
        // Arrange
        service.addEmployee(e1_anna_techcorp); // 9000 > 8000 (OK)
        service.addEmployee(e4_ewa_underpaid);  // 2500 < 3000 (Violation)

        // Act
        List<Employee> inconsistent = service.validateSalaryConsistency();

        // Assert
        assertEquals(1, inconsistent.size(), "Powinien zostać zwrócony 1 pracownik niespełniający wymogów.");
        assertEquals(e4_ewa_underpaid, inconsistent.get(0));
    }

    @Test
    @DisplayName("Powinien wygenerować poprawne statystyki (CompanyStatistics) dla każdej firmy")
    void getCompanyStatistics_ShouldReturnCorrectStats() {
        // Arrange
        service.addEmployee(e1_anna_techcorp); // TechCorp
        service.addEmployee(e2_piotr_devhouse); // DevHouse
        service.addEmployee(e3_jan_techcorp);  // TechCorp

        // Act
        Map<String, CompanyStatistics> statsMap = service.getCompanyStatistics();

        // Assert
        assertEquals(2, statsMap.size());

        // Weryfikacja TechCorp
        CompanyStatistics techStats = statsMap.get("TechCorp");
        assertNotNull(techStats);
        assertEquals(2, techStats.getEmployeeCount());
        assertEquals(8750.0, techStats.getAverageSalary(), 0.001); // (9000+8500)/2
        assertEquals("Anna Nowak", techStats.getHighestEarnerName());

        // Weryfikacja DevHouse
        CompanyStatistics devStats = statsMap.get("DevHouse");
        assertNotNull(devStats);
        assertEquals(1, devStats.getEmployeeCount());
        assertEquals(12500.0, devStats.getAverageSalary(), 0.001);
    }

    // --- Testy: Przypadki Brzegowe (Edge Cases) ---

    @Test
    @DisplayName("Metody analityczne nie powinny rzucać wyjątków dla pustego serwisu")
    void testOperationsOnEmptyService_ShouldNotThrowErrors() {
        // Arrange - Empty Service

        // Act & Assert
        assertDoesNotThrow(() -> service.findByCompany("TechCorp"));
        assertDoesNotThrow(() -> service.sortByName());
        assertDoesNotThrow(() -> service.groupByPosition());
        assertDoesNotThrow(() -> service.countByPosition());
        assertDoesNotThrow(() -> service.validateSalaryConsistency());
        assertDoesNotThrow(() -> service.getCompanyStatistics());

        // Weryfikacja wartości domyślnych dla pustego stanu
        assertTrue(service.findByCompany("TechCorp").isEmpty());
        assertEquals(0.0, service.averageSalary(), "Dla pustej listy średnia powinna wynosić 0.0");
        assertNull(service.findHighestSalary(), "Dla pustej listy wynik powinien być null");
    }
}