package service;

import model.Employee;


public class RatingService {


    public void addRating(Employee employee, int rating) {
        employee.addRating(rating);
    }
}