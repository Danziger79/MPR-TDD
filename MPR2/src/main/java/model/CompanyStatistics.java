package model;

import java.text.DecimalFormat;

// Klasa modelu danych (DTO) przechowująca zagregowane statystyki dotyczące firmy.
// Służy do przenoszenia przetworzonych informacji o zatrudnieniu i wynagrodzeniach.
public class CompanyStatistics {
    private int employeeCount;
    private double averageSalary;
    private String highestEarnerName;

    // Konstruktor inicjalizujący obiekt wszystkimi wymaganymi danymi statystycznymi.
    // Przyjmuje obliczoną wcześniej liczbę pracowników, średnią pensję oraz dane pracownika z najwyższym wynagrodzeniem.
    public CompanyStatistics(int employeeCount, double averageSalary, String highestEarnerName) {
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.highestEarnerName = highestEarnerName;
    }

    public int getEmployeeCount() { return employeeCount; }
    public double getAverageSalary() { return averageSalary; }
    public String getHighestEarnerName() { return highestEarnerName; }

    // Przesłonięcie metody toString w celu wygenerowania czytelnego raportu tekstowego.
    // Wewnątrz metody wykorzystywana jest klasa DecimalFormat, aby sformatować wartość zmiennoprzecinkową
    // (średnią pensję) do dwóch miejsc po przecinku, zgodnie ze standardem zapisu walutowego.
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return "Statystyki firmy: " +
                "Liczba pracowników=" + employeeCount +
                ", Średnia pensja=" + df.format(averageSalary) + " PLN" +
                ", Najlepiej zarabiający='" + highestEarnerName + '\'';
    }
}