package ru.spbstu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.History;

@Repository
public interface HistoryRepository extends CrudRepository<History, Long> {
}
