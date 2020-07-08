package ru.spbstu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.spbstu.entity.Film;
import ru.spbstu.exeption.FilmNotFoundExeption;
import ru.spbstu.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/database")
public class FilmController {
    @Autowired
    private FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public ResponseEntity<List<Film>> listFilm(){
        return new ResponseEntity<>(filmService.listFilm(), HttpStatus.OK);
    }

    @GetMapping("/film/{id}")
    public ResponseEntity<Film> findFilm(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(filmService.findFilm(id), HttpStatus.OK);
        } catch (FilmNotFoundExeption e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
    }
}
