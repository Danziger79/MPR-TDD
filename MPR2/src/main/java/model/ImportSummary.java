package model;

import java.util.List;

// Ta klasa to taki "raport" z importu CSV.
// Mówi, ile się udało, a co poszło nie tak.
public class ImportSummary {
    private int importedCount;
    private List<String> errors;

    public ImportSummary(int importedCount, List<String> errors) {
        this.importedCount = importedCount;
        this.errors = errors;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "ImportSummary{" +
                "zaimportowano=" + importedCount +
                ", błędy=" + errors.size() +
                '}';
    }
}