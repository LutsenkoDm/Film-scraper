package ru.spbstu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.exeption.FilmNotFoundExeption;
import ru.spbstu.service.FilmService;
import ru.spbstu.service.ScheduleService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/database")
public class FilmController {
    @Autowired
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public ResponseEntity<List<Film>> listFilm(){
        return new ResponseEntity<>(filmService.listFilm(), HttpStatus.OK);
    }

    @GetMapping("/filmById/{id}")
    public ResponseEntity<Film> findFilm(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(filmService.findFilm(id), HttpStatus.OK);
        } catch (FilmNotFoundExeption e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
    }

    @GetMapping("/filmByTitle/{title}")
    public ResponseEntity<Film> findFilm(@PathVariable("title") String title) {
        try {
            return new ResponseEntity<>(filmService.findFilm(title), HttpStatus.OK);
        } catch (FilmNotFoundExeption e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
    }

    @GetMapping("/filteredByYearFilms/{year1}/{year2}")//Фильтр по годам
    public ResponseEntity<List<Film>> filterFilmsByYear(@PathVariable("year1") int year1, @PathVariable("year2") int year2) {
        List<Film> filteredByYearFilms = filmService.listFilm().stream().filter(film -> film.getYear()  >= year1 && film.getYear() <= year2).collect(Collectors.toList());
        if (filteredByYearFilms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
        }
        return new ResponseEntity<>(filteredByYearFilms, HttpStatus.OK);
    }

    @GetMapping("/filteredByCountryFilms/{country}")//Фильтр по стане
    public ResponseEntity<List<Film>> filterFilmsByCountry(@PathVariable("country") String country) {
        List<Film> filteredByCountryFilms = filmService.listFilm().stream().filter(film -> film.getCountry().contains(country)).collect(Collectors.toList());
        if (filteredByCountryFilms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
        }
        return new ResponseEntity<>(filteredByCountryFilms, HttpStatus.OK);
    }

    @GetMapping("/filteredByDirectorFilms/{director}")//Фильтр по режиссеру
    public ResponseEntity<List<Film>> filterFilmsByDirector(@PathVariable("director") String director) {
        List<Film> filteredByDirectorFilms = filmService.listFilm().stream().filter(film -> film.getDirector().contains(director)).collect(Collectors.toList());
        if (filteredByDirectorFilms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
        }
        return new ResponseEntity<>(filteredByDirectorFilms, HttpStatus.OK);
    }

    @GetMapping("/filteredByGenreFilms/{genre}")//Фильтр по жанру
    public ResponseEntity<List<Film>> filterFilmsByGenre(@PathVariable("genre") String genre) {
        List<Film> filteredByGenreFilms = filmService.listFilm().stream().filter(film -> film.getGenre().contains(genre)).collect(Collectors.toList());
        if (filteredByGenreFilms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
        }
        return new ResponseEntity<>(filteredByGenreFilms, HttpStatus.OK);
    }

    @GetMapping("/filteredByRatingFilms/{rating1}/{rating2}")//Фильтр по рейтингу
    public ResponseEntity<List<Film>> filterFilmsByRating(@PathVariable("rating1") double rating1, @PathVariable("rating2") double rating2) {
        List<Film> filteredByRatingFilms = filmService.listFilm().stream().filter(film -> film.getRating() >= rating1 && film.getRating() <= rating2).collect(Collectors.toList());
        if (filteredByRatingFilms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
        }
        return new ResponseEntity<>(filteredByRatingFilms, HttpStatus.OK);
    }

    @GetMapping("/filteredByActorFilms/{actor}")//Фильтр по актеру
    public ResponseEntity<List<Film>> filterFilmsByActor(@PathVariable("actor") String actor) {
        List<Film> filteredByActorFilms = filmService.listFilm().stream().filter(film -> film.getActors().contains(actor)).collect(Collectors.toList());
        if (filteredByActorFilms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
        }
        return new ResponseEntity<>(filteredByActorFilms, HttpStatus.OK);
    }

    @GetMapping("/filteredByDurationFilms/{duration1}/{duration2}")//Фильтр по длительности
    public ResponseEntity<List<Film>> filterFilmsByDuration(@PathVariable("duration1") int duration1, @PathVariable("duration2") int duration2) {
        try {
            List<Film> filteredByDurationFilms = filmService.listFilm().stream().filter(film -> film.getDurationInMinutes() >= duration1 && film.getDurationInMinutes() <= duration2).collect(Collectors.toList());
            if (filteredByDurationFilms.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
            }
            return new ResponseEntity<>(filteredByDurationFilms, HttpStatus.OK);
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid duration");
        }
    }
}
