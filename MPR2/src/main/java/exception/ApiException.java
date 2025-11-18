package exception;

// Definicja niestandardowego wyjątku typu "checked", dziedziczącego bezpośrednio po klasie Exception.
// Klasa przeznaczona do obsługi specyficznych błędów wynikających z komunikacji z zewnętrznym API.
public class ApiException extends Exception {

    // Konstruktor tworzący nowy wyjątek z podanym komunikatem opisującym przyczynę błędu.
    public ApiException(String message) {
        super(message);
    }

    // Konstruktor przyjmujący komunikat oraz obiekt Throwable będący pierwotną przyczyną błędu.
    // Umożliwia mechanizm "exception chaining", czyli zachowanie oryginalnego wyjątku (np. błędu sieci) wewnątrz ApiException.
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}