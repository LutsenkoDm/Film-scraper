package ru.spbstu.notification;

import lombok.Data;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.User;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Data
@Service
public class EmailNotificationSender implements EmailNotification {

  private static final String senderEmail = "filmnotification@gmail.com";

  private final JavaMailSender javaMailSender;

  @Override
  public void sendNotification(User user, Film film, List<Schedule> scheduleList) {

    if (scheduleList.size() == 0) {
      return;
    }

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

      helper.setFrom(senderEmail);
      helper.setTo(user.getEmail());
      helper.setSubject("Film notification");

      String message = getMessage(film, scheduleList);
      helper.setText(message);

      File filmPoster = getPng(film);
      if (filmPoster != null) {
        helper.addAttachment(film.getTitle() + ".png", filmPoster);
      }
      javaMailSender.send(mimeMessage);

      if (filmPoster != null) {
        filmPoster.delete();
      }

    }
    catch (MessagingException e) {
      e.printStackTrace();
    }

  }

  private String getMessage(Film film, List<Schedule> scheduleList) {
    String message = "Ближайший фильм: " + film.getTitle() + "\n";
    message += "Сеансы: " + "\n";
    for (Schedule schedule : scheduleList) {
      message += ("Кинотеатр: " + schedule.getCinema().getName() + "\n");
      message += ("Дата: " + schedule.getSession().getDateTime() + "\n");
      message += ("Длительность: " + film.getDuration() + "\n");
      message += ("Стоимость: " + schedule.getSession().getCost() + "\n");
      message += "------------------------------------------------------" + "\n";
    }
    return message;
  }

  private File getPng(Film film) {

    String path1 = film.getImageRef1();

    File filmPosterFile = new File("image.png");
    if (path1 != null) {
      try {
        BufferedImage img = ImageIO.read(new URL(path1));

        ImageIO.write(img, "png", filmPosterFile);

      }
      catch (IOException e) {
        e.printStackTrace();
      }

    }
    else {
      filmPosterFile = null;
    }
    return filmPosterFile;
  }

}
