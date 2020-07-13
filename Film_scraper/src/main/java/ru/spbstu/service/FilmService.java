package ru.spbstu.service;

import ru.spbstu.entity.Film;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    List<Film> listFilm();

    Film findFilm(long id);

    Film findFilm(String title);
}
