package ru.spbstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByDateTime(LocalDateTime dateTime);
    List<Session> findAllByDateTimeIsAfterAndDateTimeIsBefore(LocalDateTime dateTime, LocalDateTime endOfDate);
}

