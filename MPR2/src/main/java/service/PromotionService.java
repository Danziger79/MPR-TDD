package service;

import model.Employee;
import model.Position;


public class PromotionService {


    public void promote(Employee employee, Position newPosition) {

        // Test sprawdza czy stanowisko się zmieniło
        employee.setPosition(newPosition);

        // Test sprawdza czy pensja to nowa baza
        employee.setSalary(newPosition.getBaseSalary());
    }
}