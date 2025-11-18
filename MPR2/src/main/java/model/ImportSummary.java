package model;

import java.util.List;

// Klasa typu DTO (Data Transfer Object) agregująca wyniki procesu importu danych (np. z pliku CSV).
// Przechowuje statystyki dotyczące liczby poprawnie przetworzonych rekordów oraz listę szczegółów napotkanych błędów.
public class ImportSummary {
    private int importedCount;
    private List<String> errors;

    // Konstruktor inicjalizujący obiekt raportu.
    // Przyjmuje licznik sukcesów oraz listę komunikatów błędów dla wierszy, których nie udało się zaimportować.
    public ImportSummary(int importedCount, List<String> errors) {
        this.importedCount = importedCount;
        this.errors = errors;
    }

    public int getImportedCount() {
        return importedCount;
    }

    // Zwraca listę opisów błędów. Każdy ciąg znaków w liście odpowiada konkretnemu problemowi walidacji napotkanemu podczas importu.
    public List<String> getErrors() {
        return errors;
    }

    // Przesłonięcie metody toString w celu ułatwienia logowania i debugowania procesu.
    // Zwraca skrócone podsumowanie operacji (liczba sukcesów vs całkowita liczba błędów), zamiast wypisywać pełną treść błędów.
    @Override
    public String toString() {
        return "ImportSummary{" +
                "zaimportowano=" + importedCount +
                ", błędy=" + errors.size() +
                '}';
    }
}