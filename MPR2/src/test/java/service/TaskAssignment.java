package service;

import interfaces.CalendarService;
import interfaces.SkillsRepository;
import interfaces.TaskRepository;
import model.Employee;
import model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// Rola: Test jednostkowy z wykorzystaniem frameworka Mockito.
// Opis: Klasa weryfikuje logikę biznesową TaskAssignmentService w pełnej izolacji.
// Zamiast ręcznie tworzyć klasy pomocnicze (jak CalendarServiceStub czy SkillsRepositoryFake),
// wykorzystujemy adnotacje Mockito do automatycznego generowania i konfigurowania obiektów zastępczych (Test Doubles).
@ExtendWith(MockitoExtension.class) // Integracja: Aktywuje silnik Mockito, który przetwarza adnotacje @Mock i @InjectMocks przed każdym testem.
class TaskAssignmentServiceTest {

    // @Mock: Tworzy dynamiczną atrapę (proxy) dla zależności.
    // Domyślnie taki obiekt nie posiada logiki (metody zwracają null/0/false).
    // Jego zachowanie definiujemy w teście za pomocą instrukcji when(...).thenReturn(...).
    @Mock
    private CalendarService calendarService;

    @Mock
    private SkillsRepository skillsRepository;

    @Mock
    private TaskRepository taskRepository;

    // @InjectMocks: Automatycznie tworzy instancję testowanego serwisu (SUT - System Under Test).
    // Framework wykrywa konstruktor klasy TaskAssignmentService i wstrzykuje do niego
    // utworzone powyżej instancje @Mock. Zastępuje to ręczne wywołanie: new TaskAssignmentService(mock1, mock2, mock3).
    @InjectMocks
    private TaskAssignmentService service;

    @Test
    void shouldAssignTaskWhenEmployeeIsAvailableAndCompetent() {
        // ARRANGE (Konfiguracja środowiska testowego)
        Employee emp = new Employee("Jan", "jan@test.pl", "IT", Position.PROGRAMISTA, 10000);

        // Konfiguracja STUBBINGU (Programowanie zachowania Mocków):
        // Definiujemy scenariusz "Happy Path":
        // 1. Gdy serwis zapyta kalendarz o dostępność dla dowolnego ID (anyLong()), zwróć listę z naszym pracownikiem.
        when(calendarService.getAvailableEmployees(anyLong())).thenReturn(List.of(emp));

        // 2. Gdy serwis zapyta o umiejętności tego konkretnego pracownika, zwróć zbiór zawierający "JAVA".
        when(skillsRepository.getSkills(emp)).thenReturn(Set.of("JAVA"));

        // ACT (Wykonanie logiki biznesowej)
        // Uruchamiamy testowaną metodę. Wewnątrz niej zostaną wywołane nasze skonfigurowane mocki.
        boolean result = service.assignTask(101L, emp, "JAVA");

        // ASSERT (Weryfikacja wyników)
        // Sprawdzenie wartości zwracanej przez metodę (czy algorytm zadziałał poprawnie).
        assertTrue(result, "Zadanie powinno zostać przypisane, gdy spełnione są warunki dostępności i kompetencji.");

        // Weryfikacja interakcji (Mocking/Verification):
        // Sprawdzamy efekt uboczny (Side Effect) działania metody.
        // Upewniamy się, że serwis faktycznie wywołał metodę saveAssignment na repozytorium zadań dokładnie jeden raz.
        verify(taskRepository, times(1)).saveAssignment(101L, emp);
    }
}