package ru.spbstu.notification;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.Session;
import ru.spbstu.entity.User;
import ru.spbstu.repository.ScheduleRepository;
import ru.spbstu.repository.SessionRepository;
import ru.spbstu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class EmailServiceImpl implements EmailService {

  private final ScheduleRepository scheduleRepository;

  private final SessionRepository sessionRepository;

  private final UserRepository userRepository;

  private final EmailNotification emailNotification;

  public void notifyAllUsers() {
    LocalDateTime date = LocalDateTime.now();

    LocalDateTime endOfDate = date.plusDays(1);

    List<Film> filmListThisDay = getNearFilms(date, endOfDate);

    List<User> userList = userRepository.findAll();

    for (User user : userList) {
      for (Film film: filmListThisDay) {
        emailNotification.sendNotification(user, film, findSchedulesByFilm(film));
      }
    }
  }

  private List<Film> getNearFilms(LocalDateTime dateTime, LocalDateTime endOfDate) {


    List<Session> sessionList = sessionRepository.findAllByDateTimeGreaterThanAndDateTimeBefore(dateTime, endOfDate);

    List<Film> filmListThisDay = new ArrayList<>();

    List<Schedule> scheduleList = new ArrayList<>();

    if (sessionList != null) {
      for (Session session : sessionList) {
        scheduleList.addAll(scheduleRepository.findAllBySession(session));
      }
    }

    for (Schedule schedule : scheduleList) {
      if (!filmListThisDay.contains(schedule.getFilm())) {
        filmListThisDay.add(schedule.getFilm());
      }
    }

    return filmListThisDay;
  }

  private List<Schedule> findSchedulesByFilm(Film film) {
    return scheduleRepository.findAllByFilm(film);
  }
}
