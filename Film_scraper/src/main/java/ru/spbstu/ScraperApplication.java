package ru.spbstu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Session;
import ru.spbstu.repository.FilmRepository;
import ru.spbstu.repository.SessionRepository;
import ru.spbstu.scraper.Scraper;
import ru.spbstu.scraper.Week;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class ScraperApplication {

  @Autowired
  FilmRepository filmRepository;

  @Autowired
  SessionRepository sessionRepository;

  @Autowired
  private Scraper scraper;

  public static void main(String[] args) {
    SpringApplication.run(ScraperApplication.class, args);
  }

  @Bean
  public void scrap() {

    for (int i = 0; i < Week.size(); i++) {
      String day = Week.getDay(i);
      scraper.getFilms(day).forEach((film) -> {
        for (var session : film.getSessionList()) {
          session.getSessionTimes().forEach((time) -> sessionRepository.save(new Session(LocalDateTime.parse(day + "T" + time), 100)));
        }
      });
    }

    scraper.getBufferedFilms().forEach((film) -> filmRepository.save(new Film(
            film.getName(),
            Integer.parseInt(film.getYear()),
            film.getCountry(),
            film.getDirector(),
            film.getGenre(),
            film.getDuration(),
            film.getActors())));
    scraper.close();
  }
}
