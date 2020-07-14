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
  private Long id;

  @Column(unique = true)
  private String title;

  private Integer year;

  private String country;

  private String director;

  private String genre;

  private String duration;

  private String actors;

  private String description;

  private String imageRef1;
  private String imageRef2;
  private String imageRef3;
  private String imageRef4;

  private double rating;

  public Film(String title, Integer year, String country, String director, String genre, String duration, String actors, String description,
              String imageRef1, String imageRef2, String imageRef3, String imageRef4, double rating) {
    this.title = title;
    this.year = year;
    this.country = country;
    this.director = director;
    this.genre = genre;
    this.duration = duration;
    this.actors = actors;
    this.rating = rating;
    this.description = description;
    this.imageRef1 = imageRef1;
    this.imageRef2 = imageRef2;
    this.imageRef3 = imageRef3;
    this.imageRef4 = imageRef4;
  }

  public int getDurationInMinutes () throws NumberFormatException {
    return Integer.parseInt(duration.split(" ")[0]);
  }
}
