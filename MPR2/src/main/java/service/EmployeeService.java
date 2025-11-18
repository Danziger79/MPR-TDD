package service;

import model.CompanyStatistics;
import model.Employee;
import model.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Główny serwis logiki biznesowej odpowiedzialny za zarządzanie danymi pracowników.
// Klasa integruje operacje CRUD, funkcje analityczne, raportowe oraz walidację danych.
public class EmployeeService {

    // Wewnętrzny magazyn danych (repozytorium in-memory) przechowujący listę pracowników.
    // Lista jest niestatyczna, co oznacza, że każda instancja serwisu pracuje na własnym zbiorze danych.
    private final List<Employee> employees = new ArrayList<>();

    /**
     * Dodaje nowego pracownika do rejestru.
     * Przed dodaniem weryfikuje unikalność pracownika w oparciu o metodę equals() (np. unikalny email).
     *
     * @param employee Obiekt pracownika do dodania.
     * @return true, jeśli pracownik został pomyślnie dodany; false, jeśli taki pracownik już istnieje w systemie.
     */
    public boolean addEmployee(Employee employee) {
        for (Employee e : employees) {
            if (e.equals(employee)) {
                return false;
            }
        }
        employees.add(employee);
        return true;
    }

    // Wyświetla pełną listę pracowników na standardowym wyjściu (konsola).
    // Metoda pomocnicza służąca do szybkiej weryfikacji stanu serwisu.
    public void showAllEmployees() {
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    // Wyszukuje pracowników zatrudnionych w firmie o podanej nazwie.
    // Wyszukiwanie jest niewrażliwe na wielkość liter (case-insensitive).
    public List<Employee> findByCompany(String companyName) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getCompanyName().equalsIgnoreCase(companyName)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Zwraca listę pracowników posortowaną alfabetycznie według imienia i nazwiska.
     * Metoda nie modyfikuje wewnętrznej listy serwisu, lecz zwraca jej posortowaną kopię.
     * Zastosowano algorytm sortowania bąbelkowego.
     *
     * @return Posortowana lista obiektów Employee.
     */
    public List<Employee> sortByName() {
        List<Employee> sortedList = new ArrayList<>(employees);

        for (int i = 0; i < sortedList.size() - 1; i++) {
            for (int j = 0; j < sortedList.size() - 1 - i; j++) {
                if (sortedList.get(j).getName().compareToIgnoreCase(sortedList.get(j + 1).getName()) > 0) {
                    Employee temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                }
            }
        }
        return sortedList;
    }

    // Grupuje pracowników według zajmowanego stanowiska (Position).
    // Zwraca mapę, gdzie kluczem jest stanowisko, a wartością lista pracowników przypisanych do tego stanowiska.
    public Map<Position, List<Employee>> groupByPosition() {
        Map<Position, List<Employee>> grouped = new HashMap<>();
        for (Employee e : employees) {
            Position pos = e.getPosition();
            if (!grouped.containsKey(pos)) {
                grouped.put(pos, new ArrayList<>());
            }
            grouped.get(pos).add(e);
        }
        return grouped;
    }

    // Agreguje dane statystyczne dotyczące liczby pracowników na poszczególnych stanowiskach.
    // Zwraca mapę z licznością wystąpień dla każdego stanowiska.
    public Map<Position, Integer> countByPosition() {
        Map<Position, Integer> counts = new HashMap<>();
        for (Employee e : employees) {
            Position pos = e.getPosition();
            counts.put(pos, counts.getOrDefault(pos, 0) + 1);
        }
        return counts;
    }

    // Oblicza średnie wynagrodzenie dla wszystkich pracowników zarejestrowanych w serwisie.
    // W przypadku braku pracowników zwraca wartość 0.0.
    public double averageSalary() {
        if (employees.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Employee e : employees) {
            sum += e.getSalary();
        }
        return sum / employees.size();
    }

    // Identyfikuje pracownika otrzymującego najwyższe wynagrodzenie w całej firmie.
    // Zwraca null w przypadku pustej listy pracowników.
    public Employee findHighestSalary() {
        if (employees.isEmpty()) {
            return null;
        }
        Employee highest = employees.get(0);
        for (Employee e : employees) {
            if (e.getSalary() > highest.getSalary()) {
                highest = e;
            }
        }
        return highest;
    }

    /**
     * Weryfikuje zgodność wynagrodzeń z widełkami płacowymi zdefiniowanymi dla danego stanowiska.
     * Sprawdza, czy aktualna pensja pracownika nie jest niższa niż minimalna stawka (baseSalary) dla jego pozycji.
     *
     * @return Lista pracowników, których wynagrodzenie nie spełnia wymogów minimalnych.
     */
    public List<Employee> validateSalaryConsistency() {
        List<Employee> inconsistentEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            double baseSalary = employee.getPosition().getBaseSalary();
            if (employee.getSalary() < baseSalary) {
                inconsistentEmployees.add(employee);
            }
        }
        return inconsistentEmployees;
    }

    /**
     * Generuje szczegółowy raport statystyczny dla każdej firmy występującej w systemie.
     * Proces składa się z dwóch etapów:
     * 1. Grupowania pracowników według nazwy firmy.
     * 2. Obliczenia statystyk (liczba pracowników, średnia płaca, najlepiej zarabiający) dla każdej grupy
     * i zmapowania ich na obiekt DTO CompanyStatistics.
     *
     * @return Mapa, gdzie kluczem jest nazwa firmy, a wartością obiekt statystyk.
     */
    public Map<String, CompanyStatistics> getCompanyStatistics() {
        // Etap 1: Grupowanie tymczasowe
        Map<String, List<Employee>> employeesByCompany = new HashMap<>();
        for (Employee e : employees) {
            String companyName = e.getCompanyName();
            if (!employeesByCompany.containsKey(companyName)) {
                employeesByCompany.put(companyName, new ArrayList<>());
            }
            employeesByCompany.get(companyName).add(e);
        }

        // Etap 2: Agregacja danych i tworzenie raportu
        Map<String, CompanyStatistics> companyStats = new HashMap<>();
        for (Map.Entry<String, List<Employee>> entry : employeesByCompany.entrySet()) {
            String companyName = entry.getKey();
            List<Employee> companyEmployees = entry.getValue();

            int employeeCount = companyEmployees.size();
            double totalSalary = 0.0;
            Employee highestEarner = null;

            for (Employee emp : companyEmployees) {
                totalSalary += emp.getSalary();
                if (highestEarner == null || emp.getSalary() > highestEarner.getSalary()) {
                    highestEarner = emp;
                }
            }

            double averageSalary = (employeeCount > 0) ? totalSalary / employeeCount : 0.0;
            String earnerName = (highestEarner != null) ? highestEarner.getName() : "N/A";

            CompanyStatistics stats = new CompanyStatistics(employeeCount, averageSalary, earnerName);
            companyStats.put(companyName, stats);
        }
        return companyStats;
    }
}