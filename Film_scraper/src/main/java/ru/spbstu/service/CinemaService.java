package ru.spbstu.service;

import ru.spbstu.entity.Cinema;

import java.util.List;

public interface CinemaService {
    List<Cinema> listCinema();

    Cinema findCinema(long id);
}
