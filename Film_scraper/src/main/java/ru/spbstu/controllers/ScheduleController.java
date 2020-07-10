package ru.spbstu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.spbstu.entity.*;
import ru.spbstu.exeption.ScheduleNotFoundExeption;
import ru.spbstu.service.ScheduleService;

import java.util.List;

@RestController
@RequestMapping("/database")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> listSchedule() {
        return new ResponseEntity<>(scheduleService.listSchedule(), HttpStatus.OK);
    }

    @GetMapping("/schedule/{id}")
    public ResponseEntity<Schedule> findSchedule(@PathVariable("id") long id){
        try {
            return new ResponseEntity<>(scheduleService.findSchedule(id), HttpStatus.OK);
        } catch (ScheduleNotFoundExeption e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found");
        }
    }

    @PostMapping(value = "/createSchedule", consumes = "application/json", produces = "application/json")
    public Schedule createSchedule(@RequestBody Schedule newSchedule){
        return scheduleService.createSchedule(newSchedule);
    }

    @DeleteMapping("/deleteSchedule/{id}")
    public void deleteSchedule(@PathVariable("id") Long id) {
        try {
            scheduleService.deleteSchedule(id);
        } catch (ScheduleNotFoundExeption e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found");
        }
    }

    @PutMapping(value = "/updateSchedule/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable("id") Long id, @RequestBody Schedule newSchedule) {
        try {
            Schedule updatedSchedule= scheduleService.findSchedule(id);
            Film film = newSchedule.getFilm();
            Session session = newSchedule.getSession();
            Cinema cinema = newSchedule.getCinema();

            if (film != null)
                updatedSchedule.setFilm(film);
            if (session != null)
                updatedSchedule.setSession(session);
            if (cinema != null)
                updatedSchedule.setCinema(cinema);

            return ResponseEntity.ok(scheduleService.createSchedule(updatedSchedule));
        } catch (ScheduleNotFoundExeption e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found");
        }
    }
}
