package doubles;

import interfaces.NotificationService;
import model.Employee;
import java.util.ArrayList;
import java.util.List;

// Rola: Implementacja wzorca Test Double typu "Spy" (Szpieg).
// Opis: Klasa służąca do weryfikacji efektów ubocznych (Side Effects).
// Nie wykonuje rzeczywistej logiki biznesowej (np. wysyłki e-mail), lecz "podgląda" i rejestruje
// wywołania metod. Umożliwia testom weryfikację historii interakcji (np. czy metoda została wywołana
// z odpowiednimi parametrami, ile razy itp.) po zakończeniu działania logiki biznesowej.
public class NotificationServiceSpy implements NotificationService {

    // Wewnętrzny rejestr (log audytowy) przechowujący historię wszystkich wywołań metody send().
    // Kolekcja jest publiczna (lub posiada gettery), aby umożliwić asercje w fazie "Then" testu.
    public List<SentMessage> sentMessages = new ArrayList<>();

    // Niemodyfikowalna struktura danych (Immutable DTO) reprezentująca pojedynczą, przechwyconą interakcję.
    // Działa jak "snapshot" argumentów przekazanych do metody w momencie jej wywołania.
    public static class SentMessage {
        public final Employee employee;
        public final String message;

        public SentMessage(Employee employee, String message) {
            this.employee = employee;
            this.message = message;
        }
    }

    // Implementacja metody interfejsu.
    // Zamiast realizować wysyłkę, metoda przechwytuje argumenty wejściowe, tworzy obiekt SentMessage
    // i dodaje go do wewnętrznej listy historii.
    @Override
    public void send(Employee employee, String message) {
        sentMessages.add(new SentMessage(employee, message));
    }

    // Metoda pomocnicza (Utility Method) dla testów.
    // Ułatwia weryfikację liczności wywołań (Cardinality Check), np. sprawdzenie, czy wysłano dokładnie jeden e-mail.
    public int getCallCount() {
        return sentMessages.size();
    }

    // Metoda pomocnicza weryfikująca, czy dany pracownik był adresatem którejkolwiek z wysłanych wiadomości.
    // Przeszukuje historię wywołań przy użyciu strumienia, sprawdzając dopasowanie po nazwie pracownika.
    public boolean wasMessageSentTo(String employeeName) {
        return sentMessages.stream()
                .anyMatch(msg -> msg.employee.getName().equals(employeeName));
    }
}