package ru.spbstu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
}
