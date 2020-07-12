package ru.spbstu.service;

import ru.spbstu.entity.Film;

import java.util.List;

public interface FilmService {
    List<Film> listFilm();

    Film findFilm(long id);
}
