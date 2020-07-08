package ru.spbstu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Cinema;

@Repository
public interface CinemaRepository extends CrudRepository<Cinema, Long> {
}
