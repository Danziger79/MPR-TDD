package interfaces;

import model.Employee;
import java.util.List;

public interface CertificateRepository {
    List<Employee> findEmployeesWithExpiringCertificates(int days);
}