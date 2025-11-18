package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import exception.ApiException;
import model.Employee;
import model.Position;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

// Serwis odpowiedzialny za komunikację z zewnętrznym REST API.
// Obsługuje wysyłanie żądań HTTP oraz deserializację odpowiedzi JSON na obiekty modelu wewnętrznego.
public class ApiService {

    private final HttpClient client;
    private final Gson gson;

    public ApiService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Pobiera listę użytkowników z zewnętrznego serwisu i konwertuje ją na listę pracowników.
     * Metoda wykonuje synchroniczne zapytanie HTTP GET, parsuje odpowiedź i mapuje wybrane pola.
     *
     * @return Lista obiektów Employee utworzona na podstawie danych z API.
     * @throws ApiException W przypadku błędu sieciowego, błędu HTTP (status inny niż 200) lub błędu parsowania JSON.
     */
    public List<Employee> fetchEmployeesFromApi() throws ApiException {
        List<Employee> apiEmployees = new ArrayList<>();

        // Konstrukcja żądania HTTP GET skierowanego do endpointu z danymi użytkowników.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET()
                .build();

        try {
            // Wysłanie żądania i oczekiwanie na odpowiedź w formacie tekstowym (String).
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Weryfikacja kodu statusu HTTP. Oczekiwany kod 200 (OK).
            if (response.statusCode() != 200) {
                throw new ApiException("Błąd zapytania API. Nieoczekiwany status: " + response.statusCode());
            }

            // Deserializacja ciała odpowiedzi JSON do obiektu JsonArray przy użyciu biblioteki GSON.
            String jsonBody = response.body();
            JsonArray usersArray = gson.fromJson(jsonBody, JsonArray.class);

            // Iteracja po elementach tablicy JSON i mapowanie pól na obiekt domenowy Employee.
            for (JsonElement userElement : usersArray) {
                JsonObject userObject = userElement.getAsJsonObject();

                // Ekstrakcja podstawowych danych tekstowych z obiektu JSON.
                String fullName = userObject.get("name").getAsString();
                String email = userObject.get("email").getAsString();

                // Nawigacja do zagnieżdżonego obiektu 'company' w celu pobrania nazwy firmy.
                String companyName = userObject.get("company")
                        .getAsJsonObject()
                        .get("name")
                        .getAsString();

                // Przypisanie domyślnego stanowiska i wynagrodzenia dla pracowników importowanych z API.
                Position position = Position.PROGRAMISTA;
                double salary = position.getBaseSalary();

                apiEmployees.add(new Employee(fullName, email, companyName, position, salary));
            }

        } catch (IOException | InterruptedException e) {
            // Obsługa błędów warstwy transportowej lub przerwania wątku.
            // Wyjątek jest opakowywany w ApiException w celu zachowania spójności interfejsu.
            throw new ApiException("Błąd połączenia z API: " + e.getMessage(), e);
        } catch (JsonSyntaxException e) {
            // Obsługa błędów wynikających z nieprawidłowej struktury otrzymanego JSON-a.
            throw new ApiException("Błąd parsowania odpowiedzi JSON: " + e.getMessage(), e);
        }

        return apiEmployees;
    }
}