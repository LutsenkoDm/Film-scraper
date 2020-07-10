package ru.spbstu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity
@Table(name = "session")
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Integer id;

  @Column(unique = true)
  private LocalDateTime dateTime;

  private Integer cost;

  public Session(LocalDateTime dateTime, Integer cost) {
    this.dateTime = dateTime;
    this.cost = cost;
  }
}
