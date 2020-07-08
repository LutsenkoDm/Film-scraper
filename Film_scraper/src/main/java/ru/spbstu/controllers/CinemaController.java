package ru.spbstu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.spbstu.entity.Cinema;
import ru.spbstu.exeption.CinemaNotFoundExeption;
import ru.spbstu.service.CinemaService;

import java.util.List;

@RestController
@RequestMapping("/database")
public class CinemaController {
    @Autowired
    private CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService){
        this.cinemaService = cinemaService;
    }

    @GetMapping("/cinemas")
    public ResponseEntity<List<Cinema>> getAllBooks() {
        return new ResponseEntity<>(cinemaService.listCinema(), HttpStatus.OK);
    }

    @GetMapping("/cinema/{id}")
    public ResponseEntity<Cinema> findCinema(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(cinemaService.findCinema(id), HttpStatus.OK);
        } catch (CinemaNotFoundExeption e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cinema not found");
        }
    }
}
