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
}