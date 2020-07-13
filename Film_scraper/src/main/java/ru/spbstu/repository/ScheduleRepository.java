package ru.spbstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;
import ru.spbstu.entity.Session;

import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  List<Schedule> findAllBySession(Session session);
  List<Schedule> findAllByFilm(Film film);
}
