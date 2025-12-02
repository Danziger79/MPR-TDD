package doubles;

import interfaces.SystemConfig;

// Rola: Implementacja wzorca Test Double typu "Dummy".
// Opis: Obiekt ten jest używany wyłącznie do wypełnienia listy argumentów (np. w konstruktorze),
// gdy testowany kod wymaga zależności (Dependency Injection), ale z niej nie korzysta w danym scenariuszu testowym.
// Jego zadaniem jest umożliwienie kompilacji i uruchomienia testu bez wpływu na jego logikę.
public class SystemConfigDummy implements SystemConfig {

    // Nadpisanie metody interfejsu rzucające wyjątek.
    // Gwarantuje to, że jeśli testowany kod spróbuje pobrać URL do bazy (co nie powinno mieć miejsca przy użyciu Dummy),
    // test zakończy się natychmiastowym błędem (Fail Fast), sygnalizując błąd w konstrukcji testu.
    @Override
    public String getDbUrl() {
        throw new UnsupportedOperationException("Dummy: nie powinno być wywołane!");
    }

    // Nadpisanie metody interfejsu rzucające wyjątek.
    // Blokuje dostęp do konfiguracji timeoutu, potwierdzając, że ten obiekt jest tylko atrapą (placeholderem)
    // i nie powinien brać czynnego udziału w logice biznesowej.
    @Override
    public int getTimeout() {
        throw new UnsupportedOperationException("Dummy: nie powinno być wywołane!");
    }
}