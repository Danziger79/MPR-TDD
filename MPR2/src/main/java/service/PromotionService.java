package service;

import model.Employee;
import model.Position;

// Serwis domenowy odpowiedzialny za zarządzanie ścieżką kariery i wynagrodzeniami pracowników.
// Obsługuje procesy awansów pionowych oraz rewaloryzacji wynagrodzeń z uwzględnieniem polityki płacowej firmy.
public class PromotionService {

    /**
     * Przeprowadza procedurę awansu pracownika na nowe stanowisko.
     * Weryfikuje, czy zmiana stanowiska jest faktycznym awansem w strukturze organizacyjnej
     * (zgodnie z zasadą: niższy numer poziomu = wyższa ranga).
     *
     * @param employee Pracownik podlegający awansowi.
     * @param newPosition Docelowe stanowisko.
     * @throws IllegalArgumentException Jeśli nowe stanowisko jest na tym samym lub niższym poziomie hierarchii (democja/poziomo).
     */
    public void promote(Employee employee, Position newPosition) {
        int oldLevel = employee.getPosition().getHierarchyLevel();
        int newLevel = newPosition.getHierarchyLevel();

        // Blokada degradacji lub przesunięć poziomych.
        // W tym modelu biznesowym poziom 1 (Prezes) jest najwyższy, a 5 (Stażysta) najniższy.
        // Zatem awans następuje tylko wtedy, gdy nowa wartość (newLevel) jest mniejsza od starej.
        if (newLevel >= oldLevel) {
            throw new IllegalArgumentException("Awans musi być na wyższe stanowisko");
        }

        // Aktualizacja stanowiska oraz reset wynagrodzenia do stawki bazowej nowej roli.
        employee.setPosition(newPosition);
        employee.setSalary(newPosition.getBaseSalary());
    }

    /**
     * Aplikuje procentową podwyżkę wynagrodzenia dla pracownika.
     * Metoda pilnuje, aby nowa pensja nie przekroczyła maksymalnych widełek (MaxSalary) przewidzianych dla danego stanowiska.
     *
     * @param employee Pracownik otrzymujący podwyżkę.
     * @param percentage Wartość podwyżki w procentach (np. 10.0 dla 10%).
     * @throws IllegalArgumentException W przypadku podania ujemnej wartości procentowej.
     */
    public void giveRaise(Employee employee, double percentage) {
        if (percentage < 0) {
            throw new IllegalArgumentException("Podwyżka nie może być ujemna");
        }

        double currentSalary = employee.getSalary();
        double raiseAmount = currentSalary * (percentage / 100.0);
        double newSalary = currentSalary + raiseAmount;

        // Walidacja Business Rules: "Capping" wynagrodzenia.
        // Jeśli wyliczona kwota przekracza górne widełki dla stanowiska,
        // pensja zostaje spłaszczona do poziomu maksymalnego.
        double maxSalary = employee.getPosition().getMaxSalary();
        if (newSalary > maxSalary) {
            newSalary = maxSalary;
        }

        employee.setSalary(newSalary);
    }
}