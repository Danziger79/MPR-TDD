package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

class PromotionServiceTest {

    private PromotionService promotionService;
    private Employee stazysta;

    @BeforeEach
    void setUp() {
        // Jeszcze nie ma tej klasy
        promotionService = new PromotionService();
        stazysta = new Employee("Jan Stażysta", "jan@tech.pl", "TechCorp",
                Position.STAZYSTA, 3500);
    }

    @Test
    @DisplayName("Awans ze Stażysty na Programistę powinien ustawić nowe stanowisko i bazową pensję")
    void shouldPromoteEmployeeAndSetBaseSalary() {
        // Arrange (mamy w setup)

        // Act
        // Ta metoda jeszcze nie istnieje
        promotionService.promote(stazysta, Position.PROGRAMISTA);

        // Assert (Używamy AssertJ)


        assertThat(stazysta.getPosition())
                .as("Sprawdzenie, czy stanowisko się zmieniło")
                .isSameAs(Position.PROGRAMISTA); // Asercja AssertJ

        assertThat(stazysta.getSalary())
                .as("Sprawdzenie, czy pensja to nowa baza")
                .isEqualTo(Position.PROGRAMISTA.getBaseSalary()); // Asercja AssertJ
    }
}