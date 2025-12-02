package interfaces;

// Rola: Kontrakt konfiguracyjny (Configuration Interface).
// Opis: Interfejs definiujący zestaw metod dostępowych do globalnych parametrów konfiguracyjnych systemu,
// takich jak adresy baz danych czy limity czasowe. W kontekście testów jednostkowych często wykorzystywany
// do demonstracji wzorca Dummy (obiekt wymagany przez zależność, ale nieużywany w logice testu).
public interface SystemConfig {

    // Pobiera adres URL połączenia do bazy danych (np. JDBC Connection String).
    // Metoda ta jest kluczowa dla warstwy persystencji, ale w testach jednostkowych logiki biznesowej
    // często jest zaślepiana (Stub) lub ignorowana (Dummy).
    String getDbUrl();

    // Zwraca zdefiniowany limit czasu (timeout) dla operacji systemowych, wyrażony zazwyczaj w milisekundach.
    int getTimeout();
}