package service;

import model.CompanyStatistics;
import model.Employee;
import model.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Kompletny serwis - łączymy logikę z zadania 1 i 2
public class EmployeeService {

    // Lista już nie jest 'static'! Każda instancja serwisu
    // ma swoją własną listę. To JEST kluczowe dla testów.
    private final List<Employee> employees = new ArrayList<>();

    // --- Metody z pierwszego zadania (oryginalne) ---

    public boolean addEmployee(Employee employee) {
        for (Employee e : employees) {
            if (e.equals(employee)) { // equals() sprawdza po emailu
                return false;
            }
        }
        employees.add(employee);
        return true;
    }

    public void showAllEmployees() {
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    public List<Employee> findByCompany(String companyName) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getCompanyName().equalsIgnoreCase(companyName)) {
                result.add(e);
            }
        }
        return result;
    }

    // --- Metody przywrócone (o które prosi zadanie testowe) ---

    /**
     * Zwraca Kopię listy posortowaną alfabetycznie po imieniu.
     * Nie modyfikuje oryginalnej listy.
     */
    public List<Employee> sortByName() {
        // Tworzymy kopię listy, żeby nie zmieniać oryginału
        List<Employee> sortedList = new ArrayList<>(employees);

        // Klasyczny sort bąbelkowy (z zadania 1)
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

    /**
     * Grupuje pracowników po stanowisku.
     */
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

    /**
     * Liczy, ilu pracowników jest na każdym stanowisku.
     */
    public Map<Position, Integer> countByPosition() {
        Map<Position, Integer> counts = new HashMap<>();
        for (Employee e : employees) {
            Position pos = e.getPosition();
            // Używamy getOrDefault, żeby kod był krótszy
            counts.put(pos, counts.getOrDefault(pos, 0) + 1);
        }
        return counts;
    }

    /**
     * Oblicza średnie wynagrodzenie wszystkich pracowników.
     */
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

    /**
     * Znajduje pracownika z najwyższym wynagrodzeniem.
     */
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

    // --- Metody analityczne z zadania 2 (CSV/API) ---

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

    public Map<String, CompanyStatistics> getCompanyStatistics() {
        Map<String, List<Employee>> employeesByCompany = new HashMap<>();
        for (Employee e : employees) {
            String companyName = e.getCompanyName();
            if (!employeesByCompany.containsKey(companyName)) {
                employeesByCompany.put(companyName, new ArrayList<>());
            }
            employeesByCompany.get(companyName).add(e);
        }

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