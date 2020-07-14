package ru.spbstu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.spbstu.entity.Cinema;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.Session;
import ru.spbstu.repository.*;
import ru.spbstu.scraper.Scraper;
import ru.spbstu.scraper.Week;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
public class ScraperApplication {

  @Autowired
  FilmRepository filmRepository;
  @Autowired
  SessionRepository sessionRepository;
  @Autowired
  CinemaRepository cinemaRepository;
  @Autowired
  ScheduleRepository scheduleRepository;
  @Autowired
  HistoryRepository historyRepository;

  @Autowired
  private Scraper scraper;

  private Film film;
  private Session session;
  private Cinema cinema;

  public static void main(String[] args) {
    SpringApplication.run(ScraperApplication.class, args);
  }

  @Bean
  public void scrap() {

    for (int i = 0; i < Week.size(); i++) {
      String day = Week.getDay(i);
      scraper.getFilms(day).forEach((scrapedFilm) -> {
        Optional<Film> filmOptional = filmRepository.findByTitle(scrapedFilm.getTitle());
        if (filmOptional.isPresent()) {
          film = filmOptional.get();
        } else {
          film = new Film(scrapedFilm.getTitle(), Integer.parseInt(scrapedFilm.getYear()), scrapedFilm.getCountry(), scrapedFilm.getDirector(),
                          scrapedFilm.getGenre(), scrapedFilm.getDuration(), scrapedFilm.getActors(), scrapedFilm.getDescription(), scrapedFilm.getRating());
          filmRepository.save(film);
        }
        for (var scrapedSession : scrapedFilm.getSessionList()) {
          Optional<Cinema> cinemaOptional = cinemaRepository.findByName(scrapedSession.getCinemaName());
          if (cinemaOptional.isPresent()) {
            cinema = cinemaOptional.get();
          } else {
            cinema = new Cinema(scrapedSession.getCinemaName());
            cinemaRepository.save(cinema);
          }
          scrapedSession.getSessionTimes().forEach((time) -> {
            LocalDateTime dateTime = LocalDateTime.parse(day + "T" + time);
            Optional<Session> sessionOptional = sessionRepository.findByDateTime(dateTime);
            if (sessionOptional.isPresent()) {
              session = sessionOptional.get();
            } else {
              session = new Session(dateTime, 100);
              sessionRepository.save(session);
            }
            scheduleRepository.save(new Schedule(film, session, cinema));
          });
        }
      });
    }
    scraper.close();
  }
}
