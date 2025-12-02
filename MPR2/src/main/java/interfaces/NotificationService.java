package interfaces;

import model.Employee;

// Rola: Kontrakt systemu powiadomień (Notification Contract).
// Opis: Interfejs definiujący abstrakcję dla mechanizmów komunikacji wychodzącej (e-mail, SMS, Slack).
// Jego głównym celem jest odseparowanie logiki biznesowej od technicznych szczegółów wysyłki.
// W testach jednostkowych jest kluczowym miejscem zastosowania wzorca Spy (do weryfikacji, czy powiadomienie zostało wysłane).
public interface NotificationService {

    // Zleca wysłanie wiadomości do wskazanego pracownika.
    // Metoda jest typu "void", co oznacza, że jej działanie polega na efektach ubocznych (Side Effects).
    // Implementacja może realizować wysyłkę synchronicznie lub kolejować ją do przetworzenia w tle.
    void send(Employee employee, String message);
}