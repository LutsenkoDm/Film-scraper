package ru.spbstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Film;
import ru.spbstu.exeption.FilmNotFoundExeption;
import ru.spbstu.repository.FilmRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {

    @Autowired
    FilmRepository filmRepository;

    @Override
    public List<Film> listFilm() {
        return (List<Film>) filmRepository.findAll();
    }

    @Override
    public Film findFilm(long id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isPresent()) {
            return optionalFilm.get();
        } else {
            throw new FilmNotFoundExeption("Not found!");
        }
    }
}
