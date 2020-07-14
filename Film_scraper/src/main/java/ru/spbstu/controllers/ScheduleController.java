package ru.spbstu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.spbstu.entity.*;
import ru.spbstu.exeption.ScheduleNotFoundExeption;
import ru.spbstu.repository.ScheduleRepository;
import ru.spbstu.service.ScheduleService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/database")
public class ScheduleController {

    @Autowired
    private final ScheduleService scheduleService;

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

    @GetMapping("/films/{day}")//Все фильмы(с их сеансами) в определенный день
    public ResponseEntity<List<Schedule>> getFilmsAtCurrentDay(@PathVariable("day") String dayToParse) {
        LocalDate day;
        try {
            day = LocalDate.parse(dayToParse);
        } catch (DateTimeParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format, correct: yyyy-mm-dd");
        }
        List<Schedule> filteredFilmsAtCurrentDay = scheduleService.listSchedule().stream().filter(schedule -> schedule.getSession().getDateTime().toLocalDate().equals(day)).collect(Collectors.toList());
        if (filteredFilmsAtCurrentDay.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Films not found");
        }
        return new ResponseEntity<>(filteredFilmsAtCurrentDay, HttpStatus.OK);
    }

    @GetMapping("/filmSessionsById/{id}")//Все сеансы определенного фильма за всю неделю по ID
    public ResponseEntity<List<Session>> getFilmWeekSessionsById(@PathVariable("id") long id) {
        List<Session> filmWeekSessions = scheduleService.listSchedule().stream().filter(schedule -> schedule.getFilm().getId() == id).map(Schedule::getSession).collect(Collectors.toList());
        if (filmWeekSessions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
        return new ResponseEntity<>(filmWeekSessions, HttpStatus.OK);
    }

    @GetMapping("/filmSessionsByTitle/{title}")//Все сеансы определенного фильма за всю неделю по title
    public ResponseEntity<List<Session>> getFilmWeekScheduleByTitle(@PathVariable("title") String title) {
        List<Session> filmWeekSessions = scheduleService.listSchedule().stream().filter(schedule -> schedule.getFilm().getTitle().equals(title)).map(Schedule::getSession).collect(Collectors.toList());
        if (filmWeekSessions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
        return new ResponseEntity<>(filmWeekSessions, HttpStatus.OK);
    }

    @GetMapping("/cinemaScheduleById/{id}")//Расписание в данном кинотеатре (по его Id)
    public ResponseEntity<List<Schedule>> getCinemaSchedule(@PathVariable("id") long id) {
        List<Schedule> filmsInCinema = scheduleService.listSchedule().stream().filter(schedule -> schedule.getCinema().getId() == id).collect(Collectors.toList());
        if (filmsInCinema.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessions & Films in this cinema not found");
        }
        return new ResponseEntity<>(filmsInCinema, HttpStatus.OK);
    }

    @GetMapping("/cinemaScheduleByName/{name}")//Расписание в данном кинотеатре (по его названию)
    public ResponseEntity<List<Schedule>> getCinemaSchedule(@PathVariable("name") String name) {
        List<Schedule> filmsInCinema = scheduleService.listSchedule().stream().filter(schedule -> schedule.getCinema().getName().equals(name)).collect(Collectors.toList());
        if (filmsInCinema.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessions & Films in this cinema not found");
        }
        return new ResponseEntity<>(filmsInCinema, HttpStatus.OK);
    }

    @GetMapping("/filmsSession/{day}/{time1}/{time2}")//Фильмы с сеансами в определённый день и промежуток времени
    public ResponseEntity<List<Schedule>> filterFilmsAtCurrentDayByTime(@PathVariable("day") String dayParse,
                                                          @PathVariable("time1") String timeParse1,
                                                          @PathVariable("time2") String timeParse2) {

        try {
            LocalTime time1 = LocalTime.parse(timeParse1);
            LocalTime time2 = LocalTime.parse(timeParse2);
            LocalDate day = LocalDate.parse(dayParse);
            List<Schedule> schedules = scheduleService.listSchedule().stream().filter(schedule ->
                    schedule.getSession().getDateTime().toLocalDate().equals(day)
                            && schedule.getSession().getDateTime().toLocalTime().isAfter(time1)
                            && schedule.getSession().getDateTime().toLocalTime().isBefore(time2)).collect(Collectors.toList());

            if(schedules.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found sessions in this time");
            return new ResponseEntity<>(schedules, HttpStatus.OK);

        } catch (DateTimeParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid date format, correct day: yyyy-mm-dd, correct time: HH:mm:ss.zzz");
        }
    }
}

