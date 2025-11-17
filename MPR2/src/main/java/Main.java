import exception.ApiException;
import model.Employee;
import model.ImportSummary;
import service.ApiService;
import service.EmployeeService;
import service.ImportService;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // 1. Tworzymy główny serwis (teraz nazywa się EmployeeService)
        EmployeeService service = new EmployeeService();

        // 2. Tworzymy serwisy do importu, przekazując im ten główny serwis
        ImportService importService = new ImportService(service);
        ApiService apiService = new ApiService();

        // --- TESTUJEMY IMPORT Z CSV ---
        System.out.println("=== 1. Rozpoczynam import z CSV ===");

        // UWAGA: Stwórz plik 'employees.csv' w głównym katalogu projektu!
        ImportSummary summary = importService.importFromCsv("employees.csv");

        System.out.println("Import zakończony. Podsumowanie:");
        System.out.println(" > Zaimportowano pomyślnie: " + summary.getImportedCount() + " pracowników.");
        if (!summary.getErrors().isEmpty()) {
            System.out.println(" > Wystąpiły błędy (" + summary.getErrors().size() + "):");
            for (String error : summary.getErrors()) {
                System.out.println("   - " + error);
            }
        }
        System.out.println("\n--- Stan bazy po imporcie CSV ---");
        service.showAllEmployees();

        // --- TESTUJEMY IMPORT Z API ---
        System.out.println("\n=== 2. Pobieranie danych z REST API ===");
        try {
            List<Employee> apiEmployees = apiService.fetchEmployeesFromApi();
            System.out.println("Pobrano " + apiEmployees.size() + " pracowników z API.");

            // Dodajemy pobranych pracowników do naszego serwisu
            int addedFromApi = 0;
            for (Employee emp : apiEmployees) {
                if (service.addEmployee(emp)) {
                    addedFromApi++;
                }
            }
            System.out.println(addedFromApi + " nowych pracowników zostało dodanych do bazy.");

        } catch (ApiException e) {
            System.err.println("NIE UDAŁO SIĘ POBRAĆ DANYCH Z API: " + e.getMessage());
            e.printStackTrace(); // Pokazuje nam cały "ślad" błędu
        }

        System.out.println("\n--- Stan bazy po imporcie z API ---");
        service.showAllEmployees();

        // --- TESTUJEMY NOWE METODY ANALITYCZNE ---

        // Test 1: Niespójne pensje
        System.out.println("\n=== 3. Analiza: Niespójne pensje (poniżej bazy) ===");
        List<Employee> inconsistent = service.validateSalaryConsistency();
        if (inconsistent.isEmpty()) {
            System.out.println("Wszyscy pracownicy zarabiają powyżej bazy dla swojego stanowiska. Jest OK!");
        } else {
            System.out.println("Znaleziono pracowników z pensją poniżej bazy:");
            for (Employee e : inconsistent) {
                System.out.println("  -> " + e.getName() + " (Stawka: " + e.getSalary() + ", Baza: " + e.getPosition().getBaseSalary() + ")");
            }
        }

        // Test 2: Statystyki firm
        System.out.println("\n=== 4. Analiza: Statystyki firm ===");
        var companyStats = service.getCompanyStatistics(); // 'var' to skrót od Map<String, CompanyStatistics>

        if (companyStats.isEmpty()) {
            System.out.println("Brak danych do wygenerowania statystyk.");
        } else {
            // Iterujemy po mapie statystyk i ładnie je drukujemy
            for (Map.Entry<String, model.CompanyStatistics> entry : companyStats.entrySet()) {
                System.out.println("\nFirma: " + entry.getKey());
                System.out.println("  " + entry.getValue()); // Tu zadziała nasz .toString() z CompanyStatistics
            }
        }
    }
}