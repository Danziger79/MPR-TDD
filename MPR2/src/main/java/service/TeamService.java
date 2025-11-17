package service;

import model.Employee;
import model.ProjectTeam;


public class TeamService {


    public void assignEmployeeToTeam(Employee employee, ProjectTeam team) {


        employee.setCurrentTeam(team);


        team.addMember(employee);
    }
}