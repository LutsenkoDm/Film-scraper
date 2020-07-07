package ru.spbstu.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Schedule;
import ru.spbstu.exeption.ScheduleNotFoundExeption;
import ru.spbstu.repository.ScheduleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Override
    public List<Schedule> listSchedule() {
        return (List<Schedule>) scheduleRepository.findAll();
    }

    @Override
    public Schedule findSchedule(long id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {
            return optionalSchedule.get();
        } else {
            throw new ScheduleNotFoundExeption("Not found!");
        }
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Schedule schedule, long id) {
        Schedule existingSchedule = findSchedule(id);
        BeanUtils.copyProperties(schedule, existingSchedule);
        return scheduleRepository.save(existingSchedule);
    }

    @Override
    public void deleteSchedule(long id) {
        scheduleRepository.delete(findSchedule(id));
    }
}
