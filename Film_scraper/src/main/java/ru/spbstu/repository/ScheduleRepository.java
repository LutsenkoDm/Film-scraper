package ru.spbstu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Film;
import ru.spbstu.entity.Schedule;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    Optional<Schedule> findByFilm(Film film);
}
