package service;

import model.Employee;

import java.util.List;


public class RatingService {


    public void addRating(Employee employee, int rating) {
        employee.addRating(rating);
    }
    public double getAverageRating(Employee employee) {

        List<Integer> ratings = employee.getRatingHistory();


        double sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }

        // Dzielimy (12 / 3 = 4.0)
        return sum / ratings.size();
    }
}