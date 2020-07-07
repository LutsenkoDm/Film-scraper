package ru.spbstu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.spbstu.entity.Session;
import ru.spbstu.exeption.SessionNotFoundExeption;
import ru.spbstu.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/database")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> listFilm(){
        return new ResponseEntity<>(sessionService.listSession(), HttpStatus.OK);
    }

    @GetMapping("/session/{id}")
    public ResponseEntity<Session> findSession(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(sessionService.findSession(id), HttpStatus.OK);
        } catch (SessionNotFoundExeption e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
        }
    }
}
