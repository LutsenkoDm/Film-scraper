package ru.spbstu.exeption;

public class SessionNotFoundExeption extends RuntimeException{
    public SessionNotFoundExeption(String message) {
        super(message);
    }
}
