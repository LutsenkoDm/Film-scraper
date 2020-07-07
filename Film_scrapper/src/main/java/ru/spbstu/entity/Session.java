package ru.spbstu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "session")
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Integer id;

  private LocalDate date;

  private Integer cost;

  public Session(LocalDate date, Integer cost) {
    this.date = date;
    this.cost = cost;
  }
}
