package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Klasa testowa weryfikująca poprawność implementacji metod domenowych klasy Employee.
// Główny nacisk położony jest na weryfikację kontraktu tożsamości obiektów (equals oraz hashCode).
class EmployeeTest {

    @Test
    void testEquals_SameEmail_ShouldBeEqual() {
        // Given: Dwa obiekty o różnym stanie atrybutów (imię, firma, stanowisko),
        // ale posiadające ten sam unikalny identyfikator biznesowy (adres email).
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Employee e2 = new Employee("Jan Nowak", "jan@kowalski.pl", "FirmaB", Position.MANAGER, 10000);

        // Then: Obiekty powinny zostać uznane za tożsame.
        assertEquals(e1, e2, "Obiekty z tym samym identyfikatorem (email) muszą być uznawane za równe, niezależnie od pozostałych pól.");
    }

    @Test
    void testEquals_DifferentEmail_ShouldNotBeEqual() {
        // Given: Dwa obiekty posiadające identyczne dane osobowe i stanowiskowe,
        // ale różniące się adresem email.
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Employee e2 = new Employee("Jan Kowalski", "jan@nowak.pl", "FirmaA", Position.PROGRAMISTA, 5000);

        // Then: Obiekty muszą zostać uznane za różne.
        assertNotEquals(e1, e2, "Różne adresy email muszą skutkować nierównością obiektów.");
    }

    @Test
    void testEquals_DifferentObjectType_ShouldReturnFalse() {
        // Given: Obiekt klasy Employee oraz obiekt innej klasy (Object).
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Object o = new Object();

        // Then: Porównanie obiektów niekompatybilnych typów musi zwrócić false (bezpieczeństwo typów).
        assertFalse(e1.equals(o), "Metoda equals powinna zwracać false przy próbie porównania z obiektem innej klasy.");
    }

    @Test
    void testHashCode_SameEmail_ShouldBeEqual() {
        // Given: Dwa obiekty logicznie równe (ten sam email).
        Employee e1 = new Employee("Jan Kowalski", "jan@kowalski.pl", "FirmaA", Position.PROGRAMISTA, 5000);
        Employee e2 = new Employee("Anna Nowak", "jan@kowalski.pl", "FirmaB", Position.MANAGER, 10000);

        // Then: Zgodnie z kontraktem Java, równe obiekty muszą zwracać ten sam kod skrótu (hash code).
        assertEquals(e1.hashCode(), e2.hashCode(), "HashCode dla obiektów równych (wg equals) musi być identyczny.");
    }
}