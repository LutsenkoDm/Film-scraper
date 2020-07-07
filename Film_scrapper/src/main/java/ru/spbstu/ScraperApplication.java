package ru.spbstu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.spbstu.scraper.Scraper;
import ru.spbstu.scraper.Week;

@SpringBootApplication
public class ScraperApplication {

  @Autowired
  private Scraper scraper;

  @Autowired
  private Week week;

  public static void main(String[] args) {
    SpringApplication.run(ScraperApplication.class, args);
  }

  @Bean
  public void scrap() {
    //Получаем список филмов с кинопоиска в определенный день, в данном случае сегодня
    // Каждый Film содержит название, год, страну, режиссера, жанр длительность, актеров и список его сеансов в этот день
    scraper.getFilms(week.getDay(0)).forEach((film) -> System.out.println(film.toString() + '\n'));
    System.out.println("--------------------------------------------------------------------------------");
    scraper.getDay1Films().forEach((film) -> System.out.println(film.toString() + '\n'));
    scraper.close();
  }
}
