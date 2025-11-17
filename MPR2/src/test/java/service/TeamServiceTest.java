package service;

import model.Employee;
import model.Position;
import model.ProjectTeam; // <-- NOWY IMPORT
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

// Użyjemy AssertJ do sprawdzenia (wymóg zadania)
import static org.assertj.core.api.Assertions.*;

class TeamServiceTest {

    private TeamService teamService;
    private Employee pracownik;
    private ProjectTeam team;

    @BeforeEach
    void setUp() {
        // Klasa jeszcze nie istnieje!
        teamService = new TeamService();


        pracownik = new Employee("Jan Kowalski", "jan@tech.pl", "TechCorp",
                Position.PROGRAMISTA, 9000, LocalDate.of(2020, 1, 1));
        team = new ProjectTeam("Projekt Feniks");
    }

    @Test
    @DisplayName("Dodanie pracownika do zespołu powinno ustawić mu zespół i dodać go do listy członków")
    void shouldAddEmployeeToTeam() {
        // Arrange (mamy w setup)


        // Ta metoda jeszcze nie istnieje!
        teamService.assignEmployeeToTeam(pracownik, team);




        assertThat(pracownik.getCurrentTeam())
                .as("Pracownik powinien mieć referencję do zespołu")
                .isNotNull()
                .isSameAs(team); // Sprawdzamy, czy to TEN SAM obiekt zespołu


        assertThat(team.getMembers())
                .as("Lista członków zespołu powinna zawierać pracownika")
                .hasSize(1)
                .contains(pracownik);
    }
    @Test
    @DisplayName("Próba dodania pracownika, który jest już w zespole, powinna rzucić wyjątek")
    void shouldThrowExceptionWhenEmployeeAlreadyInATeam() {

        teamService.assignEmployeeToTeam(pracownik, team);


        ProjectTeam teamB = new ProjectTeam("Projekt Goryl");


        // Sprawdzamy, czy próba dodania do DRUGIEGO zespołu rzuci błędem
        assertThatIllegalStateException() // Chcemy konkretny typ błędu
                .isThrownBy(() -> {
                    // Ta linijka powinna rzucić błędem
                    teamService.assignEmployeeToTeam(pracownik, teamB);
                })
                .withMessageContaining("Pracownik jest już w innym zespole");
    }

    @Test
    @DisplayName("Próba dodania pracownika do pełnego zespołu powinna rzucić wyjątek")
    void shouldThrowExceptionWhenTeamIsFull() {

        Employee e1 = new Employee("User1", "u1@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());
        Employee e2 = new Employee("User2", "u2@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());
        Employee e3 = new Employee("User3", "u3@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());
        Employee e4 = new Employee("User4", "u4@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());


        teamService.assignEmployeeToTeam(e1, team);
        teamService.assignEmployeeToTeam(e2, team);
        teamService.assignEmployeeToTeam(e3, team);
        teamService.assignEmployeeToTeam(e4, team);


        teamService.assignEmployeeToTeam(pracownik, team);


        Employee e6_niezmesciSie = new Employee("User6", "u6@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());


        assertThatIllegalStateException()
                .isThrownBy(() -> {

                    teamService.assignEmployeeToTeam(e6_niezmesciSie, team);
                })
                .withMessageContaining("Zespół jest już pełny");
    }
    @Test
    @DisplayName("Przeniesienie pracownika powinno usunąć go ze starego zespołu i dodać do nowego")
    void shouldTransferEmployeeBetweenTeams() {

        teamService.assignEmployeeToTeam(pracownik, team);


        ProjectTeam teamB = new ProjectTeam("Projekt Goryl");


        teamService.transferEmployee(pracownik, teamB);


        assertThat(pracownik.getCurrentTeam())
                .as("Pracownik powinien być teraz w Zespole B")
                .isSameAs(teamB);


        assertThat(team.getMembers())
                .as("Zespół A nie powinien już mieć tego pracownika")
                .isEmpty();


        assertThat(teamB.getMembers())
                .as("Zespół B powinien teraz mieć tego pracownika")
                .containsExactly(pracownik); // Sprawdza, czy zawiera TYLKO jego
    }
    @Test
    @DisplayName("Sprawdzenie różnorodności powinno zwrócić false, jeśli w zespole nie ma Managera")
    void shouldReturnFalseForTeamWithNoManager() {

        teamService.assignEmployeeToTeam(pracownik, team);


        Employee p2 = new Employee("User1", "u1@tech.pl", "Tech", Position.PROGRAMISTA, 8000, LocalDate.now());
        Employee s1 = new Employee("User2", "u2@tech.pl", "Tech", Position.STAZYSTA, 3000, LocalDate.now());
        teamService.assignEmployeeToTeam(p2, team);
        teamService.assignEmployeeToTeam(s1, team);


        boolean isCompliant = teamService.isTeamDiversityCompliant(team);


        assertThat(isCompliant)
                .as("Zespół bez Managera nie powinien być 'compliant'")
                .isFalse();
    }
    @Test
    @DisplayName("Sprawdzenie różnorodności powinno zwrócić true, jeśli w zespole jest Manager")
    void shouldReturnTrueForTeamWithManager() {

        teamService.assignEmployeeToTeam(pracownik, team);


        Employee manager = new Employee("Szef Szefów", "szef@tech.pl", "Tech", Position.MANAGER, 15000, LocalDate.now());


        teamService.assignEmployeeToTeam(manager, team);


        boolean isCompliant = teamService.isTeamDiversityCompliant(team);


        assertThat(isCompliant)
                .as("Zespół z Managerem powinien być 'compliant'")
                .isTrue();
    }
}
