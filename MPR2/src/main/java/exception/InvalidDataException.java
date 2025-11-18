package exception;

// Wyjątek kontrolowany (checked) zgłaszany w przypadku wykrycia nieprawidłowych danych podczas przetwarzania,
// na przykład gdy format pliku wejściowego nie jest zgodny z oczekiwanym schematem.
public class InvalidDataException extends Exception {

    // Konstruktor inicjalizujący wyjątek z wiadomością tekstową wyjaśniającą konkretny powód błędu walidacji.
    public InvalidDataException(String message) {
        super(message);
    }
}