package ru.spbstu.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
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
    private static String description;
    private static double rating;
    private static final List<Film> previousDaysFilms = new ArrayList<>();

    static {
        System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
        url = "https://www.kinopoisk.ru/";
        cityUrl = "afisha/city/2/";
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("localhost:8888");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        driver = new FirefoxDriver(new FirefoxOptions(capabilities));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("general.useragent.override", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/82.0.4083.0 Safari/537.36");
    }

    private List<String> getFilmHrefs(String day) {//???????? ?????? ?? ????? ???????? ?? ??????
        List<String> filmHrefs = new ArrayList<>();
        List<WebElement> filmWebElements = driver.findElements(By.xpath("//div[@class=\"title _FILM_\"]/div/p"));
        for (WebElement film : filmWebElements) {
            if (film.findElement(By.tagName("u")).getAttribute("id").contains(day)) {//???? ????? ???? ? ???? ????
                filmHrefs.add(film.findElement(By.tagName("a")).getAttribute("href"));//?? ????????? ?????? ?? ????
            } else {
                break;//??????? ?????? ???? ?????? ? ???? ????, ????? ??? ??? ??? ?? ??????????
            }
        }
        return filmHrefs;
    }

    private List<Session> getFilmSessionList(String filmHref) {
        List<Session> sessionList = new ArrayList<>();
        driver.get(filmHref + cityUrl);//???? ?????? ??????????? ??? ?? ???????? ? ???????? ??????? ??????
        List<WebElement> schedulerItems = driver.findElements(By.xpath("//*[@class=\"schedule-item\"]"));//???????? ??????? ? ????????? ????????? ? ???????? ? ???
        for (WebElement item : schedulerItems) {
            String cinemaName = item.findElement(By.xpath(".//*[@class=\"schedule-item__left\"]//a")).getText();//???????? ???????? ??????????
            //???????? ?????? ?????? ??????? ? ??????????
            List<WebElement> sessionTimes = item.findElements(By.xpath(".//*[@class=\"schedule-item__right\"]//*[@class=\"schedule-item__session-button\"]"));
            List<String> sessionTimesInString = new ArrayList<>();
            for (WebElement sessionTime : sessionTimes) {//???????????? ?? ? String
                sessionTimesInString.add(sessionTime.getText());
            }
            sessionList.add(new Session(cinemaName, sessionTimesInString));
        }
        return sessionList;
    }

    public List<Film> getFilms(String day) {
        driver.get(url + cityUrl + "day_view/" + day);
        List<Film> thisDayFilms = new ArrayList<>();
        for (String href : getFilmHrefs(day)) {//???? ?? ?????? ?? ????? ???????? ?? ?????? ? ???? ????
            driver.get(href);//????????? ?? ???????? ? ???????
            String filmTitle = driver.findElement(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[1]/div[1]/div/h1/span")).getText();//???????? ???????? ??????
            Optional<Film> previousDayFilm = previousDaysFilms.stream().filter(s -> s.getTitle().contains(filmTitle)).findAny();//???? ????? ????? ? ??? ????????????? ????
            if (previousDayFilm.isPresent()) {//???? ????? ??? ? ?????????? ???
                previousDayFilm.get().setSessionList(getFilmSessionList(href));//?? ?????? ?????? ??????
                thisDayFilms.add(previousDayFilm.get());//????????? ??? ? ?????? ??????? ? ?????? ????
            } else {//???? ? ????????? ??? ?????? ?????? ?? ????
                actors = driver.findElement(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[2]/div[2]/div/div[1]/ul")).getText();//??????? ???????
                actors = actors.substring(0, actors.indexOf('\n', actors.indexOf('\n', actors.indexOf('\n', actors.indexOf('\n') + 1) + 1) + 1));
                description = driver.findElement(By.className("styles_paragraph__2Otvx")).getText();
                description = description.length() > 252 ? description.substring(0, 252) + "..." : description;
                try {
                    rating = Double.parseDouble(driver.findElement(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[1]/div[2]/div/div[1]/span[1]/span")).getText());
                } catch (NumberFormatException exception) {
                    rating = 0.0;
                    exception.printStackTrace();
                }
                List<WebElement> filmInfoTableRows = driver.findElements(By.xpath("//html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[3]/div/div/div[2]/div[1]/div/div"));//??????? ???????? ? ????? ? ??????
                By rowValueXpath = By.xpath("./div[2]");
                for (WebElement row : filmInfoTableRows) {//????? ?? ??? ? ???? ???????????? ??? ??????????????
                      switch (row.findElement(By.xpath("./div[1]")).getText()/*??? ????*/) {
                        case "??? ????????????":
                            year = row.findElement(rowValueXpath).getText();
                            break;
                        case "??????":
                            country = row.findElement(rowValueXpath).getText();
                            break;
                        case "????????":
                            director = row.findElement(rowValueXpath).getText();
                            break;
                        case "????":
                            genre = row.findElement(rowValueXpath).getText().replace(", ..." + '\n' + "?????", "");
                            break;
                        case "?????":
                            duration = row.findElement(rowValueXpath).getText();
                            break;
                    }
                }
                Film newFilm = new Film(filmTitle,year,country,director,genre,duration, actors, description, rating, getFilmSessionList(href));
                thisDayFilms.add(newFilm);//????????? ????? ? ?????? ??????? ? ?????? ????
                previousDaysFilms.add(newFilm);//? ??? ?? ? ?????? ??? ?????????????, ?.?. ?? ??? ????????? ?????? ???
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
        previousDaysFilms.forEach((film) -> film.setSessionList(null));//?????? null ? ?????? ?.?. ??? ?? ???????
        return previousDaysFilms;
    }

    public void close() {
        driver.quit();
    }
}
