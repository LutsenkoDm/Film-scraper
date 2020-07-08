package ru.spbstu.exeption;

public class CinemaNotFoundExeption extends RuntimeException {
    public CinemaNotFoundExeption(String message) {
        super(message);
    }
}
