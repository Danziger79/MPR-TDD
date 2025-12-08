package service;

import interfaces.FileSystemAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Rola: Test jednostkowy z wykorzystaniem frameworka Mockito (Error Handling Scenario).
// Opis: Test weryfikuje odporność serwisu ExportService na błędy infrastruktury (Exceptions).
// Sprawdza, czy w przypadku błędu zapisu (IO), serwis poprawnie przetwarza dane (formatowanie)
// oraz czy podejmuje próbę zaraportowania błędu (logging), zamiast pozwolić aplikacji się "wysypać".
@ExtendWith(MockitoExtension.class) // Integracja Mockito z JUnit 5 - automatyzuje inicjalizację pól @Mock.
class ExportServiceTest {

    // @Mock: Tworzy całkowitą atrapę obiektu FileSystemAdapter.
    // Obiekt ten nie ma żadnej logiki (metody nic nie robią), dopóki ich nie zaprogramujemy (Stubbing).
    // Służy do symulacji zewnętrznych systemów (tutaj: dysku twardego).
    @Mock
    private FileSystemAdapter fileSystemAdapter;

    // @Spy: Tworzy "szpiega" na rzeczywistej instancji obiektu (Real Object Proxy).
    // Metody wywoływane na tym polu wykonują PRAWDZIWY kod (ExportService.CsvFormatter),
    // ale Mockito pozwala nam śledzić te wywołania (np. czy metoda format() została uruchomiona).
    @Spy
    private ExportService.CsvFormatter csvFormatter = new ExportService.CsvFormatter();

    // @InjectMocks: Automatycznie tworzy instancję testowanej klasy (SUT - System Under Test)
    // i wstrzykuje do niej utworzone powyżej zależności (@Mock oraz @Spy).
    // Zastępuje ręczne wywołanie konstruktora: new ExportService(mock, spy).
    @InjectMocks
    private ExportService service;

    @Test
    void shouldLogErrorWhenExportFails() throws IOException {
        // ARRANGE (Konfiguracja)
        List<String> data = List.of("A", "B", "C");

        // Konfiguracja zachowania Mocka (Stubbing) dla metody typu VOID.
        // Scenariusz: Symulacja awarii systemu plików (np. brak miejsca na dysku, brak uprawnień).
        // Instrukcja: "Jeśli wywołasz write() z plikiem 'report.csv' i dowolną treścią, rzuć wyjątek IOException".
        doThrow(new IOException("Disk full"))
                .when(fileSystemAdapter).write(eq("report.csv"), anyString());

        // ACT (Wykonanie)
        // Uruchamiamy metodę biznesową, spodziewając się, że wewnętrzny blok try-catch obsłuży błąd.
        service.exportData("report.csv", data);

        // ASSERT (Weryfikacja)

        // 1. Weryfikacja interakcji ze Szpiegiem (Spy).
        // Sprawdzamy, czy logika biznesowa podjęła próbę sformatowania danych,
        // zanim nastąpił błąd zapisu. Potwierdza to poprawny przepływ danych wewnątrz metody.
        verify(csvFormatter).format(data);

        // 2. Weryfikacja obsługi błędów (Error Handling Verification).
        // Sprawdzamy, czy w bloku 'catch' serwis spróbował zapisać log błędu do pliku "error.log".
        // Matcher 'contains' pozwala sprawdzić, czy treść logu zawiera kluczową frazę błędu.
        verify(fileSystemAdapter).write(eq("error.log"), contains("Failed to write"));
    }
}