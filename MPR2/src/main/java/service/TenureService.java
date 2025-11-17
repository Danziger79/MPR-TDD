package service;

import model.Employee;
import java.time.LocalDate;
import java.time.Period;


public class TenureService {


    public long getTenureInYears(Employee employee, LocalDate currentDate) {

        if (employee.getDateOfHire().isAfter(currentDate)) {
            return 0; // Test oczekiwał 0
        }

        Period period = Period.between(employee.getDateOfHire(), currentDate);


        return period.getYears();
    }

}