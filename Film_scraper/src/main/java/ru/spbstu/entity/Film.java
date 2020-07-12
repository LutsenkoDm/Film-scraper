package ru.spbstu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "film")
public class Film {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Integer id;

  @Column(unique = true)
  private String title;

  private Integer year;

  private String country;

  private String director;

  private String genre;

  private String duration;

  private String actors;

  private double rating;

  public Film(String title, Integer year, String country, String director, String genre, String duration, String actors, double rating) {
    this.title = title;
    this.year = year;
    this.country = country;
    this.director = director;
    this.genre = genre;
    this.duration = duration;
    this.actors = actors;
    this.rating = rating;
  }
}
