package ru.spbstu.service;

import ru.spbstu.entity.History;

import java.util.List;

public interface HistoryService {
    List<History> listHistory();

    History findHistory(long id);
    History createHistory(History history);
    History updateHistory(History history, long id);
    void deleteHistory(long id);
}
