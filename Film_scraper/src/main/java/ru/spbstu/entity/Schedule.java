package ru.spbstu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "schedule")
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "film_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Film film;

  @ManyToOne
  @JoinColumn(name = "session_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Session session;

  @ManyToOne
  @JoinColumn(name = "cinema_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Cinema cinema;

  public Schedule(Film film, Session session, Cinema cinema) {
    this.film = film;
    this.session = session;
    this.cinema = cinema;
  }
}
