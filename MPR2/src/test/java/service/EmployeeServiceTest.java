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

class EmployeeServiceTest {

    // Obiekt, który będziemy testować (System Under Test - SUT)
    private EmployeeService service;

    // Przykładowe dane testowe
    private Employee e1_anna_techcorp_prog;
    private Employee e2_piotr_devhouse_mgr;
    private Employee e3_jan_techcorp_prog;
    private Employee e4_ewa_techcorp_staz;

    // Adnotacja @BeforeEach (jak na wykładzie)
    // Ta metoda odpali się PRZED KAŻDYM testem (@Test)
    // Gwarantuje to, że każdy test zaczyna z "czystą" listą
    // i świeżym serwisem. Testy są od siebie niezależne.
    @BeforeEach
    void setUp() {
        // Arrange (wspólne dla wszystkich testów)
        service = new EmployeeService(); // Nowiutka instancja serwisu

        // Nowiutkie obiekty pracowników
        // (daję opisowe nazwy zmiennych, żeby było łatwiej)
        e1_anna_techcorp_prog = new Employee("Anna Nowak", "anna@techcorp.pl", "TechCorp", Position.PROGRAMISTA, 9000);
        e2_piotr_devhouse_mgr = new Employee("Piotr Wisniewski", "piotr@devhouse.pl", "DevHouse", Position.MANAGER, 12500);
        e3_jan_techcorp_prog = new Employee("Jan Kowalski", "jan@techcorp.pl", "TechCorp", Position.PROGRAMISTA, 8500);

        // Pracownik z pensją poniżej bazy (baza STAZYSTA to 3000)
        e4_ewa_techcorp_staz = new Employee("Ewa Zielinska", "ewa@techcorp.pl", "TechCorp", Position.STAZYSTA, 2500);
    }

    // --- Testy dodawania ---

    @Test
    @DisplayName("Dodanie nowego pracownika powinno zwrócić true")
    void addEmployee_NewEmployee_ShouldReturnTrue() {
        // Act
        boolean result = service.addEmployee(e1_anna_techcorp_prog);

        // Assert
        assertTrue(result, "Metoda powinna zwrócić true dla nowego pracownika");
    }

