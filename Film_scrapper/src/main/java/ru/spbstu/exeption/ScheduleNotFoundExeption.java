package ru.spbstu.exeption;

public class ScheduleNotFoundExeption extends RuntimeException {
    public ScheduleNotFoundExeption(String message) {
        super(message);
    }
}
