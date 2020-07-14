package ru.spbstu.controllers;

import java.util.*;
import ru.spbstu.entity.Film;
import ru.spbstu.service.FilmService;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class RecommendationsController {
    //написать реализацию получения id текущего пользователя
    private int currentUserId = 3;

    @Autowired
    private FilmService filmService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/recommendations")
    public List<Film> getRecommendations() {
        List<Film> films = filmService.listFilm();
        List<String> genres = getInfoFromDB("get_genres");
        List<String> producers = getInfoFromDB("get_director");

        Set<Film> recommendations = new LinkedHashSet<>();

        if (!producers.isEmpty()) {
            for (String producer : producers) {
                for (Film film : films) {
                    if (film.getDirector().equals(producer)) {
                        recommendations.add(film);
                    }
                }
            }
        }

        if (!genres.isEmpty()) {
            for (String genre : genres) {
                Set<Film> subset = new TreeSet<>(Comparator.comparingDouble(Film::getRating).reversed());
                for (Film film : films) {
                    if (film.getGenre().equals(genre)) {
                        subset.add(film);
                    }
                }
                recommendations.addAll(subset);
            }
        }

        films.sort(Comparator.comparingDouble(Film::getRating).reversed());
        recommendations.addAll(films);

        recommendations.removeAll(getHistory());

        return new ArrayList<>(recommendations).subList(0,10);
    }

    private List<String> getInfoFromDB(String StoredProcedureName){
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery(StoredProcedureName)
                .registerStoredProcedureParameter("curruserid", int.class, ParameterMode.IN)
                .setParameter("curruserid", currentUserId);
        query.execute();

        List<String> resultList = query.getResultList();
        Integer frequency;

        HashMap<String, Integer> preferencesMap = new HashMap<>();
        for (String elem : resultList) {
            frequency = preferencesMap.get(elem);
            if (frequency == null) {
                preferencesMap.put(elem, 1);
            }
            else {
                preferencesMap.put(elem, ++frequency);
            }
        }

        ArrayList<Integer> frequencyList = new ArrayList<>(preferencesMap.values());
        Collections.sort(frequencyList);

        ArrayList<String> sortedPreferences = new ArrayList<>();
        for (int i = frequencyList.size() - 1; i >= 0; --i)
        {
            for (Map.Entry<String, Integer> entry : preferencesMap.entrySet()){
                if(entry.getValue().equals(frequencyList.get(i))){
                    sortedPreferences.add(entry.getKey());
                    preferencesMap.remove(entry.getKey());
                    break;
                }
            }
        }
        return sortedPreferences;
    }

    private List<Film> getHistory() {
        List<Film> history = new ArrayList<>();

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("get_history")
                .registerStoredProcedureParameter("curruserid", int.class, ParameterMode.IN)
                .setParameter("curruserid", currentUserId);
        query.execute();

        List<Integer> filmsID = query.getResultList();

        for (Integer id : filmsID) {
            history.add(filmService.findFilm(id.longValue()));
        }

        return history;
    }
}

