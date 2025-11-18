package service;

import exception.InvalidDataException;
import model.Employee;
import model.ImportSummary;
import model.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Serwis odpowiedzialny za proces masowego importu danych pracowników z plików zewnętrznych (CSV).
// Klasa pełni rolę mediatora między surowymi danymi plikowymi a logiką biznesową serwisu EmployeeService.
public class ImportService {

    // Referencja do głównego serwisu pracowniczego, umożliwiająca trwały zapis zwalidowanych danych.
    private EmployeeService employeeService;

    public ImportService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Przeprowadza proces importu danych z pliku CSV.
     * Metoda otwiera plik, pomija nagłówek, a następnie iteruje po wierszach,
     * dokonując parsowania, walidacji i konwersji każdego rekordu na obiekt Employee.
     *
     * @param filePath Ścieżka systemowa do pliku CSV.
     * @return Obiekt ImportSummary zawierający statystyki sukcesów oraz listę błędów walidacji.
     */
    public ImportSummary importFromCsv(String filePath) {
        int importedCount = 0;
        List<String> errors = new ArrayList<>();
        int lineNumber = 0;

        // Wykorzystanie konstrukcji try-with-resources zapewnia automatyczne zamknięcie
        // strumienia wejściowego (BufferedReader) niezależnie od wyniku operacji (sukces lub wyjątek).
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line = reader.readLine();
            lineNumber++;

            // Weryfikacja nagłówka pliku. Jeśli plik jest pusty lub pierwsza linia nie istnieje,
            // proces importu zostaje przerwany, a błąd odnotowany.
            if (line == null || line.isEmpty()) {
                errors.add("Plik jest pusty.");
                return new ImportSummary(0, errors);
            }

            // Pętla przetwarzająca właściwe dane (payload).
            // Każdy obrót pętli odpowiada jednej linii w pliku CSV.
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Ignorowanie pustych linii (np. białych znaków na końcu pliku),
                // aby uniknąć niepotrzebnych błędów parsowania.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Blok try-catch zagnieżdżony wewnątrz pętli pozwala na izolację błędów.
                // Błąd parsowania lub walidacji w jednym wierszu nie przerywa całego procesu importu,
                // lecz jest rejestrowany w liście błędów, a pętla przechodzi do kolejnego rekordu.
                try {
                    // Podział linii na tokeny przy użyciu separatora przecinkowego.
                    String[] data = line.split(",");

                    // Wstępna walidacja struktury rekordu (oczekiwana liczba kolumn: 6).
                    if (data.length != 6) {
                        throw new InvalidDataException("Niepoprawna liczba kolumn (oczekiwano 6, jest " + data.length + ")");
                    }

                    // Normalizacja danych wejściowych (usunięcie białych znaków).
                    String firstName = data[0].trim();
                    String lastName = data[1].trim();
                    String email = data[2].trim();
                    String company = data[3].trim();
                    String positionStr = data[4].trim().toUpperCase();
                    String salaryStr = data[5].trim();

                    // Walidacja i konwersja stanowiska (Position).
                    // W przypadku podania wartości spoza enum Position, zostanie rzucony wyjątek.
                    Position position;
                    try {
                        position = Position.valueOf(positionStr);
                    } catch (IllegalArgumentException e) {
                        throw new InvalidDataException("Nieznane stanowisko: '" + data[4].trim() + "'");
                    }

                    // Parsowanie wartości numerycznej wynagrodzenia wraz z walidacją logiczną.
                    double salary;
                    try {
                        salary = Double.parseDouble(salaryStr);
                    } catch (NumberFormatException e) {
                        throw new InvalidDataException("Niepoprawny format pensji: '" + salaryStr + "'");
                    }

                    if (salary <= 0) {
                        throw new InvalidDataException("Pensja musi być dodatnia (jest " + salary + ")");
                    }

                    // Utworzenie obiektu domenowego po pomyślnej walidacji wszystkich pól.
                    String fullName = firstName + " " + lastName;
                    Employee employee = new Employee(fullName, email, company, position, salary);

                    // Próba zapisu pracownika w serwisie głównym.
                    // Metoda addEmployee zwraca false, jeśli pracownik o danym emailu już istnieje.
                    if (employeeService.addEmployee(employee)) {
                        importedCount++;
                    } else {
                        errors.add("Linia " + lineNumber + ": Pracownik z emailem '" + email + "' już istnieje.");
                    }

                } catch (InvalidDataException | IllegalArgumentException e) {
                    // Obsługa wyjątków walidacji biznesowej oraz błędów formatowania danych dla pojedynczego rekordu.
                    errors.add("Linia " + lineNumber + ": Błąd danych -> " + e.getMessage());
                }
            }

        } catch (IOException e) {
            // Obsługa krytycznych błędów wejścia-wyjścia (IO), np. brak pliku lub brak uprawnień odczytu.
            // Tego typu błąd uniemożliwia dalsze przetwarzanie i przerywa import.
            errors.add("Krytyczny błąd odczytu pliku: " + e.getMessage());
        }

        // Zwrócenie obiektu podsumowującego operację importu.
        return new ImportSummary(importedCount, errors);
    }
}