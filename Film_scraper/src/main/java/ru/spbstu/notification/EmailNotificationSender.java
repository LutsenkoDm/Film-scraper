package ru.spbstu.notification;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.User;

import java.util.List;

@Data
@Service
public class EmailNotificationSender implements EmailNotification{

  private static final String senderEmail = "filmnotification@gmail.com";

  private final JavaMailSender javaMailSender;

  @Override
  public void sendNotification(User user, Film film, List<Schedule> scheduleList) {

    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setFrom(senderEmail);
    mailMessage.setTo(user.getEmail());
    mailMessage.setSubject("Film notification");

    String message = "Ближайший фильм: " + film.getTitle() + "\n" + "Сеансы: " + "\n";
    for (Schedule schedule: scheduleList) {
      message += ("Кинотеатр: " + schedule.getCinema().getName() + "\n");
      message += ("Дата: " + schedule.getSession().getDateTime() + "\n");
      message += ("Стоимость: " + schedule.getSession().getCost() + "\n");
      message += "------------------------------------------------------" + "\n";
    }
    mailMessage.setText(message);

    javaMailSender.send(mailMessage);

  }

}
