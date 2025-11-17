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
    private Employee manager; // <-- DODAJ TO POLE

    @BeforeEach
    void setUp() {
        promotionService = new PromotionService();
        stazysta = new Employee("Jan Stażysta", "jan@tech.pl", "TechCorp",
                Position.STAZYSTA, 3500);
        // DODAJ INICJALIZACJĘ MANAGERA
        manager = new Employee("Anna Manager", "anna@tech.pl", "TechCorp",
                Position.MANAGER, 13000);
    }


    @Test
    @DisplayName("Awans na to samo lub niższe stanowisko powinien rzucić wyjątek")
    void shouldThrowExceptionWhenPromotingToSameOrLowerRank() {
        // Assert (AssertJ do sprawdzania wyjątków)

        // Scenariusz 1: To samo stanowisko
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // blad
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
}