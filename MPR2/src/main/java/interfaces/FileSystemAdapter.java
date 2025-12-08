package interfaces;

// Rola: Abstrakcja warstwy dostępu do systemu plików (IO Adapter).
// Opis: Interfejs definiujący kontrakt dla operacji zapisu danych.
// Stosowany w celu odseparowania logiki biznesowej od niskopoziomowych operacji wejścia-wyjścia (I/O).
// Kluczowy dla testowalności – umożliwia zastąpienie rzeczywistych operacji dyskowych
// obiektem typu Mock (FileSystemMock) w środowisku testowym.


import java.io.IOException;

public interface FileSystemAdapter {
    // Dodajemy "throws IOException", żeby móc to symulować w teście
    void write(String path, String content) throws IOException;
}