package ru.spbstu.schedule;

import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.spbstu.notification.EmailService;

@Data
@Component
public class Schedule {

  private final EmailService emailService;
  @Scheduled(fixedRate = 86400000)
  public void notifyUsers() {
    emailService.notifyAllUsers();
  }
}
