package ru.spbstu.exeption;

public class HistoryNotFoundExeption extends RuntimeException {
    public HistoryNotFoundExeption(String message) {
        super(message);
    }
}
