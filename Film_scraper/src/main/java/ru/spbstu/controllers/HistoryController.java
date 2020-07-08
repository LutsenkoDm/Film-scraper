package ru.spbstu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.spbstu.entity.History;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.User;
import ru.spbstu.exeption.HistoryNotFoundExeption;
import ru.spbstu.service.HistoryService;

import java.util.List;

@RestController
@RequestMapping("/database")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    public HistoryController(HistoryService historyService){
        this.historyService = historyService;
    }

    @GetMapping("/histories")
    public ResponseEntity<List<History>> listHistory() {
        return new ResponseEntity<>(historyService.listHistory(), HttpStatus.OK);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<History> findHistory(@PathVariable("id") long id){
        try {
            return new ResponseEntity<>(historyService.findHistory(id), HttpStatus.OK);
        } catch (HistoryNotFoundExeption e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found");
        }
    }

    @PostMapping(value = "/createHistory", consumes = "application/json", produces = "application/json")
    public History createHistory(@RequestBody History newHistory){
        return historyService.createHistory(newHistory);
    }

    @DeleteMapping("/deleteHistory/{id}")
    public void deleteHistory(@PathVariable("id") Long id) {
        try {
            historyService.deleteHistory(id);
        } catch (HistoryNotFoundExeption e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found");
        }
    }

    @PutMapping(value = "/updateHistory/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<History> updateHistory(@PathVariable("id") Long id, @RequestBody History newHistory) {
        try {
            History updatedHistory = historyService.findHistory(id);
            User user = newHistory.getUser();
            Schedule schedule = newHistory.getSchedule();

            if (user != null)
                updatedHistory.setUser(user);
            if (schedule != null)
                updatedHistory.setSchedule(schedule);

            return ResponseEntity.ok(historyService.createHistory(updatedHistory));
        } catch (HistoryNotFoundExeption e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found");
        }
    }

}