    @Test
    @DisplayName("Próba dodania pracownika z tym samym emailem powinna zwrócić false")
    void addEmployee_DuplicateEmail_ShouldReturnFalse() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog); // Dodajemy Annę
        // Tworzymy "duplikat" z tym samym mailem, ale innymi danymi
        Employee duplicate = new Employee("Inna Osoba", "anna@techcorp.pl", "InnaFirma", Position.PREZES, 99999);

        // Act
        boolean result = service.addEmployee(duplicate);

        // Assert
        assertFalse(result, "Metoda powinna zwrócić false dla duplikatu emaila");
    }

    // --- Testy wyszukiwania i filtrowania ---

    @Test
    @DisplayName("Wyszukiwanie po firmie powinno zwrócić poprawną listę")
    void findByCompany_ShouldReturnCorrectEmployees() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog);
        service.addEmployee(e2_piotr_devhouse_mgr);
        service.addEmployee(e3_jan_techcorp_prog);

        // Act
        List<Employee> techCorpList = service.findByCompany("TechCorp");
        List<Employee> devHouseList = service.findByCompany("DevHouse");

        // Assert
        assertEquals(2, techCorpList.size(), "TechCorp powinien mieć 2 pracowników");
        assertTrue(techCorpList.contains(e1_anna_techcorp_prog));
        assertTrue(techCorpList.contains(e3_jan_techcorp_prog));

        assertEquals(1, devHouseList.size(), "DevHouse powinien mieć 1 pracownika");
        assertTrue(devHouseList.contains(e2_piotr_devhouse_mgr));
    }

    @Test
    @DisplayName("Wyszukiwanie po firmie (case-insensitive) powinno działać")
    void findByCompany_CaseInsensitive_ShouldReturnEmployees() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog);

        // Act
        List<Employee> result = service.findByCompany("techcorp"); // małą literą

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Wyszukiwanie nieistniejącej firmy powinno zwrócić pustą listę")
    void findByCompany_NonExistentCompany_ShouldReturnEmptyList() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog);

        // Act
        List<Employee> result = service.findByCompany("FirmaKtorejNieMa");

        // Assert
        assertNotNull(result, "Lista nie powinna być nullem");
        assertTrue(result.isEmpty(), "Lista pracowników powinna być pusta");
    }

    // --- Testy sortowania i grupowania ---

    @Test
    @DisplayName("Sortowanie po imieniu powinno zwrócić listę w kolejności alfabetycznej")
    void sortByName_ShouldReturnSortedList() {
        // Arrange
        service.addEmployee(e2_piotr_devhouse_mgr); // Piotr
        service.addEmployee(e1_anna_techcorp_prog); // Anna
        service.addEmployee(e3_jan_techcorp_prog);  // Jan

        // Act
        List<Employee> sorted = service.sortByName();

        // Assert
        // Oczekiwana kolejność: Anna, Jan, Piotr
        assertEquals(3, sorted.size());
        assertEquals(e1_anna_techcorp_prog, sorted.get(0), "Pierwsza powinna być Anna");
        assertEquals(e3_jan_techcorp_prog, sorted.get(1), "Drugi powinien być Jan");
        assertEquals(e2_piotr_devhouse_mgr, sorted.get(2), "Trzeci powinien być Piotr");
    }

    @Test
    @DisplayName("Grupowanie po stanowisku powinno działać poprawnie")
    void groupByPosition_ShouldGroupCorrectly() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog); // PROGRAMISTA
        service.addEmployee(e2_piotr_devhouse_mgr); // MANAGER
        service.addEmployee(e3_jan_techcorp_prog);  // PROGRAMISTA

        // Act
        Map<Position, List<Employee>> grouped = service.groupByPosition();

        // Assert
        assertEquals(2, grouped.size(), "Powinny być 2 grupy stanowisk");
        assertTrue(grouped.containsKey(Position.PROGRAMISTA));
        assertTrue(grouped.containsKey(Position.MANAGER));

        assertEquals(2, grouped.get(Position.PROGRAMISTA).size(), "Powinno być 2 programistów");
        assertEquals(1, grouped.get(Position.MANAGER).size(), "Powinien być 1 manager");
    }

    @Test
    @DisplayName("Zliczanie po stanowisku powinno zwrócić poprawne liczby")
    void countByPosition_ShouldCountCorrectly() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog); // PROGRAMISTA
        service.addEmployee(e2_piotr_devhouse_mgr); // MANAGER
        service.addEmployee(e3_jan_techcorp_prog);  // PROGRAMISTA

        // Act
        Map<Position, Integer> counts = service.countByPosition();

        // Assert
        assertEquals(2, counts.size());
        assertEquals(2, counts.get(Position.PROGRAMISTA), "Oczekiwano 2 programistów");
        assertEquals(1, counts.get(Position.MANAGER), "Oczekiwano 1 managera");
    }

    // --- Testy statystyk i analiz ---

    @Test
    @DisplayName("Średnia pensja powinna być obliczona poprawnie")
    void averageSalary_ShouldReturnCorrectAverage() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog); // 9000
        service.addEmployee(e2_piotr_devhouse_mgr); // 12500
        service.addEmployee(e3_jan_techcorp_prog);  // 8500

        // (9000 + 12500 + 8500) / 3 = 30000 / 3 = 10000
        double expectedAverage = 10000.0;

        // Act
        double actualAverage = service.averageSalary();

        // Assert
        // Używamy "delty" (trzeci argument) do porównywania liczb double
        assertEquals(expectedAverage, actualAverage, 0.001, "Średnia pensja jest niepoprawna");
    }

    @Test
    @DisplayName("Pracownik z najwyższą pensją powinien być znaleziony")
    void findHighestSalary_ShouldReturnCorrectEmployee() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog); // 9000
        service.addEmployee(e2_piotr_devhouse_mgr); // 12500 (najwyższa)
        service.addEmployee(e3_jan_techcorp_prog);  // 8500

        // Act
        Employee highest = service.findHighestSalary();

        // Assert
        assertNotNull(highest);
        // Porównujemy obiekty. Dzięki temu, że mamy dobrą metodę equals(),
        // to zadziała, ale bezpieczniej jest porównać po ID (emailu)
        // albo po prostu sprawdzić, czy to ta sama instancja.
        assertEquals(e2_piotr_devhouse_mgr, highest, "Znaleziono niepoprawnego pracownika");
    }

    @Test
    @DisplayName("Walidacja pensji powinna znaleźć pracowników poniżej bazy")
    void validateSalaryConsistency_ShouldFindInconsistentEmployees() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog); // 9000 > 8000 (OK)
        service.addEmployee(e4_ewa_techcorp_staz);  // 2500 < 3000 (BŁĄD)

        // Act
        List<Employee> inconsistent = service.validateSalaryConsistency();

        // Assert
        assertEquals(1, inconsistent.size(), "Powinien być 1 niespójny pracownik");
        assertEquals(e4_ewa_techcorp_staz, inconsistent.get(0));
    }

    @Test
    @DisplayName("Statystyki firmy powinny być obliczone poprawnie")
    void getCompanyStatistics_ShouldReturnCorrectStats() {
        // Arrange
        service.addEmployee(e1_anna_techcorp_prog); // TechCorp, 9000
        service.addEmployee(e2_piotr_devhouse_mgr); // DevHouse, 12500
        service.addEmployee(e3_jan_techcorp_prog);  // TechCorp, 8500

        // Act
        Map<String, CompanyStatistics> statsMap = service.getCompanyStatistics();

        // Assert
        assertEquals(2, statsMap.size(), "Powinny być statystyki dla 2 firm");

        // Sprawdzamy TechCorp
        CompanyStatistics techStats = statsMap.get("TechCorp");
        assertNotNull(techStats);
        assertEquals(2, techStats.getEmployeeCount());
        assertEquals((9000.0 + 8500.0) / 2.0, techStats.getAverageSalary(), 0.001);
        assertEquals("Anna Nowak", techStats.getHighestEarnerName(), "Anna zarabia więcej w TechCorp");

        // Sprawdzamy DevHouse
        CompanyStatistics devStats = statsMap.get("DevHouse");
        assertNotNull(devStats);
        assertEquals(1, devStats.getEmployeeCount());
        assertEquals(12500.0, devStats.getAverageSalary(), 0.001);
        assertEquals("Piotr Wisniewski", devStats.getHighestEarnerName());
    }

    // --- Testy Przypadków Brzegowych (Edge Cases) ---

    @Test
    @DisplayName("Operacje na pustym serwisie nie powinny rzucać błędów")
    void testOperationsOnEmptyService_ShouldNotThrowErrors() {
        // Arrange
        // Serwis jest pusty (dzięki @BeforeEach)

        // Act & Assert
        // Używamy assertDoesNotThrow do sprawdzenia, czy kod się nie "wysypie"
        assertDoesNotThrow(() -> service.findByCompany("TechCorp"), "findByCompany na pustym");
        assertDoesNotThrow(() -> service.sortByName(), "sortByName na pustym");
        assertDoesNotThrow(() -> service.groupByPosition(), "groupByPosition na pustym");
        assertDoesNotThrow(() -> service.countByPosition(), "countByPosition na pustym");
        assertDoesNotThrow(() -> service.validateSalaryConsistency(), "validateSalaryConsistency na pustym");
        assertDoesNotThrow(() -> service.getCompanyStatistics(), "getCompanyStatistics na pustym");

        // Sprawdzamy, czy zwracają poprawne "puste" wartości
        assertTrue(service.findByCompany("TechCorp").isEmpty(), "findByCompany powinno zwrócić pustą listę");
        assertEquals(0.0, service.averageSalary(), "averageSalary powinno zwrócić 0.0");
        assertNull(service.findHighestSalary(), "findHighestSalary powinno zwrócić null");
    }
}