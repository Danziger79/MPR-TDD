package service;

import interfaces.FileSystemAdapter;

// Rola: Serwis odpowiedzialny za operacje eksportu danych do zewnętrznego systemu plików.
// Opis: Klasa pełni funkcję logiki biznesowej, która formatuje dane i deleguje ich fizyczny zapis
// do adaptera (FileSystemAdapter). Jest to główny obiekt testowany (SUT) w scenariuszach
// wykorzystujących wzorzec Mock, służący do weryfikacji behawioralnej (czy serwis poprawnie "rozmawia" z systemem plików).
public class ExportService {

    // Abstrakcja systemu plików (zależność).
    // Użycie interfejsu zamiast konkretnej implementacji umożliwia wstrzyknięcie obiektu Mock w testach.
    private final FileSystemAdapter fileSystem;

    public ExportService(FileSystemAdapter fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Przetwarza dane i zleca ich zapis w określonej lokalizacji.
     *
     * @param filename Nazwa pliku docelowego.
     * @param data Surowe dane tekstowe do zapisania.
     */
    public void exportData(String filename, String data) {
        // Logika biznesowa: Formatowanie danych (dodanie nagłówka/prefiksu "CSV:").
        String formattedData = "CSV:" + data;

        // Interakcja z systemem zewnętrznym (Side Effect).
        // W teście z użyciem Mocka weryfikujemy, czy ta metoda została wywołana
        // dokładnie z taką ścieżką ("/export/" + filename) i tak sformatowaną treścią.
        fileSystem.write("/export/" + filename, formattedData);
    }
}