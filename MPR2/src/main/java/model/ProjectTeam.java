package model;

import java.util.ArrayList;
import java.util.List;

// Klasa domenowa reprezentująca zespół projektowy.
// Zarządza listą przypisanych pracowników oraz przechowuje stałe ograniczenia biznesowe dotyczące struktury zespołu.
public class ProjectTeam {

    private String teamName;

    // Wewnętrzna lista przechowywująca referencje do obiektów Employee będących członkami tego zespołu.
    private List<Employee> members = new ArrayList<>();

    // Stała konfiguracyjna określająca maksymalny dopuszczalny rozmiar zespołu zgodnie z regułami biznesowymi.
    private static final int MAX_TEAM_SIZE = 5;

    // Konstruktor tworzący nowy zespół o wskazanej nazwie.
    public ProjectTeam(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    // Zwraca listę obecnych członków zespołu.
    // Metoda zwraca tzw. "defensive copy" (nową instancję listy), aby uniemożliwić modyfikację
    // wewnętrznego stanu obiektu ProjectTeam poprzez bezpośrednie operacje na zwróconej liście.
    public List<Employee> getMembers() {
        return new ArrayList<>(members);
    }

    public int getMaxTeamSize() {
        return MAX_TEAM_SIZE;
    }

    // Dodaje pracownika do zespołu.
    // Metoda zawiera mechanizm dedublikacji - weryfikuje, czy dany pracownik nie znajduje się już na liście,
    // aby uniknąć wielokrotnego przypisania tej samej osoby do jednego projektu.
    public void addMember(Employee employee) {
        if (!members.contains(employee)) {
            members.add(employee);
        }
    }

    // Usuwa wskazanego pracownika z listy członków zespołu.
    public void removeMember(Employee employee) {
        members.remove(employee);
    }
}