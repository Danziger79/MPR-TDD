package service;

import model.Employee;
import model.ImportSummary;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

// Klasa testowa weryfikująca poprawność importu danych z plików CSV.
// Testy tworzą tymczasowe pliki w izolowanym środowisku (@TempDir),
// aby sprawdzić różne scenariusze: sukces, błędy walidacji, błędy formatu.
class ImportServiceTest {

    private ImportService importService;
    private EmployeeService employeeService;

    // JUnit 5 automatycznie tworzy i czyści ten katalog tymczasowy dla każdego testu.
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Używamy prawdziwego EmployeeService, aby sprawdzić integrację
        employeeService = new EmployeeService();
        importService = new ImportService(employeeService);
    }

    /**
     * Pomocnicza metoda do tworzenia plików CSV w katalogu tymczasowym.
     */
    private String createCsvFile(String fileName, String content) throws IOException {
        File file = tempDir.resolve(fileName).toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file.getAbsolutePath();
    }

    @Test
    @DisplayName("Powinien poprawnie zaimportować poprawny plik CSV")
    void shouldImportValidCsvFile() throws IOException {
        // Arrange
        String csvContent = "Imie,Nazwisko,Email,Firma,Stanowisko,Pensja\n" + // Nagłówek
                "Jan,Kowalski,jan@test.pl,TechCorp,PROGRAMISTA,9000\n" +
                "Anna,Nowak,anna@test.pl,DevHouse,MANAGER,15000";

        String filePath = createCsvFile("valid.csv", csvContent);

        // Act
        ImportSummary summary = importService.importFromCsv(filePath);

        // Assert
        assertThat(summary.getImportedCount())
                .as("Powinno zaimportować 2 pracowników")
                .isEqualTo(2);

        assertThat(summary.getErrors())
                .as("Lista błędów powinna być pusta")
                .isEmpty();

        // Weryfikacja czy dane trafiły do serwisu
        assertThat(employeeService.findByCompany("TechCorp")).hasSize(1);
        assertThat(employeeService.findByCompany("DevHouse")).hasSize(1);
    }

    @Test
    @DisplayName("Powinien pominąć błędne wiersze i zaraportować błędy")
    void shouldSkipInvalidRowsAndReportErrors() throws IOException {
        // Arrange
        // 1. Poprawny
        // 2. Zła liczba kolumn (brak pensji)
        // 3. Nieznane stanowisko (Ninja)
        // 4. Ujemna pensja
        // 5. Zły format pensji (text)
        String csvContent = "Header\n" +
                "Jan,Ok,jan@ok.pl,Firma,STAZYSTA,3000\n" +
                "Jan,ZleKolumny,jan@zle.pl,Firma,STAZYSTA\n" +
                "Jan,ZleStanowisko,jan@ninja.pl,Firma,NINJA,5000\n" +
                "Jan,UjemnaPensja,jan@ujemna.pl,Firma,PROGRAMISTA,-100\n" +
                "Jan,ZlyFormat,jan@format.pl,Firma,MANAGER,tysiac";

        String filePath = createCsvFile("mixed.csv", csvContent);

        // Act
        ImportSummary summary = importService.importFromCsv(filePath);

        // Assert
        assertThat(summary.getImportedCount())
                .as("Tylko 1 rekord był poprawny")
                .isEqualTo(1);

        assertThat(summary.getErrors())
                .as("Powinny być 4 błędy walidacji")
                .hasSize(4)
                .anySatisfy(error -> assertThat(error).contains("Niepoprawna liczba kolumn"))
                .anySatisfy(error -> assertThat(error).contains("Nieznane stanowisko"))
                .anySatisfy(error -> assertThat(error).contains("Pensja musi być dodatnia"))
                .anySatisfy(error -> assertThat(error).contains("Niepoprawny format pensji"));
    }

    @Test
    @DisplayName("Nie powinien zaimportować duplikatów (ten sam email w serwisie)")
    void shouldNotImportDuplicates() throws IOException {
        // Arrange
        // Najpierw dodajemy pracownika ręcznie do serwisu
        employeeService.addEmployee(new Employee("Istniejacy", "duplikat@test.pl", "X", Position.STAZYSTA, 3000));

        String csvContent = "H\n" +
                "Nowy,Pracownik,nowy@test.pl,X,STAZYSTA,3000\n" +
                "Ten,Sam,duplikat@test.pl,X,MANAGER,10000"; // Ten email już jest

        String filePath = createCsvFile("duplicates.csv", csvContent);

        // Act
        ImportSummary summary = importService.importFromCsv(filePath);

        // Assert
        assertThat(summary.getImportedCount())
                .as("Powinien zaimportować tylko nowego pracownika")
                .isEqualTo(1);

        assertThat(summary.getErrors())
                .as("Powinien zgłosić błąd duplikatu")
                .hasSize(1)
                .first().asString().contains("już istnieje");
    }

    @Test
    @DisplayName("Powinien obsłużyć pusty plik")
    void shouldHandleEmptyFile() throws IOException {
        // Arrange
        String filePath = createCsvFile("empty.csv", "");

        // Act
        ImportSummary summary = importService.importFromCsv(filePath);

        // Assert
        assertThat(summary.getImportedCount()).isZero();
        assertThat(summary.getErrors())
                .hasSize(1)
                .first().asString().contains("Plik jest pusty");
    }

    @Test
    @DisplayName("Powinien zgłosić błąd dla nieistniejącego pliku")
    void shouldReportErrorForNonExistentFile() {
        // Arrange
        String nonExistentPath = "sciezka/ktorej/nie/ma.csv";

        // Act
        ImportSummary summary = importService.importFromCsv(nonExistentPath);

        // Assert
        assertThat(summary.getImportedCount()).isZero();
        assertThat(summary.getErrors())
                .hasSize(1)
                .first().asString().contains("Krytyczny błąd odczytu pliku");
    }
}