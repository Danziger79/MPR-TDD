package model;

import java.text.DecimalFormat;

// Kolejny "kontenerek" na dane, tym razem na statystyki firmy.
public class CompanyStatistics {
    private int employeeCount;
    private double averageSalary;
    private String highestEarnerName; // Pełne imię i nazwisko

    public CompanyStatistics(int employeeCount, double averageSalary, String highestEarnerName) {
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.highestEarnerName = highestEarnerName;
    }

    // Gettery (gdybyśmy ich potrzebowali)
    public int getEmployeeCount() { return employeeCount; }
    public double getAverageSalary() { return averageSalary; }
    public String getHighestEarnerName() { return highestEarnerName; }

    // Nadpisujemy toString, żeby fajnie się drukowało na konsoli
    @Override
    public String toString() {
        // Mały trik na sformatowanie pensji do dwóch miejsc po przecinku
        DecimalFormat df = new DecimalFormat("#.00");
        return "Statystyki firmy: " +
                "Liczba pracowników=" + employeeCount +
                ", Średnia pensja=" + df.format(averageSalary) + " PLN" +
                ", Najlepiej zarabiający='" + highestEarnerName + '\'';
    }
}