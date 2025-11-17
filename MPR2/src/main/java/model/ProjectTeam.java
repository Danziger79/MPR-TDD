package model;

import java.util.ArrayList;
import java.util.List;

public class ProjectTeam {

    private String teamName;
    private List<Employee> members = new ArrayList<>();


    private static final int MAX_TEAM_SIZE = 5;

    public ProjectTeam(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }


    public List<Employee> getMembers() {
        return new ArrayList<>(members);
    }

    public int getMaxTeamSize() {
        return MAX_TEAM_SIZE;
    }


    public void addMember(Employee employee) {

        if (!members.contains(employee)) {
            members.add(employee);
        }
    }


    public void removeMember(Employee employee) {
        members.remove(employee);
    }
}