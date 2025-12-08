package interfaces;

import model.Employee;

public interface TaskRepository {
    void saveAssignment(long taskId, Employee employee);
}