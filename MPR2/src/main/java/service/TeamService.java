package service;

import model.Employee;
import model.Position;
import model.ProjectTeam;

// Serwis operacyjny odpowiedzialny za zarządzanie strukturą zespołów projektowych.
// Obsługuje procesy przydzielania pracowników (onboarding), transferów międzyzespołowych
// oraz weryfikację zgodności struktury zespołu z regułami biznesowymi.
public class TeamService {

    /**
     * Przypisuje pracownika do wskazanego zespołu projektowego.
     * Metoda przeprowadza walidację dostępności pracownika (czy nie posiada już przypisania)
     * oraz pojemności zespołu docelowego (czy nie przekroczono limitu członków).
     *
     * @param employee Pracownik, który ma zostać przypisany.
     * @param team Zespół docelowy.
     * @throws IllegalStateException W przypadku, gdy pracownik jest już członkiem innego zespołu lub gdy zespół docelowy jest pełny.
     */
    public void assignEmployeeToTeam(Employee employee, ProjectTeam team) {
        // Weryfikacja czy pracownik jest "wolnym zasobem".
        if (employee.getCurrentTeam() != null) {
            throw new IllegalStateException("Pracownik jest już w innym zespole ("
                    + employee.getCurrentTeam().getTeamName() + ")");
        }

        // Weryfikacja limitu miejsc w zespole (Capacity Check).
        if (team.getMembers().size() >= team.getMaxTeamSize()) {
            throw new IllegalStateException("Zespół jest już pełny (Max: "
                    + team.getMaxTeamSize() + ")");
        }

        forceAssign(employee, team);
    }

    /**
     * Realizuje procedurę transferu pracownika z obecnego zespołu do nowego.
     * Operacja jest atomowa z punktu widzenia logiki biznesowej: najpierw sprawdza dostępność miejsca w nowym zespole,
     * następnie usuwa powiązanie ze starym i tworzy nowe.
     *
     * @param employee Pracownik podlegający transferowi.
     * @param newTeam Zespół docelowy.
     * @throws IllegalStateException Jeśli nowy zespół nie posiada wolnych miejsc.
     */
    public void transferEmployee(Employee employee, ProjectTeam newTeam) {
        // Sprawdzenie pojemności przed wykonaniem jakichkolwiek zmian (Fail-fast).
        if (newTeam.getMembers().size() >= newTeam.getMaxTeamSize()) {
            throw new IllegalStateException("Nowy zespół jest już pełny (Max: "
                    + newTeam.getMaxTeamSize() + ")");
        }

        // Jeśli pracownik posiada aktualne przypisanie, należy je najpierw rozwiązać.
        if (employee.getCurrentTeam() != null) {
            forceRemove(employee, employee.getCurrentTeam());
        }

        // Ustanowienie nowej relacji.
        forceAssign(employee, newTeam);
    }

    // Metoda pomocnicza usuwająca dwukierunkową relację między pracownikiem a zespołem.
    // Aktualizuje stan obiektu Team (usunięcie z listy) oraz Employee (ustawienie referencji na null).
    private void forceRemove(Employee employee, ProjectTeam team) {
        team.removeMember(employee);
        employee.leaveCurrentTeam();
    }

    // Metoda pomocnicza ustanawiająca dwukierunkową relację między pracownikiem a zespołem.
    // Aktualizuje stan obiektu Employee (ustawienie referencji zespołu) oraz Team (dodanie do listy członków).
    private void forceAssign(Employee employee, ProjectTeam team) {
        employee.setCurrentTeam(team);
        team.addMember(employee);
    }

    /**
     * Weryfikuje, czy zespół spełnia wymogi strukturalne (Compliance).
     * W obecnej implementacji sprawdza, czy w składzie zespołu znajduje się przynajmniej jedna osoba
     * na stanowisku kierowniczym (MANAGER).
     *
     * @param team Zespół poddawany weryfikacji.
     * @return true, jeśli zespół posiada Managera; false w przeciwnym razie.
     */
    public boolean isTeamDiversityCompliant(ProjectTeam team) {
        for (Employee member : team.getMembers()) {
            if (member.getPosition() == Position.MANAGER) {
                return true;
            }
        }
        return false;
    }
}