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
}