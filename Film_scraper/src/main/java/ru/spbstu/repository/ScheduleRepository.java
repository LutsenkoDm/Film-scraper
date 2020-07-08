package ru.spbstu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.entity.Schedule;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
}
