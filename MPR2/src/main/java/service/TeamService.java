package service;

import model.Employee;
import model.Position;
import model.ProjectTeam;

public class TeamService {


    public void assignEmployeeToTeam(Employee employee, ProjectTeam team) {

        if (employee.getCurrentTeam() != null) {
            throw new IllegalStateException("Pracownik jest już w innym zespole ("
                    + employee.getCurrentTeam().getTeamName() + ")");
        }


        if (team.getMembers().size() >= team.getMaxTeamSize()) {
            throw new IllegalStateException("Zespół jest już pełny (Max: "
                    + team.getMaxTeamSize() + ")");
        }

        forceAssign(employee, team);
    }

    public void transferEmployee(Employee employee, ProjectTeam newTeam) {

        if (newTeam.getMembers().size() >= newTeam.getMaxTeamSize()) {
            throw new IllegalStateException("Nowy zespół jest już pełny (Max: "
                    + newTeam.getMaxTeamSize() + ")");
        }


        if (employee.getCurrentTeam() != null) {
            forceRemove(employee, employee.getCurrentTeam());
        }


        forceAssign(employee, newTeam);
    }

    private void forceRemove(Employee employee, ProjectTeam team) {
        team.removeMember(employee);
        employee.leaveCurrentTeam();
    }

    private void forceAssign(Employee employee, ProjectTeam team) {
        employee.setCurrentTeam(team);
        team.addMember(employee);
    }


    public boolean isTeamDiversityCompliant(ProjectTeam team) {

        for (Employee member : team.getMembers()) {

            if (member.getPosition() == Position.MANAGER) {
                return true; // Jest OK
            }
        }


        return false;
    }
}
//nic