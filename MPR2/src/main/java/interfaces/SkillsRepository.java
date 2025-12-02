package interfaces;

import model.Employee;
import java.util.Set;

// Rola: Kontrakt repozytorium danych (Data Access Object Contract).
// Opis: Interfejs definiujący abstrakcję dostępu do danych o umiejętnościach pracowników.
// Separuje logikę biznesową od fizycznego sposobu przechowywania danych.
// W testach umożliwia łatwe podstawienie implementacji typu Fake (In-Memory) zamiast prawdziwej bazy SQL.
public interface SkillsRepository {

    // Pobiera zbiór unikalnych kompetencji przypisanych do danego pracownika.
    // Zwraca kolekcję typu Set, co gwarantuje brak duplikatów (np. nie można mieć dwa razy "JAVA").
    Set<String> getSkills(Employee employee);

    // Rejestruje nową umiejętność dla wskazanego pracownika.
    // Operacja powinna być trwała (persist) w ramach cyklu życia aplikacji (lub testu w przypadku Fake'a).
    void addSkill(Employee employee, String skill);
}