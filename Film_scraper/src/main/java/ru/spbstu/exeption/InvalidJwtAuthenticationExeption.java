package ru.spbstu.exeption;

public class InvalidJwtAuthenticationExeption extends RuntimeException {
    public InvalidJwtAuthenticationExeption(String message) {
        super(message);
    }
}
