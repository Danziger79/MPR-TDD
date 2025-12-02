package service;

import interfaces.CalendarService;
import interfaces.SkillsRepository;
import interfaces.SystemConfig;
import model.Employee;
import java.util.List;

// Rola: Serwis domenowy (Business Service) odpowiedzialny za logikę przydzielania zadań.
// Opis: Klasa pełni rolę koordynatora, który integruje dane o dostępności (CalendarService)
// z danymi o kompetencjach (SkillsRepository) w celu znalezienia odpowiedniego pracownika.
// Jest to główny obiekt testowany (SUT - System Under Test) w scenariuszach wykorzystujących
// wzorce Test Doubles (Stub, Fake, Dummy).
public class TaskAssignmentService {

    private final CalendarService calendarService;
    private final SkillsRepository skillsRepository;

    // Zależność konfiguracyjna. W kontekście metody assignTask nie jest bezpośrednio używana,
    // co czyni ją idealnym kandydatem do zastosowania wzorca Dummy w testach.
    private final SystemConfig config;

    // Konstruktor realizujący wstrzykiwanie zależności (Dependency Injection).
    // Pozwala na przekazanie zarówno rzeczywistych implementacji, jak i obiektów testowych.
    public TaskAssignmentService(CalendarService calendar, SkillsRepository skills, SystemConfig config) {
        this.calendarService = calendar;
        this.skillsRepository = skills;
        this.config = config;
    }

    /**
     * Próbuje przypisać zadanie do pierwszego dostępnego pracownika posiadającego wymagane kompetencje.
     *
     * @param taskId Identyfikator zadania (kontekst dla sprawdzenia dostępności).
     * @param requiredSkill Nazwa wymaganej umiejętności.
     * @return true, jeśli udało się znaleźć i przypisać pracownika; false w przeciwnym razie.
     */
    public boolean assignTask(long taskId, String requiredSkill) {
        // Krok 1: Pobranie listy dostępnych kandydatów (Interakcja ze Stubem w teście).
        List<Employee> available = calendarService.getAvailableEmployees(taskId);

        for (Employee emp : available) {
            // Krok 2: Weryfikacja czy kandydat posiada wymaganą umiejętność (Interakcja z Fake'iem w teście).
            // Metoda sprawdza stan w repozytorium umiejętności.
            if (skillsRepository.getSkills(emp).contains(requiredSkill)) {
                return true; // Sukces: Znaleziono pasującego pracownika.
            }
        }

        // Porażka: Żaden z dostępnych pracowników nie spełnia kryteriów kompetencyjnych.
        return false;
    }
}