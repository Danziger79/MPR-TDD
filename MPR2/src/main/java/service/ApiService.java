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

// Serwis do gadania z zewnętrznym API
public class ApiService {

    // Klient HTTP i parser GSON.
    // Tworzymy je raz i używamy wielokrotnie.
    private final HttpClient client;
    private final Gson gson;

    public ApiService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Pobiera użytkowników z API i mapuje ich na naszą klasę Employee.
     * Rzuca nasz własny wyjątek 'ApiException', jeśli coś pójdzie nie tak.
     */
    public List<Employee> fetchEmployeesFromApi() throws ApiException {
        List<Employee> apiEmployees = new ArrayList<>();

        // 1. Zbuduj zapytanie (Request) - dokładnie jak na wykładzie
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET() // Mówimy, że to metoda GET
                .build();

        try {
            // 2. Wyślij zapytanie i pobierz odpowiedź (Response)
            // Mówimy, że spodziewamy się odpowiedzi jako String (JSON)
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 3. Sprawdź status odpowiedzi
            if (response.statusCode() != 200) {
                // Jeśli serwer nie odpowiedział "OK" (kod 200), to jest błąd
                throw new ApiException("Błąd zapytania API. Status: " + response.statusCode());
            }

            // 4. Parsowanie JSON-a (tu wchodzi GSON)
            String jsonBody = response.body();

            // Parsujemy cały string odpowiedzi na 'JsonArray' (bo API zwraca listę)
            JsonArray usersArray = gson.fromJson(jsonBody, JsonArray.class);

            // 5. Iterujemy po tablicy JSON
            for (JsonElement userElement : usersArray) {
                // Każdy element tablicy to obiekt, więc rzutujemy go
                JsonObject userObject = userElement.getAsJsonObject();

                // Wyciągamy dane z JSON-a, pole po polu
                // Dokładnie tak, jak na wykładzie: .get("nazwaPola").getAsTyp()
                String fullName = userObject.get("name").getAsString();
                String email = userObject.get("email").getAsString();

                // Pole "company.name" jest zagnieżdżone, więc musimy
                // najpierw pobrać obiekt "company", a dopiero z niego "name"
                String companyName = userObject.get("company")
                        .getAsJsonObject()
                        .get("name")
                        .getAsString();

                // Zgodnie z zadaniem, sztywne przypisanie stanowiska i pensji
                Position position = Position.PROGRAMISTA;
                double salary = position.getBaseSalary(); // Bierzemy bazową stawkę

                // Tworzymy pracownika i dodajemy do listy
                apiEmployees.add(new Employee(fullName, email, companyName, position, salary));
            }

        } catch (IOException | InterruptedException e) {
            // To są błędy związane z siecią (np. brak neta)
            throw new ApiException("Błąd połączenia z API: " + e.getMessage(), e);
        } catch (JsonSyntaxException e) {
            // To jest błąd, jeśli API zwróci nam "nie-JSONa"
            throw new ApiException("Błąd parsowania odpowiedzi JSON: " + e.getMessage(), e);
        }

        return apiEmployees; // Zwracamy listę pobranych pracowników
    }
}