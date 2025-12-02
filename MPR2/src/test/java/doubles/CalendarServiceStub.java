package doubles;

import interfaces.CalendarService;
import model.Employee;
import java.util.List;

// Implementacja wzorca Test Double typu "Stub".
// Klasa symuluje działanie zewnętrznego serwisu kalendarza, dostarczając deterministyczne,
// sztywno zdefiniowane dane (canned answers) na potrzeby konkretnego scenariusza testowego.
// Pozwala to testować logikę zależną od dostępności pracowników bez konieczności wykonywania skomplikowanych obliczeń dat.
public class CalendarServiceStub implements CalendarService {

    // Pole przechowujące przygotowaną odpowiedź, którą zwróci Stub.
    private List<Employee> availableEmployees;

    // Metoda konfiguracyjna (nie pochodzi z interfejsu).
    // Służy do "nauczenia" Stuba, co ma zwrócić, gdy testowany kod o to zapyta.
    // Używana w fazie "Arrange" (Setup) testu.
    public void setAvailableEmployees(List<Employee> employees) {
        this.availableEmployees = employees;
    }

    // Implementacja metody z interfejsu.
    // Zwraca zaprogramowaną wcześniej listę pracowników, całkowicie ignorując parametry wejściowe (taskId).
    // To typowe zachowanie Stuba - zwraca prosty wynik bez analizy logiki biznesowej.
    @Override
    public List<Employee> getAvailableEmployees(long taskId) {
        return availableEmployees;
    }
}