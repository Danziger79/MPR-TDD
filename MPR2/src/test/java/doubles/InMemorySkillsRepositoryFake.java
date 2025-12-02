package doubles;

import interfaces.SkillsRepository;
import model.Employee;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Rola: Implementacja wzorca Test Double typu "Fake".
// Opis: Klasa dostarcza w pełni funkcjonalną, ale uproszczoną implementację repozytorium działającą w pamięci RAM (in-memory).
// Symuluje zachowanie prawdziwej bazy danych (trwałość danych w ramach cyklu życia obiektu),
// co pozwala na testowanie logiki zależnej od stanu (State-Based Testing) bez narzutu IO i konfiguracji zewnętrznej DB.
public class InMemorySkillsRepositoryFake implements SkillsRepository {

    // Wewnętrzna struktura danych symulująca trwałą pamięć masową (bazę danych).
    // Mapa odwzorowuje relację: Pracownik -> Zbiór unikalnych umiejętności.
    private final Map<Employee, Set<String>> storage = new HashMap<>();

    // Implementacja metody odczytu danych (odpowiednik operacji SELECT).
    // Zwraca zbiór umiejętności dla danego pracownika. Zastosowanie getOrDefault gwarantuje zwrócenie
    // pustego zbioru (zamiast null) dla braku danych, co upraszcza obsługę po stronie klienta.
    @Override
    public Set<String> getSkills(Employee employee) {
        return storage.getOrDefault(employee, new HashSet<>());
    }

    // Implementacja metody zapisu (odpowiednik operacji INSERT/UPDATE).
    // Dodaje nową umiejętność do "bazy". Wykorzystanie computeIfAbsent zapewnia atomowość operacji
    // inicjalizacji zbioru dla nowego pracownika, jeśli ten jeszcze nie istnieje w rejestrze.
    @Override
    public void addSkill(Employee employee, String skill) {
        storage.computeIfAbsent(employee, k -> new HashSet<>()).add(skill);
    }
}