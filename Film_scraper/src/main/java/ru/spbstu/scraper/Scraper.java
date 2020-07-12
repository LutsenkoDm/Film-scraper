package ru.spbstu.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Component
public class Scraper {

    final static private  WebDriver driver;
    final static private  String url;
    final static private  String cityUrl;
    private static String year;
    private static String country;
    private static String director;
    private static String genre;
    private static String duration;
    private static String actors;
    private static double rating;
    private static final List<Film> previousDaysFilms = new ArrayList<>();

    static {
        System.setProperty("webdriver.gecko.driver", "Film_scraper\\drivers\\geckodriver.exe");
        url = "https://www.kinopoisk.ru/";
        cityUrl = "afisha/city/2/";
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("localhost:8888");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        driver = new FirefoxDriver(new FirefoxOptions(capabilities));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


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

    private List<Session> getFilmSessionList(String filmHref) {
        List<Session> sessionList = new ArrayList<>();
        driver.get(filmHref + cityUrl);//Жмем кнопку переводящую нас на страницу с сеансами данного фильма
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

    public List<Film> getFilms(String day) {
        driver.get(url + cityUrl + "day_view/" + day);
        List<Film> thisDayFilms = new ArrayList<>();
        for (String href : getFilmHrefs(day)) {//Идем по списку со всеми ссылками на фильмы в этот день
            driver.get(href);//Переходим на страницу с фильмом
            String filmTitle = driver.findElement(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[1]/div[1]/div/h1/span")).getText();//Получаем названия фильма
            Optional<Film> previousDayFilm = previousDaysFilms.stream().filter(s -> s.getTitle().contains(filmTitle)).findAny();//Ищем такой фильм в уже просмотренных нами
            if (previousDayFilm.isPresent()) {//Если такой был в предыдущие дни
                previousDayFilm.get().setSessionList(getFilmSessionList(href));//То меняем только сеансы
                thisDayFilms.add(previousDayFilm.get());//Добавляем его в список фильмов в данный день
            } else {//Если в преыдущие дни такого фильма не было
                actors = driver.findElement(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[2]/div[2]/div/div[1]/ul")).getText();//Скрапим актеров
                try {
                    rating = Double.parseDouble(driver.findElement(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[1]/div[2]/div/div[1]/span[1]/span")).getText());
                } catch (NumberFormatException exception) {
                    rating = 0.0;
                    exception.printStackTrace();
                }
                List<WebElement> filmInfoTableRows = driver.findElements(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[2]/div[1]/div/div"));//Скрапим табличку с инфой о фильме
                By rowValueXpath = By.xpath("./div[2]");
                for (WebElement row : filmInfoTableRows) {//Бежим по ней и ищем интересующие нас характеристики
                      switch (row.findElement(By.xpath("./div[1]")).getText()/*Имя ряда*/) {
                        case "Год производства":
                            year = row.findElement(rowValueXpath).getText();
                            break;
                        case "Страна":
                            country = row.findElement(rowValueXpath).getText();
                            break;
                        case "Режиссер":
                            director = row.findElement(rowValueXpath).getText();
                            break;
                        case "Жанр":
                            genre = row.findElement(rowValueXpath).getText().replace(", ..." + '\n' + "слова", "");
                            break;
                        case "Время":
                            duration = row.findElement(rowValueXpath).getText();
                            break;
                    }
                }
                Film newFilm = new Film(filmTitle,year,country,director,genre,duration, actors, rating, getFilmSessionList(href));
                thisDayFilms.add(newFilm);//Добавляем фильм в список фильмов в данный день
                previousDaysFilms.add(newFilm);//И его же в список уже просмотренных, т.к. мы его встретили первый раз
            }
        }
        return thisDayFilms;
    }

    public List<Film> getDay1Films() {
        return getFilms(Week.getDay(0));
    }

    public List<Film> getDay2Films() {
        return getFilms(Week.getDay(1));
    }

    public List<Film> getDay3Films() {
        return getFilms(Week.getDay(2));
    }

    public List<Film> getDay4Films() {
        return getFilms(Week.getDay(3));
    }

    public List<Film> getDay5Films() {
        return getFilms(Week.getDay(4));
    }

    public List<Film> getDay6Films() {
        return getFilms(Week.getDay(5));
    }

    public List<Film> getDay7Films() {
        return getFilms(Week.getDay(6));
    }

    public List<List<Film>> getWeekFilms() {
        List<List<Film>> weekFilms = new ArrayList<>();
        for (int i = 0; i < Week.size(); i++) {
            weekFilms.add(getFilms(Week.getDay(0)));
        }
        return weekFilms;
    }

    public List<Film> getBufferedFilms() {
        previousDaysFilms.forEach((film) -> film.setSessionList(null));//ставим null в сеансы т.к. они не валидны
        return previousDaysFilms;
    }

    public void close() {
        driver.quit();
    }
}
