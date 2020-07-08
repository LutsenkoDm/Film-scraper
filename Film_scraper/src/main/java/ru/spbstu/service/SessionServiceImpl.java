package ru.spbstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Session;
import ru.spbstu.exeption.SessionNotFoundExeption;
import ru.spbstu.repository.SessionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public List<Session> listSession() {
        return (List<Session>) sessionRepository.findAll();
    }

    @Override
    public Session findSession(long id) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isPresent()) {
            return optionalSession.get();
        } else {
            throw new SessionNotFoundExeption("Not found!");
        }
    }
}
