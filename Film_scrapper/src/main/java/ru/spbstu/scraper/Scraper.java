package ru.spbstu.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class Scraper {

    final static private  WebDriver driver;
    final static private  String url;
    private static Week week;
    private static String year;
    private static String country;
    private static String director;
    private static String genre;
    private static String duration;
    private static String actors;
    List<Film> previousDaysFilms = new ArrayList<>();

    static {
        System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
        url = "https://www.kinopoisk.ru/afisha/city/2/";
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        week = new Week();
    }

    private List<String> getFilmHrefs(String day) {//Получаем список со всеми ссылками на фильмы
        List<String> filmHrefs = new ArrayList<>();
        List<WebElement> filmWebElements = driver.findElements(By.xpath("//div[@class=\"title _FILM_\"]/div/p"));
        for (WebElement film : filmWebElements) {
            if (film.findElement(By.tagName("u")).getAttribute("id").contains(day)) {//Если фильм идет в этот день
                filmHrefs.add(film.findElement(By.tagName("a")).getAttribute("href"));//То добавлеем ссылку на него
            } else {
                break;//Сначала всегда идут фильмы в этот день, после них нас уже не интересуют
            }
        }
        return filmHrefs;
    }

    private List<Session> getFilmSessionList() {
        List<Session> sessionList = new ArrayList<>();
        driver.findElement(By.xpath("//*[@id=\"newMenuSub\"]/li[3]/a")).click();//Жмем кнопку переводящую нас на страницу с сеансами данного фильма
        List<WebElement> schedulerItems = driver.findElements(By.xpath("//*[@class=\"schedule-item\"]"));//Получаем элемент с названием кнотеатра и временем в нем
        for (WebElement item : schedulerItems) {
            String cinemaName = item.findElement(By.xpath(".//*[@class=\"schedule-item__left\"]//a")).getText();//Получаем название кинотеатра
            //Получаем список времен сеансов в кинотеатре
            List<WebElement> sessionTimes = item.findElements(By.xpath(".//*[@class=\"schedule-item__right\"]//*[@class=\"schedule-item__session-button\"]"));
            List<String> sessionTimesInString = new ArrayList<>();
            for (WebElement sessionTime : sessionTimes) {//Переобразуем их в String
                sessionTimesInString.add(sessionTime.getText());
            }
            sessionList.add(new Session(cinemaName, sessionTimesInString));
        }
        return sessionList;
    }

    public List<Film> getFilms(String day) {//Если тут ввести что-то кроме week.getDay(0-6) то все упадет, но пока так
        driver.get(url + "day_view/" + day);
        List<Film> thisDayFilms = new ArrayList<>();
        for (String href : getFilmHrefs(day)) {//Идем по списку со всеми ссылками на фильмы в этот день
            driver.get(href);//Переходим на страницу с фильмом
            String filmName = driver.findElement(By.xpath("//*[@id=\"headerFilm\"]/h1/span")).getText();//Получаем названия фильма
            Optional<Film> previousDayFilm = previousDaysFilms.stream().filter(s -> s.getName().contains(filmName)).findAny();//Ищем такой фильм в уже просмотренных нами
            if (previousDayFilm.isPresent()) {//Если такой был в предыдущие дни
                previousDayFilm.get().setSessionList(getFilmSessionList());//То меняем только сеансы
                thisDayFilms.add(previousDayFilm.get());//Добавляем его в список фильмов в данный день
            } else {//Если в преыдущие дни такого фильма не было
                actors = driver.findElement(By.xpath("//*[@id=\"actorList\"]")).getText();//Скрапим актеров
                actors = actors.substring(16, !actors.contains("...") ? actors.length() : actors.indexOf("..."));//16 потому что надо убрать "В главных ролях:"(эта надпись всегда вначале списка актеров)
                List<WebElement> filmInfoTableRows = driver.findElements(By.xpath("//*[@id=\"infoTable\"]/table/tbody/tr"));//Скрапим табличку с инфой о фильме
                for (WebElement row : filmInfoTableRows) {//Бежим по ней и ищем интересующие нас характеристики
                    String rowTitle = row.findElement(By.xpath("./td[1]")).getText();
                    switch (rowTitle) {
                        case "год":
                            year = row.findElement(By.xpath("./td[2]")).getText();
                            break;
                        case "страна":
                            country = row.findElement(By.xpath("./td[2]")).getText();
                            break;
                        case "режиссер":
                            director = row.findElement(By.xpath("./td[2]")).getText();
                            break;
                        case "жанр":
                            genre = row.findElement(By.xpath("./td[2]")).getText().replace(", ..." + '\n' + "слова", "");
                            break;
                        case "время":
                            duration = row.findElement(By.xpath(".//*[@id=\"runtime\"]")).getText();
                            break;
                    }
                }
                Film newFilm = new Film(filmName,year,country,director,genre,duration, actors, getFilmSessionList());
                thisDayFilms.add(newFilm);//Добавляем фильм в список фильмов в данный день
                previousDaysFilms.add(newFilm);//И его же в список уже просмотренных, т.к. мы его встретили первый раз
            }
        }
        return thisDayFilms;
    }

    public List<Film> getDay1Films() {
        return getFilms(week.getDay(0));
    }

    public List<Film> getDay2Films() {
        return getFilms(week.getDay(1));
    }

    public List<Film> getDay3Films() {
        return getFilms(week.getDay(2));
    }

    public List<Film> getDay4Films() {
        return getFilms(week.getDay(3));
    }

    public List<Film> getDay5Films() {
        return getFilms(week.getDay(4));
    }

    public List<Film> getDay6Films() {
        return getFilms(week.getDay(5));
    }

    public List<Film> getDay7Films() {
        return getFilms(week.getDay(6));
    }

    public List<List<Film>> getWeekFilms() {
        List<List<Film>> weekFilms = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekFilms.add(getFilms(week.getDay(0)));
        }
        return weekFilms;
    }

    public void close() {
        driver.quit();
    }
}
