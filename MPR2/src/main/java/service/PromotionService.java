package service;

import model.Employee;
import model.Position;

public class PromotionService {

    public void promote(Employee employee, Position newPosition) {


        int oldLevel = employee.getPosition().getHierarchyLevel();
        int newLevel = newPosition.getHierarchyLevel();


        if (newLevel >= oldLevel) {

            throw new IllegalArgumentException("Awans musi być na wyższe stanowisko");
        }



        employee.setPosition(newPosition);
        employee.setSalary(newPosition.getBaseSalary());
    }

    public void giveRaise(Employee employee, double percentage) {


        if (percentage < 0) {
            throw new IllegalArgumentException("Podwyżka nie może być ujemna");
        }


        double currentSalary = employee.getSalary();
        double raiseAmount = currentSalary * (percentage / 100.0);
        double newSalary = currentSalary + raiseAmount;


        double maxSalary = employee.getPosition().getMaxSalary();
        if (newSalary > maxSalary) {
            newSalary = maxSalary;
        }


        employee.setSalary(newSalary);
    }
}