package ru.spbstu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usr")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Integer id;

  @Column(unique = true)
  private String user_name;

  private String password;

  private String email;

  public User(String user_name, String password, String email) {
    this.user_name = user_name;
    this.password = password;
    this.email = email;
  }
}
