package service;

import model.Employee;
import model.ProjectTeam;


public class TeamService {


    public void assignEmployeeToTeam(Employee employee, ProjectTeam team) {

        if (employee.getCurrentTeam() != null) {

            throw new IllegalStateException("Pracownik jest już w innym zespole ("
                    + employee.getCurrentTeam().getTeamName() + ")");
        }
        employee.setCurrentTeam(team);


        team.addMember(employee);
    }
}