package ru.spbstu.service;

import ru.spbstu.entity.Session;

import java.util.List;

public interface SessionService {
    List<Session> listSession();

    Session findSession(long id);
}
