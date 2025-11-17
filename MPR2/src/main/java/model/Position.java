package model;


public enum Position {

    PREZES(25000, 40000, 1),
    WICEPREZES(18000, 28000, 2),
    MANAGER(12000, 19000, 3),
    PROGRAMISTA(8000, 14000, 4),
    STAZYSTA(3000, 5000, 5);

    private final double baseSalary;
    private final double maxSalary;
    private final int hierarchyLevel;

    Position(double baseSalary, double maxSalary, int hierarchyLevel) {
        this.baseSalary = baseSalary;
        this.maxSalary = maxSalary;
        this.hierarchyLevel = hierarchyLevel;
    }

    // Gettery
    public double getBaseSalary() { return baseSalary; }
    public double getMaxSalary() { return maxSalary; }
    public int getHierarchyLevel() { return hierarchyLevel; }
}