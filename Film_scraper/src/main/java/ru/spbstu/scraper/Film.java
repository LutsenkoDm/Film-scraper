package ru.spbstu.scraper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Film {
    private String name;
    private String year;
    private String country;
    private String director;
    private String genre;
    private String duration;
    private String actors;
    private List<Session> sessionList;
}
