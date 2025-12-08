package service;

import interfaces.FileSystemAdapter;
import java.io.IOException;
import java.util.List;

public class ExportService {

    private final FileSystemAdapter fileSystemAdapter;

    // Dodajemy to pole, aby móc użyć na nim @Spy w teście
    private final CsvFormatter csvFormatter;

    public ExportService(FileSystemAdapter fileSystemAdapter, CsvFormatter csvFormatter) {
        this.fileSystemAdapter = fileSystemAdapter;
        this.csvFormatter = csvFormatter;
    }

    /**
     * Eksportuje listę danych do pliku CSV.
     * Obsługuje wyjątki IO poprzez zapisanie logu błędu.
     */
    public void exportData(String filename, List<String> data) {
        try {
            // 1. Używamy obiektu, który będzie szpiegowany (@Spy)
            String content = csvFormatter.format(data);

            // 2. Próbujemy zapisać (tu w teście użyjemy doThrow, żeby wywołać błąd)
            fileSystemAdapter.write(filename, content);

        } catch (IOException e) {
            System.err.println("Błąd zapisu! Próbuję zapisać log błędu...");

            // 3. Logika Fallback - to zweryfikujemy, gdy poleci wyjątek
            try {
                fileSystemAdapter.write("error.log", "Failed to write: " + filename);
            } catch (Exception ignored) {
                // Ignorujemy błędy logowania błędu
            }
        }
    }

    // Klasa pomocnicza (normalnie byłaby w osobnym pliku, ale dla uproszczenia jest tu).
    // To na niej użyjemy @Spy w teście.
    public static class CsvFormatter {
        public String format(List<String> data) {
            // Proste łączenie przecinkami
            return String.join(",", data);
        }
    }
}