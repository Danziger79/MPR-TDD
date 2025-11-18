package service;

import model.Employee;

import java.util.List;

// Serwis odpowiedzialny za system oceniania efektywności pracowników.
// Dostarcza interfejs do manipulacji historią ocen oraz wyliczania metryk statystycznych.
public class RatingService {

    // Rejestruje nową notę w historii pracownika.
    // Logika walidacji wartości oraz zapisu jest delegowana bezpośrednio do metody domenowej w klasie Employee.
    public void addRating(Employee employee, int rating) {
        employee.addRating(rating);
    }

    /**
     * Oblicza średnią arytmetyczną ze wszystkich ocen zgromadzonych w historii pracownika.
     *
     * @param employee Pracownik, dla którego obliczana jest statystyka.
     * @return Średnia ocen jako wartość zmiennoprzecinkowa. Zwraca 0.0 w przypadku braku jakichkolwiek ocen.
     */
    public double getAverageRating(Employee employee) {

        List<Integer> ratings = employee.getRatingHistory();

        // Obsługa przypadku brzegowego: pusta historia ocen.
        // Zwracamy wartość domyślną 0.0, aby uniknąć wyjątku dzielenia przez zero lub zwracania NaN.
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }

        // Wyliczenie średniej poprzez podzielenie sumy punktów przez liczebność zbioru.
        return sum / ratings.size();
    }
}