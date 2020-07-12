package ru.spbstu.scraper;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class Session {
    private String cinemaName;
    private List<String> sessionTimes;
}
