package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Employee {
    private String name;
    private String email;
    private String companyName;
    private Position position;
    private double salary;

    private List<Integer> ratingHistory = new ArrayList<>();


    private LocalDate dateOfHire;



    public Employee(String name, String email, String companyName, Position position, double salary) {

        this(name, email, companyName, position, salary, LocalDate.now());
    }


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


    public LocalDate getDateOfHire() {
        return dateOfHire;
    }


    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public List<Integer> getRatingHistory() {
        return new ArrayList<>(ratingHistory);
    }

    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Ocena musi być w skali 1-5");
        }
        this.ratingHistory.add(rating);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return name + " (" + position + ") - " + salary + " PLN, " + companyName;
    }
}