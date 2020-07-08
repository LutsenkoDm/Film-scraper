package ru.spbstu.service;

import ru.spbstu.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> listSchedule();

    Schedule findSchedule(long id);
    Schedule createSchedule(Schedule schedule);
    Schedule updateSchedule(Schedule schedule, long id);
    void deleteSchedule(long id);
}
