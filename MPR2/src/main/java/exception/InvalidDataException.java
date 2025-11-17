package exception;

// Wyjątek do walidacji danych, np. w pliku CSV
public class InvalidDataException extends Exception {

    public InvalidDataException(String message) {
        super(message);
    }
}