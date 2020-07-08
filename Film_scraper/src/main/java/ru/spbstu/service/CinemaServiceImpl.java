package ru.spbstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbstu.entity.Cinema;
import ru.spbstu.exeption.CinemaNotFoundExeption;
import ru.spbstu.repository.CinemaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    CinemaRepository cinemaRepository;

    @Override
    public List<Cinema> listCinema() {
        return (List<Cinema>) cinemaRepository.findAll();
    }

    @Override
    public Cinema findCinema(long id) {
        Optional<Cinema> optionalCinema = cinemaRepository.findById(id);
        if (optionalCinema.isPresent()) {
            return optionalCinema.get();
        } else {
            throw new CinemaNotFoundExeption("Not found!");
        }
    }
}
