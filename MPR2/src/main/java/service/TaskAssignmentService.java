package service;

import interfaces.CalendarService;
import interfaces.SkillsRepository;
import interfaces.TaskRepository;
import model.Employee;

public class TaskAssignmentService {

    private final CalendarService calendarService;
    private final SkillsRepository skillsRepository;
    // Nowa zależność - tu będziemy zapisywać przydział (do weryfikacji w Mockito)
    private final TaskRepository taskRepository;

    // Konstruktor (SystemConfig usunięty, bo nie jest już potrzebny)
    public TaskAssignmentService(CalendarService calendarService,
                                 SkillsRepository skillsRepository,
                                 TaskRepository taskRepository) {
        this.calendarService = calendarService;
        this.skillsRepository = skillsRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Przydziela konkretnego pracownika do zadania, jeśli spełnia warunki.
     */
    public boolean assignTask(long taskId, Employee employee, String requiredSkill) {
        // 1. Sprawdź dostępność (Stubbing w teście: when(...).thenReturn(...))
        // Sprawdzamy, czy lista dostępnych pracowników zawiera naszego kandydata
        boolean isAvailable = calendarService.getAvailableEmployees(taskId).contains(employee);

        // 2. Sprawdź kompetencje (Stubbing w teście)
        boolean hasSkill = skillsRepository.getSkills(employee).contains(requiredSkill);

        if (isAvailable && hasSkill) {
            // 3. Zapisz przydział (Verify w teście: verify(taskRepository).saveAssignment(...))
            taskRepository.saveAssignment(taskId, employee);
            return true;
        }

        return false;
    }
}