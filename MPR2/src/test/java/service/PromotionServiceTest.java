package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;



class PromotionServiceTest {

    private PromotionService promotionService;
    private Employee stazysta;
    private Employee manager;

    @BeforeEach
    void setUp() {
        promotionService = new PromotionService();
        stazysta = new Employee("Jan Stażysta", "jan@tech.pl", "TechCorp",
                Position.STAZYSTA, 3500);
        manager = new Employee("Anna Manager", "anna@tech.pl", "TechCorp",
                Position.MANAGER, 13000);
    }


    @Test
    @DisplayName("Awans ze Stażysty na Programistę powinien ustawić nowe stanowisko i bazową pensję")
    void shouldPromoteEmployeeAndSetBaseSalary() {
        // Act
        promotionService.promote(stazysta, Position.PROGRAMISTA);

        // Assert
        assertThat(stazysta.getPosition())
                .as("Sprawdzenie, czy stanowisko się zmieniło")
                .isSameAs(Position.PROGRAMISTA);

        assertThat(stazysta.getSalary())
                .as("Sprawdzenie, czy pensja to nowa baza")
                .isEqualTo(Position.PROGRAMISTA.getBaseSalary());
    }


    @Test
    @DisplayName("Awans na to samo lub niższe stanowisko powinien rzucić wyjątek")
    void shouldThrowExceptionWhenPromotingToSameOrLowerRank() {
        // Scenariusz 1: To samo stanowisko
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    promotionService.promote(stazysta, Position.STAZYSTA);
                })
                .withMessageContaining("Awans musi być na wyższe stanowisko");

        // Scenariusz 2: Niższe stanowisko (z Managera na Programistę)
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    promotionService.promote(manager, Position.PROGRAMISTA);
                })
                .withMessageContaining("Awans musi być na wyższe stanowisko");
    }


    @ParameterizedTest(name = "Podwyżka {1}% dla pensji {0} daje {2}")
    @CsvSource({
            "10000, 10, 11000.00",  // Standardowa podwyżka
            "12000, 5.5, 12660.00",  // Podwyżka z ułamkiem
            "5000, 0, 5000.00"      // Zerowa podwyżka
    })
    void shouldGivePercentageRaiseCorrectly(double initialSalary, double percentage, double expectedSalary) {
        // Arrange
        manager.setSalary(initialSalary);


        promotionService.giveRaise(manager, percentage);


        assertThat(manager.getSalary(), is(closeTo(expectedSalary, 0.01)));
    }
    @Test
    @DisplayName("Podwyżka nie powinna przekroczyć max pensji dla stanowiska")
    void shouldCapRaiseAtMaxSalaryForPosition() {
        // Arrange
        // Manager zarabia 18000. Max na MANAGER to 19000.
        manager.setSalary(18000);

        // Act

        // Nasz obecny kod to zrobi, co jest błędem.
        promotionService.giveRaise(manager, 20.0);



        assertThat("Pensja powinna być ograniczona do max",
                manager.getSalary(),
                is(equalTo(Position.MANAGER.getMaxSalary())));
    }


    @Test
    @DisplayName("Podwyżka ujemna powinna rzucić wyjątek")
    void shouldThrowExceptionForNegativeRaise() {

        assertThatThrownBy(() -> {
            promotionService.giveRaise(manager, -5.0);
        })
                .isInstanceOf(IllegalArgumentException.class) // Sprawdzamy typ wyjątku
                .hasMessageContaining("Podwyżka nie może być ujemna"); // Sprawdzamy treść
    }
}