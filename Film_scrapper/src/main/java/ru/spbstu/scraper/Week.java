package ru.spbstu.scraper;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class Week {
    private final String[] days = new String[7];

    public Week() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < days.length; i++) {
            days[i] = simpleDateFormatter.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
    }

    public String getDay(int i) {
        try {
            return days[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("It`s only 7 days in the week, input parameter should be 0-6. It will be set today instead.");
            return days[0];
        }
    }

    public String[] getDays() {
        return days;
    }
}
