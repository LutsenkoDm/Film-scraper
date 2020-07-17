package ru.spbstu.notification;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.Session;
import ru.spbstu.entity.User;
import ru.spbstu.repository.FilmRepository;
import ru.spbstu.repository.ScheduleRepository;
import ru.spbstu.repository.SessionRepository;
import ru.spbstu.repository.UserRepository;
import ru.spbstu.service.ScheduleService;
import ru.spbstu.service.SessionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class EmailServiceImpl implements EmailService {

  private final UserRepository userRepository;

  private final EmailNotification emailNotification;

  private final FilmRepository filmRepository;

  private final SessionService sessionService;

  private final ScheduleService scheduleService;

  public void notifyAllUsers() {
    LocalDateTime thisDay = LocalDateTime.now();
    LocalDateTime dayPlusOne = thisDay.plusDays(1);

    List<Film> filmListThisDay = getRecommendedFilms();
    List<Session> sessionList = sessionService.findSessionByDates(thisDay, dayPlusOne);
    List<Schedule> scheduleList = findSchedulesByFilmsAndSessions(filmListThisDay, sessionList);

    List<User> userList = userRepository.findAll();

    for (User user : userList) {
      for (Film film : filmListThisDay) {
        emailNotification.sendNotification(user, film, getScheduleByFilm(film, scheduleList));
      }
    }
  }


  private List<Film> getRecommendedFilms() {
    return filmRepository.findAll();
  }

  private List<Schedule> findSchedulesByFilmsAndSessions(List<Film> filmListThisDay, List<Session> sessionList) {

    List<Schedule> scheduleList = new ArrayList<>();

    for (Film film : filmListThisDay) {
      for (Session session : sessionList) {
        scheduleList.addAll(scheduleService.findSchedulesByFilmAndSession(film, session));
      }
    }
    return scheduleList;
  }

  private List<Schedule> getScheduleByFilm(Film film, List<Schedule> scheduleList) {

    List<Schedule> scheduleListWithOneFilm = new ArrayList<>();

    for (Schedule schedule : scheduleList) {
      if (schedule.getFilm().getTitle().equals(film.getTitle())) {
        scheduleListWithOneFilm.add(schedule);
      }
    }
    return scheduleListWithOneFilm;
  }
  
}
