package doubles;

import interfaces.FileSystemAdapter;

// Rola: Implementacja wzorca Test Double typu "Mock".
// Opis: Obiekt służący do weryfikacji behawioralnej (Behavior Verification).
// Zamiast sprawdzać stan systemu po wykonaniu operacji, weryfikuje, czy testowany kod
// wszedł w interakcję z tą zależnością w ściśle określony sposób (z oczekiwanymi parametrami).
// Jest kluczowy przy testowaniu metod typu "void" (Side Effects), takich jak zapis do pliku.
public class FileSystemMock implements FileSystemAdapter {

    // Pola przechowujące wartości oczekiwane (zdefiniowane przez test)
    // oraz flagę śledzącą, czy interakcja faktycznie miała miejsce.
    private String expectedPath;
    private String expectedContent;
    private boolean writeCalled = false;

    // Metoda konfigurująca oczekiwania (Expectations).
    // Należy ją wywołać w fazie "Given" (Setup) testu, aby zdefiniować,
    // jakie parametry Mock "spodziewa się" otrzymać od testowanego serwisu.
    public void expectWrite(String path, String content) {
        this.expectedPath = path;
        this.expectedContent = content;
    }

    // Przechwycenie rzeczywistego wywołania metody przez testowany kod.
    // Działa w trybie "Fail Fast" - jeśli otrzymane argumenty nie zgadzają się z oczekiwaniami,
    // Mock natychmiast rzuca błąd asercji, przerywając test.
    @Override
    public void write(String path, String content) {
        writeCalled = true;

        // Weryfikacja zgodności argumentów w momencie wywołania.
        if (!path.equals(expectedPath)) {
            throw new AssertionError("Błąd Mocka: Oczekiwano ścieżki " + expectedPath + ", a otrzymano " + path);
        }
        if (!content.equals(expectedContent)) {
            throw new AssertionError("Błąd Mocka: Treść się nie zgadza.");
        }
    }

    // Metoda weryfikująca kompletność interakcji (Verification).
    // Wywoływana w fazie "Then" (Assert) testu. Sprawdza, czy metoda write()
    // została w ogóle uruchomiona (chroni przed sytuacją, gdy kod w ogóle nie podjął próby zapisu).
    public void verify() {
        if (!writeCalled) {
            throw new AssertionError("Błąd Mocka: Metoda write() nigdy nie została wywołana!");
        }
    }
}