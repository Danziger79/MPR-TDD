package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

// Importy statyczne dla biblioteki AssertJ (płynne asercje)
import static org.assertj.core.api.Assertions.*;

// Importy statyczne dla biblioteki Hamcrest (dopasowania matcherów)
import static org.hamcrest.Matchers.*;

// Klasa testowa weryfikująca logikę biznesową awansów i podwyżek (PromotionService).
// Wykorzystuje JUnit 5 oraz dwie różne biblioteki asercji (AssertJ i Hamcrest) w celu demonstracji różnych podejść weryfikacyjnych.
class PromotionServiceTest {

    private PromotionService promotionService;
    private Employee stazysta;
    private Employee manager;

    // Inicjalizacja środowiska testowego.
    // Tworzy nową instancję serwisu oraz obiekty pracowników w stanach początkowych przed każdym testem.
    @BeforeEach
    void setUp() {
        promotionService = new PromotionService();
        stazysta = new Employee("Jan Stażysta", "jan@tech.pl", "TechCorp",
                Position.STAZYSTA, 3500);
        manager = new Employee("Anna Manager", "anna@tech.pl", "TechCorp",
                Position.MANAGER, 13000);
    }

    @Test
    @DisplayName("Awans ze Stażysty na Programistę powinien zaktualizować stanowisko i zresetować pensję do bazy")
    void shouldPromoteEmployeeAndSetBaseSalary() {
        // Act
        promotionService.promote(stazysta, Position.PROGRAMISTA);

        // Assert (AssertJ)
        // Weryfikacja zmiany stanu obiektu: stanowisko musi ulec zmianie, a pensja przyjąć wartość bazową dla nowej roli.
        assertThat(stazysta.getPosition())
                .as("Stanowisko powinno zostać zmienione na PROGRAMISTA")
                .isSameAs(Position.PROGRAMISTA);

        assertThat(stazysta.getSalary())
                .as("Wynagrodzenie powinno zostać zresetowane do stawki bazowej programisty")
                .isEqualTo(Position.PROGRAMISTA.getBaseSalary());
    }

    @Test
    @DisplayName("Próba awansu na to samo lub niższe stanowisko powinna skutkować wyjątkiem")
    void shouldThrowExceptionWhenPromotingToSameOrLowerRank() {
        // Assert (AssertJ)
        // Weryfikacja reguły biznesowej zabraniającej degradacji (Programista < Manager).
        assertThatIllegalArgumentException()
                .as("Degradacja pracownika powinna rzucić wyjątek")
                .isThrownBy(() -> promotionService.promote(manager, Position.PROGRAMISTA));

        // Weryfikacja reguły zabraniającej awansu poziomego (Manager -> Manager).
        assertThatIllegalArgumentException()
                .as("Awans na to samo stanowisko powinien rzucić wyjątek")
                .isThrownBy(() -> promotionService.promote(manager, Position.MANAGER));
    }

    /**
     * Test sparametryzowany weryfikujący poprawność obliczania podwyżek procentowych.
     * Wykorzystuje adnotację @CsvSource do dostarczenia wielu zestawów danych testowych.
     * * @param initialSalary Pensja początkowa.
     * @param percentage Procent podwyżki.
     * @param expectedSalary Oczekiwana pensja końcowa.
     */
    @ParameterizedTest(name = "Podwyżka {1}% dla pensji {0} powinna skutkować kwotą {2}")
    @CsvSource({
            "10000, 10, 11000.00",  // Standardowa podwyżka
            "12000, 5.5, 12660.00", // Podwyżka ułamkowa
            "5000, 0, 5000.00"      // Brak podwyżki (0%)
    })
    void shouldGivePercentageRaiseCorrectly(double initialSalary, double percentage, double expectedSalary) {
        // Arrange
        manager.setSalary(initialSalary);

        // Act
        promotionService.giveRaise(manager, percentage);

        // Assert (Hamcrest)
        // Użycie matchera 'closeTo' dla wartości zmiennoprzecinkowych w celu uniknięcia błędów zaokrągleń.
        org.hamcrest.MatcherAssert.assertThat(manager.getSalary(), is(closeTo(expectedSalary, 0.01)));
    }

    @Test
    @DisplayName("Podwyżka nie powinna przekroczyć maksymalnych widełek płacowych dla danego stanowiska")
    void shouldCapRaiseAtMaxSalaryForPosition() {
        // Arrange
        // Manager ma max salary 19000. Ustawiamy 18000 i dajemy 20% podwyżki (co dałoby 21600).
        manager.setSalary(18000);

        // Act
        promotionService.giveRaise(manager, 20.0);

        // Assert (Hamcrest)
        // Sprawdzenie, czy mechanizm "cappingu" zadziałał i obciął pensję do maksimum.
        org.hamcrest.MatcherAssert.assertThat("Pensja powinna zostać ograniczona do górnego limitu widełek",
                manager.getSalary(),
                is(equalTo(Position.MANAGER.getMaxSalary())));
    }

    @Test
    @DisplayName("Próba udzielenia ujemnej podwyżki powinna rzucić wyjątek walidacji")
    void shouldThrowExceptionForNegativeRaise() {
        // Assert (AssertJ)
        assertThatThrownBy(() -> {
            promotionService.giveRaise(manager, -5.0);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Podwyżka nie może być ujemna");
    }

    /**
     * Test sparametryzowany wykorzystujący @EnumSource.
     * Sprawdza, czy system blokuje awanse na stanowiska o niższym lub równym poziomie hierarchii
     * dla pracownika na stanowisku MANAGER (poziom 3).
     * * Testowane przypadki: MANAGER (równy), PROGRAMISTA (niższy), STAZYSTA (niższy).
     */
    @ParameterizedTest(name = "Awans z Managera na {0} powinien być zablokowany")
    @EnumSource(value = Position.class, names = {"MANAGER", "PROGRAMISTA", "STAZYSTA"})
    void shouldBlockPromotionToLowerOrSameRanks(Position lowerOrSamePosition) {
        // Assert (AssertJ)
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    promotionService.promote(manager, lowerOrSamePosition);
                })
                .withMessageContaining("Awans musi być na wyższe stanowisko");
    }
}