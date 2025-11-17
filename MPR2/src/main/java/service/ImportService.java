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

// Ten serwis będzie odpowiedzialny TYLKO za importowanie.
public class ImportService {

    // Serwis importujący musi wiedzieć, gdzie dodawać pracowników,
    // więc przekazujemy mu referencję do 'EmployeeService'.
    private EmployeeService employeeService;

    public ImportService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Wczytuje plik CSV, parsuje go i dodaje pracowników do EmployeeService.
     * Zgodnie z zadaniem, używamy BufferedReader i split(), zamiast OpenCSV.
     */
    public ImportSummary importFromCsv(String filePath) {
        int importedCount = 0;
        List<String> errors = new ArrayList<>();
        int lineNumber = 0; // Zaczynamy liczenie linii od 0

        // Używamy "try-with-resources", jak na wykładzie.
        // BufferedReader otworzy się i ZAMKNIE AUTOMATYCZNIE,
        // nawet jeśli poleci błąd. Super sprawa.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line = reader.readLine(); // Wczytujemy pierwszą linię
            lineNumber++; // Jesteśmy na linii 1

            // Sprawdzamy, czy to nagłówek (i czy plik nie jest pusty)
            if (line == null || line.isEmpty()) {
                errors.add("Plik jest pusty.");
                return new ImportSummary(0, errors);
            }

            // Mamy nagłówek, więc go pominęliśmy. Czytamy resztę w pętli.
            while ((line = reader.readLine()) != null) {
                lineNumber++; // Przechodzimy do kolejnej linii

                // Pomijamy puste linie (np. ktoś wcisnął Enter na końcu pliku)
                if (line.trim().isEmpty()) {
                    continue; // 'continue' przeskakuje do następnej iteracji pętli
                }

                // Tutaj parsujemy JEDNĄ linię.
                // Opakowujemy to w try/catch, żeby błąd w jednej linii
                // nie zatrzymał nam całego importu.
                try {
                    // Dzielimy linię po przecinku
                    String[] data = line.split(",");

                    // Walidacja: czy mamy 6 kolumn?
                    if (data.length != 6) {
                        // Rzucamy własny wyjątek, który złapiemy poniżej
                        throw new InvalidDataException("Niepoprawna liczba kolumn (oczekiwano 6, jest " + data.length + ")");
                    }

                    // Wyciągamy dane i 'trim()'ujemy (usuwamy białe znaki z początku/końca)
                    String firstName = data[0].trim();
                    String lastName = data[1].trim();
                    String email = data[2].trim();
                    String company = data[3].trim();
                    String positionStr = data[4].trim().toUpperCase(); // Od razu do dużych liter
                    String salaryStr = data[5].trim();

                    // Walidacja 1: Stanowisko (Position)
                    Position position;
                    try {
                        // Position.valueOf() rzuci błędem, jeśli nie znajdzie
                        // stanowiska w enumie. To nam pasuje.
                        position = Position.valueOf(positionStr);
                    } catch (IllegalArgumentException e) {
                        throw new InvalidDataException("Nieznane stanowisko: '" + data[4].trim() + "'");
                    }

                    // Walidacja 2: Pensja (Salary)
                    double salary;
                    try {
                        salary = Double.parseDouble(salaryStr);
                    } catch (NumberFormatException e) {
                        throw new InvalidDataException("Niepoprawny format pensji: '" + salaryStr + "'");
                    }

                    if (salary <= 0) {
                        throw new InvalidDataException("Pensja musi być dodatnia (jest " + salary + ")");
                    }

                    // Jeśli wszystko poszło OK, tworzymy pracownika
                    String fullName = firstName + " " + lastName;
                    Employee employee = new Employee(fullName, email, company, position, salary);

                    // I próbujemy go dodać do serwisu
                    if (employeeService.addEmployee(employee)) {
                        importedCount++; // Udało się!
                    } else {
                        // Serwis zwrócił 'false', co oznacza, że email już był
                        errors.add("Linia " + lineNumber + ": Pracownik z emailem '" + email + "' już istnieje.");
                    }

                } catch (InvalidDataException | IllegalArgumentException e) {
                    // Łapiemy błędy walidacji dla tej JEDNEJ linii
                    errors.add("Linia " + lineNumber + ": Błąd danych -> " + e.getMessage());
                }
                // ... i pętla leci dalej, do następnej linii!
            }

        } catch (IOException e) {
            // Ten catch jest dla 'BufferedReader'
            // Jakikolwiek błąd odczytu pliku (np. nie ma pliku)
            errors.add("Krytyczny błąd odczytu pliku: " + e.getMessage());
        }

        // Zwracamy nasz "raport" z importu
        return new ImportSummary(importedCount, errors);
    }
}