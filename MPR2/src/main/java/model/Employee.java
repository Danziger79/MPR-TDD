package model;

import java.util.Objects;


public class Employee {
    private String name;
    private String email;
    private String companyName;
    private Position position;
    private double salary;


    public Employee(String name, String email, String companyName, Position position, double salary) {

        this.name = name;
        this.email = email;
        this.companyName = companyName;
        this.position = position;
        this.salary = salary;
    }


    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCompanyName() { return companyName; }
    public Position getPosition() { return position; }
    public double getSalary() { return salary; }


    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPosition(Position position) {
        this.position = position;
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
