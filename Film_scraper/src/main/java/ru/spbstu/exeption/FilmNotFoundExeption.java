package ru.spbstu.exeption;

public class FilmNotFoundExeption extends RuntimeException {
    public FilmNotFoundExeption(String message) {
        super(message);
    }
}
