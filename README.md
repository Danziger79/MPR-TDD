# Projekt TDD - System Zarządzania Pracownikami

## Opis Procesu TDD (Moduł 1: Awanse i Podwyżki)


### Krok 0: Setup
Zanim rozpoczęto TDD, modele zostały przygotowane do obsługi nowej logiki:
* `model/Position.java`: Dodano pola `hierarchyLevel` (do walidacji ścieżki awansu) i `maxSalary` (do walidacji podwyżek).
* `model/Employee.java`: Dodano settery `setPosition()` i `setSalary()`.
* **Commit:** `FEATURE: Przygotowanie modeli pod moduł awansów...`

---

### Cykl TDD #1: Podstawowy awans

* **Faza RED:**
    * **Cel:** Pracownik po awansie powinien mieć nowe stanowisko i nową (bazową) pensję.
    * **Test:** Stworzono plik `PromotionServiceTest.java` z testem `shouldPromoteEmployeeAndSetBaseSalary()`.
    * **Wynik:** BŁĄD KOMPILACJI. Klasa `PromotionService` i metoda `promote` nie istniały.
    * **Commit:** `faza RED awanse i podwyżki`

* **Faza GREEN:**
    * **Cel:** Sprawić, by test przeszedł jak najmniejszym kosztem.
    * **Kod:** Stworzono klasę `PromotionService` z metodą `promote(Employee, Position)`, która po prostu ustawiała nowe stanowisko i nową pensję bazową.
    * **Wynik:** TEST PRZESZEDŁ.
    * **Commit:** `faza GREEN awanse i podwyżki`

* **Faza REFACTOR:**
    * Kod był minimalny i czysty. Nie było potrzeby refaktoryzacji.

---

### Cykl TDD #2: Walidacja hierarchii awansu

* **Faza RED:**
    * **Cel:** System nie może pozwolić na degradację (np. z Managera na Programistę).
    * **Test:** Dodano test `shouldThrowExceptionWhenPromotingToSameOrLowerRank()`, który używał `assertThatIllegalArgumentException()` z AssertJ do sprawdzenia, czy metoda `promote` rzuci wyjątkiem.
    * **Wynik:** TEST NIE PRZESZEDŁ (oczekiwano wyjątku, którego nie było).
    * **Commit:** `faza RED walidacja awansu`

* **Faza GREEN:**
    * **Cel:** Implementacja walidacji.
    * **Kod:** Do metody `promote` dodano `if (newLevel >= oldLevel)` rzucający `IllegalArgumentException`.
    * **Wynik:** WSZYSTKIE TESTY PRZESZŁY.
    * **Commit:** `faza GREEN walidacja awansu`

* **Faza REFACTOR:**
    * Kod był czysty (prosty `if` na górze metody). Brak refaktoryzacji.

---

### Cykl TDD #3: Podstawowa podwyżka (Test Parametryczny)

* **Faza RED:**
    * **Cel:** System ma poprawnie naliczać podwyżki procentowe.
    * **Test:** Dodano test parametryczny `@ParameterizedTest` z `@CsvSource` o nazwie `shouldGivePercentageRaiseCorrectly`. Test używał matchera `closeTo` z Hamcresta do weryfikacji obliczeń `double`.
    * **Wynik:** BŁĄD KOMPILACJI (metoda `giveRaise` nie istniała).
    * **Commit:** `faza RED podstawowa podwyżka`

* **Faza GREEN:**
    * **Cel:** Implementacja prostej matematyki dla podwyżek.
    * **Kod:** Dodano metodę `giveRaise` z prostą logiką `newSalary = currentSalary * (percentage / 100.0)`.
    * **Wynik:** WSZYSTKIE TESTY PRZESZŁY.
    * **Commit:** `faza GREEN podstawowa podwyżka`

* **Faza REFACTOR:**
    * Brak refaktoryzacji.

---

### Cykl TDD #4: Walidacja podwyżki (Limity)

* **Faza RED:**
    * **Cel:** Podwyżka nie może być ujemna ani przekroczyć `maxSalary` dla stanowiska.
    * **Test:** Dodano dwa testy: `shouldThrowExceptionForNegativeRaise()` (AssertJ) oraz `shouldCapRaiseAtMaxSalaryForPosition()` (Hamcrest `is(equalTo(...))`).
    * **Wynik:** TESTY NIE PRZESZŁY (kod pozwalał na ujemne podwyżki i przekraczał limit).
    * **Commit:** `faza RED walidacja podwyżek`

* **Faza GREEN:**
    * **Cel:** Dodanie walidacji limitów.
    * **Kod:** Do metody `giveRaise` dodano dwa bloki `if` sprawdzające procent i `maxSalary`.
    * **Wynik:** WSZYSTKIE TESTY PRZESZŁY.
    * **Commit:** `faza GREEN walidacja podwyżek`

* **Faza REFACTOR:**
    * Kod był czysty. Brak refaktoryzacji.

(Analogiczny proces TDD został przeprowadzony dla modułów Ocen Pracowniczych, Stażu Pracy i Zespołów Projektowych).

---

## Raport Pokrycia Kodu (JaCoCo)

Po uruchomieniu komendy `./gradlew test`, raport pokrycia kodu został wygenerowany przez JaCoCo. Poniższy zrzut ekranu przedstawia pokrycie dla klas serwisowych i modeli, które implementowaliśmy.

