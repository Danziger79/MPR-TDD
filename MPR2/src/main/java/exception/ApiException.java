package exception;

// Nasz własny wyjątek, gdyby API nam nie odpowiedziało albo zwróciło błąd
// Robimy 'extends Exception', więc będzie to wyjątek "checked"
public class ApiException extends Exception {

    // Konstruktor, który przyjmuje wiadomość błędu
    public ApiException(String message) {
        super(message);
    }

    // Konstruktor, który "opakowuje" inny wyjątek (np. błąd sieciowy)
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}