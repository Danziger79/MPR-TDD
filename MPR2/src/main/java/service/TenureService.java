package service;

import model.Employee;
import java.time.LocalDate;
import java.time.Period;

// Serwis domeny HR odpowiedzialny za wyliczanie stażu pracy (seniority) pracowników.
// Dostarcza funkcjonalności związane z analizą czasu trwania zatrudnienia w oparciu o daty.
public class TenureService {

    /**
     * Oblicza staż pracy pracownika w pełnych latach na dzień wskazanej daty odniesienia.
     * Metoda wykorzystuje API java.time do precyzyjnych obliczeń kalendarzowych.
     *
     * @param employee Pracownik, którego staż jest obliczany.
     * @param currentDate Data odniesienia (np. dzień dzisiejszy lub data generowania raportu).
     * @return Liczba pełnych lat przepracowanych przez pracownika. Zwraca 0, jeśli data zatrudnienia jest późniejsza niż data odniesienia.
     */
    public long getTenureInYears(Employee employee, LocalDate currentDate) {

        // Weryfikacja chronologii dat.
        // Sytuacja, w której data zatrudnienia następuje po dacie odniesienia (np. planowane zatrudnienie w przyszłości),
        // jest traktowana jako brak stażu (0 lat), co zapobiega błędom logicznym i wartościom ujemnym.
        if (employee.getDateOfHire().isAfter(currentDate)) {
            return 0;
        }

        // Obliczenie okresu czasu (Period) między dwiema datami zgodnie z systemem kalendarzowym ISO-8601.
        // Klasa Period uwzględnia lata przestępne oraz różną długość miesięcy.
        Period period = Period.between(employee.getDateOfHire(), currentDate);

        // Ekstrakcja i zwrócenie liczby pełnych lat z obliczonego okresu.
        return period.getYears();
    }
}