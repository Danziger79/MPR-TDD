package service;

import model.Employee;
import model.Position;
import model.ProjectTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

// Import statyczny dla AssertJ (płynne asercje)
import static org.assertj.core.api.Assertions.*;
// Import statyczny dla Hamcrest (matcher'y)
import static org.hamcrest.Matchers.*;

// Klasa testowa weryfikująca logikę zarządzania zespołami (TeamService).
// Pokrywa scenariusze przypisywania pracowników, transferów międzyzespołowych oraz walidacji reguł biznesowych (pojemność, różnorodność).
class TeamServiceTest {

    private TeamService teamService;
    private Employee employee;
    private ProjectTeam team;

    // Inicjalizacja środowiska testowego.
    @BeforeEach
    void setUp() {
        teamService = new TeamService();

        // Utworzenie przykładowego pracownika i zespołu
        employee = new Employee("Jan Kowalski", "jan@tech.pl", "TechCorp",
                Position.PROGRAMISTA, 9000, LocalDate.of(2020, 1, 1));
        team = new ProjectTeam("Projekt Feniks");
    }

    @Test
    @DisplayName("Przypisanie pracownika do zespołu powinno ustanowić relację dwukierunkową")
    void shouldAddEmployeeToTeam() {
        // Act
        teamService.assignEmployeeToTeam(employee, team);

        // Assert (AssertJ)
        // Weryfikacja po stronie pracownika (czy wie, w jakim jest zespole)
        assertThat(employee.getCurrentTeam())
                .as("Pracownik powinien posiadać referencję do przypisanego zespołu")
                .isNotNull()
                .isSameAs(team);

        // Weryfikacja po stronie zespołu (czy ma pracownika na liście)
        assertThat(team.getMembers())
                .as("Lista członków zespołu powinna zawierać nowo dodanego pracownika")
                .hasSize(1)
                .contains(employee);
    }

    @Test
    @DisplayName("Próba przypisania pracownika będącego już w zespole powinna rzucić wyjątek")
    void shouldThrowExceptionWhenEmployeeAlreadyInATeam() {
        // Arrange
        teamService.assignEmployeeToTeam(employee, team);
        ProjectTeam teamB = new ProjectTeam("Projekt Goryl");

        // Act & Assert
        // System powinien zablokować próbę nadpisania zespołu bez wcześniejszego usunięcia (wymagany transfer)
        assertThatIllegalStateException()
                .as("Nie można przypisać pracownika, który jest już członkiem innego zespołu")
                .isThrownBy(() -> {
                    teamService.assignEmployeeToTeam(employee, teamB);
                })
                .withMessageContaining("Pracownik jest już w innym zespole");
    }

    @Test
    @DisplayName("Próba dodania pracownika do pełnego zespołu (Max 5) powinna rzucić wyjątek")
    void shouldThrowExceptionWhenTeamIsFull() {
        // Arrange - Wypełnienie zespołu do pełna (5 osób)
        // Tworzymy 4 dodatkowych pracowników + nasz główny 'employee'
        for (int i = 1; i <= 4; i++) {
            Employee filler = new Employee("Filler" + i, "f" + i + "@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());
            teamService.assignEmployeeToTeam(filler, team);
        }
        teamService.assignEmployeeToTeam(employee, team); // 5. osoba (Ostatnie wolne miejsce)

        // Próba dodania 6. osoby
        Employee extraEmployee = new Employee("Nadmiarowy", "over@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());

        // Act & Assert
        assertThatIllegalStateException()
                .as("Dodanie pracownika powyżej limitu (5) powinno być zablokowane")
                .isThrownBy(() -> {
                    teamService.assignEmployeeToTeam(extraEmployee, team);
                })
                .withMessageContaining("Zespół jest już pełny");
    }

    @Test
    @DisplayName("Transfer pracownika powinien poprawnie przenieść go między zespołami (Atomowość operacji)")
    void shouldTransferEmployeeBetweenTeams() {
        // Arrange
        teamService.assignEmployeeToTeam(employee, team); // Zespół Źródłowy
        ProjectTeam teamB = new ProjectTeam("Projekt Goryl"); // Zespół Docelowy

        // Act
        teamService.transferEmployee(employee, teamB);

        // Assert
        // 1. Pracownik wskazuje na nowy zespół
        assertThat(employee.getCurrentTeam())
                .as("Pracownik powinien być przypisany do zespołu docelowego")
                .isSameAs(teamB);

        // 2. Stary zespół jest pusty
        assertThat(team.getMembers())
                .as("Zespół źródłowy powinien zostać opuszczony")
                .isEmpty();

        // 3. Nowy zespół zawiera pracownika
        assertThat(teamB.getMembers())
                .as("Zespół docelowy powinien zawierać przetransferowanego pracownika")
                .containsExactly(employee);
    }

    @Test
    @DisplayName("Weryfikacja różnorodności (Compliance) powinna zwrócić false dla zespołu bez Managera")
    void shouldReturnFalseForTeamWithNoManager() {
        // Arrange - Zespół składający się tylko z Programisty i Stażysty
        teamService.assignEmployeeToTeam(employee, team); // Programista
        Employee intern = new Employee("Stażysta", "intern@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());
        teamService.assignEmployeeToTeam(intern, team);

        // Act
        boolean isCompliant = teamService.isTeamDiversityCompliant(team);

        // Assert
        assertThat(isCompliant)
                .as("Zespół bez osoby na stanowisku MANAGER nie spełnia wymogów compliance")
                .isFalse();
    }

    @Test
    @DisplayName("Weryfikacja różnorodności powinna zwrócić true, jeśli w zespole znajduje się Manager")
    void shouldReturnTrueForTeamWithManager() {
        // Arrange
        teamService.assignEmployeeToTeam(employee, team);
        Employee manager = new Employee("Szef Szefów", "szef@tech.pl", "Tech", Position.MANAGER, 15000, LocalDate.now());
        teamService.assignEmployeeToTeam(manager, team);

        // Act
        boolean isCompliant = teamService.isTeamDiversityCompliant(team);

        // Assert
        assertThat(isCompliant)
                .as("Obecność Managera powinna skutkować pozytywną weryfikacją compliance")
                .isTrue();
    }

    /**
     * Test demonstracyjny pokazujący możliwości biblioteki Hamcrest.
     * Weryfikuje złożone właściwości obiektów (Properties, Collections, Ranges).
     */
    @Test
    @DisplayName("Demonstracja użycia matcherów biblioteki Hamcrest")
    void hamcrestMatchersShowcaseTest() {
        // Arrange
        teamService.assignEmployeeToTeam(employee, team);

        // Assert (Hamcrest Syntax)

        // Sprawdzenie czy wartość nie jest nullem
        org.hamcrest.MatcherAssert.assertThat(employee.getCurrentTeam(), is(notNullValue()));

        // Inspekcja właściwości zagnieżdżonych (Bean Properties)
        org.hamcrest.MatcherAssert.assertThat(employee.getCurrentTeam(),
                hasProperty("teamName", startsWith("Projekt")));

        // Operacje na kolekcjach
        org.hamcrest.MatcherAssert.assertThat(team.getMembers(), hasSize(1));
        org.hamcrest.MatcherAssert.assertThat(team.getMembers(), hasItem(employee));

        // Łączenie warunków logicznych (AND) - Sprawdzenie widełek płacowych
        org.hamcrest.MatcherAssert.assertThat(employee.getSalary(),
                is(allOf(greaterThan(8000.0), lessThan(10000.0))));
    }
}