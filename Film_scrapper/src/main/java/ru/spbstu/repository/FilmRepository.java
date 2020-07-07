package ru.spbstu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Film;

@Repository
public interface FilmRepository extends CrudRepository<Film, Long> {
}
