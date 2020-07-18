FROM illlyaaaa/tar

WORKDIR /home/scraper

COPY target/Film_scraper-1.0-SNAPSHOT.jar /home/scraper

EXPOSE 8080

ENTRYPOINT ["java", "-jar","-Dwebdriver.gecko.driver=/home/scraper/geckodriver", "Film_scraper-1.0-SNAPSHOT.jar"]