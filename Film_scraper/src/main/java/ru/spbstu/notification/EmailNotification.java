package ru.spbstu.notification;

import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.User;

import java.util.List;

public interface EmailNotification {

  void sendNotification(User user, Film film, List<Schedule> scheduleList);
}

