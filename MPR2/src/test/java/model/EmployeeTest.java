package model;

import org.junit.jupiter.api.Test;

// Importujemy statycznie *wszystkie* metody z Assertions,
// żeby pisać assertEquals() zamiast Assertions.assertEquals()
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    // Jak w wykładzie: nazwy testów opisują, co robimy
    @Test
    void testEquals_SameEmail_ShouldBeEqual() {
        // Arrange (Przygotuj)
        // Dwóch różnych pracowników, ale ten sam email
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Employee e2 = new Employee("Jan Nowak", "jan@kowalski.pl", "FirmaB", Position.MANAGER, 10000);

        // Act & Assert (Działaj i Sprawdź)
        // Metoda equals() powinna patrzeć TYLKO na email
        assertEquals(e1, e2, "Pracownicy z tym samym adresem email powinni być równi");
    }

    @Test
    void testEquals_DifferentEmail_ShouldNotBeEqual() {
        // Arrange
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Employee e2 = new Employee("Jan Kowalski", "jan@nowak.pl", "FirmaA", Position.PROGRAMISTA, 5000);

        // Act & Assert
        assertNotEquals(e1, e2, "Pracownicy z różnymi adresami email nie powinni być równi");
    }

    @Test
    void testEquals_DifferentObjectType_ShouldReturnFalse() {
        // Arrange
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Object o = new Object(); // Inny typ obiektu

        // Act & Assert
        // Używamy assertFalse, bo metoda equals powinna zwrócić 'false'
        assertFalse(e1.equals(o), "Porównanie Employee z Object powinno zwrócić false");
    }

    @Test
    void testHashCode_SameEmail_ShouldBeEqual() {
        // Arrange
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Employee e2 = new Employee("Anna Nowak", "jan@kowalski.pl", "FirmaB", Position.MANAGER, 10000);

        // Act & Assert
        // Kontrakt między equals i hashCode: jak equals() jest true,
        // to hashCode() MUSI być taki sam.
        assertEquals(e1.hashCode(), e2.hashCode(), "HashCode dla pracowników z tym samym emailem musi być identyczny");
    }
}