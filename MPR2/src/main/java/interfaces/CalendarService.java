package interfaces;

import model.Employee;
import java.util.List;

// Rola: Kontrakt serwisu kalendarza (Availability Contract).
// Opis: Interfejs definiujący abstrakcję dla systemu zarządzania czasem pracy.
// Odpowiada za dostarczanie informacji o dostępności zasobów ludzkich.
// W testach jednostkowych jest to kluczowy punkt styku dla wzorca Stub (CalendarServiceStub),
// który pozwala symulować dostępność pracowników bez skomplikowanych obliczeń dat i godzin.
public interface CalendarService {

    // Pobiera listę pracowników dostępnych w terminie wymaganym do realizacji konkretnego zadania.
    // Metoda abstrahuje logikę sprawdzania urlopów, zwolnień oraz przypisania do innych projektów.
    List<Employee> getAvailableEmployees(long taskId);
}