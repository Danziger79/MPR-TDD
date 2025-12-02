package interfaces;

// Rola: Abstrakcja warstwy dostępu do systemu plików (IO Adapter).
// Opis: Interfejs definiujący kontrakt dla operacji zapisu danych.
// Stosowany w celu odseparowania logiki biznesowej od niskopoziomowych operacji wejścia-wyjścia (I/O).
// Kluczowy dla testowalności – umożliwia zastąpienie rzeczywistych operacji dyskowych
// obiektem typu Mock (FileSystemMock) w środowisku testowym.
public interface FileSystemAdapter {

    // Zleca zapis danych tekstowych pod wskazaną ścieżką.
    // Implementacja odpowiada za techniczne aspekty operacji (strumienie, obsługa błędów IO).
    void write(String path, String content);
}