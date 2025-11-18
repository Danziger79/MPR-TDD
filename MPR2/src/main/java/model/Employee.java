package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Klasa reprezentująca encję pracownika w systemie. Przechowuje dane osobowe,
// informacje o zatrudnieniu, historię ocen oraz przypisanie do zespołu projektowego.
public class Employee {
    private String name;
    private String email;
    private String companyName;
    private Position position;
    private double salary;

    // Lista przechowująca historię ocen. Inicjalizowana jako pusta lista przy tworzeniu obiektu.
    private List<Integer> ratingHistory = new ArrayList<>();
    private LocalDate dateOfHire;

    private ProjectTeam currentTeam = null;

    // Konstruktor pomocniczy. Inicjalizuje obiekt pracownika przyjmując podstawowe dane,
    // a datę zatrudnienia ustawia automatycznie na bieżącą datę systemową (LocalDate.now()).
    // Wywołuje główny konstruktor klasy.
    public Employee(String name, String email, String companyName, Position position, double salary) {
        this(name, email, companyName, position, salary, LocalDate.now());
    }

    // Główny konstruktor klasy inicjalizujący wszystkie pola wymagane do utworzenia profilu pracownika.
    public Employee(String name, String email, String companyName, Position position, double salary, LocalDate dateOfHire) {
        this.name = name;
        this.email = email;
        this.companyName = companyName;
        this.position = position;
        this.salary = salary;
        this.dateOfHire = dateOfHire;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCompanyName() { return companyName; }
    public Position getPosition() { return position; }
    public double getSalary() { return salary; }
    public LocalDate getDateOfHire() { return dateOfHire; }

    public ProjectTeam getCurrentTeam() {
        return currentTeam;
    }

    // Przypisuje pracownika do konkretnego zespołu projektowego.
    public void setCurrentTeam(ProjectTeam currentTeam) {
        this.currentTeam = currentTeam;
    }

    // Usuwa pracownika z obecnego zespołu projektowego poprzez ustawienie referencji na null.
    public void leaveCurrentTeam() {
        this.currentTeam = null;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // Zwraca historię ocen pracownika.
    // Metoda tworzy i zwraca nową kopię listy (defensive copy), aby zapobiec
    // modyfikacji wewnętrznej struktury danych obiektu przez zewnętrzne klasy.
    public List<Integer> getRatingHistory() {
        return new ArrayList<>(ratingHistory);
    }

    // Dodaje nową ocenę do historii ocen pracownika.
    // Metoda przeprowadza walidację danych wejściowych - akceptuje tylko wartości z przedziału 1-5.
    // W przypadku podania nieprawidłowej wartości rzucany jest wyjątek IllegalArgumentException.
    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Ocena musi być w skali 1-5");
        }
        this.ratingHistory.add(rating);
    }

    // Przesłonięta metoda porównująca obiekty.
    // Dwa obiekty Employee są uznawane za identyczne, jeśli posiadają ten sam adres email (unikalny identyfikator).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(email, employee.email);
    }

    // Generuje kod hash na podstawie pola email, aby zachować spójność z kontraktem metody equals.
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return name + " (" + position + ") - " + salary + " PLN, " + companyName;
    }
}