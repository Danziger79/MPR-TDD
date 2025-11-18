package model;

// Typ wyliczeniowy definiujący dostępne stanowiska w strukturze organizacyjnej firmy.
// Każdy element enum kapsułkuje reguły biznesowe dotyczące widełek płacowych oraz poziomu hierarchii służbowej dla danej roli.
public enum Position {

    PREZES(25000, 40000, 1),
    WICEPREZES(18000, 28000, 2),
    MANAGER(12000, 19000, 3),
    PROGRAMISTA(8000, 14000, 4),
    STAZYSTA(3000, 5000, 5);

    // Dolny próg wynagrodzenia dla danego stanowiska.
    private final double baseSalary;
    // Górny limit wynagrodzenia dla danego stanowiska.
    private final double maxSalary;
    // Numeryczna reprezentacja poziomu w strukturze firmy (gdzie 1 oznacza najwyższy szczebel zarządczy).
    private final int hierarchyLevel;

    // Prywatny konstruktor inicjalizujący parametry konfiguracyjne dla poszczególnych stałych wyliczeniowych.
    Position(double baseSalary, double maxSalary, int hierarchyLevel) {
        this.baseSalary = baseSalary;
        this.maxSalary = maxSalary;
        this.hierarchyLevel = hierarchyLevel;
    }

    public double getBaseSalary() { return baseSalary; }
    public double getMaxSalary() { return maxSalary; }
    public int getHierarchyLevel() { return hierarchyLevel; }
}