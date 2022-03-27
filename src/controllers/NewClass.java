package controllers;

import static controllers.AppDate.getDay;
import static controllers.AppDate.getMonth;
import static controllers.AppDate.getYear;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewClass {

    static Config config = new Config();

    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        System.out.println(dtf.format(date));
        System.out.println(day);
        if (calendar.get(Calendar.MONTH) + 1 < 10) {
            System.out.println("0" + month);
        } else {
            System.out.println(month);
        }
        System.out.println(year);
    }

    public static String getRemainingDays(String date) {
        String value = null;
        if (date != null) {
            int intvalue = 0;
            int day = Integer.parseInt(getDay(date));
            int month = Integer.parseInt(getMonth(date));
            int year = Integer.parseInt(getYear(date));
            int currentDay = HijriCalendar.getSimpleDay();
            int currentMont = HijriCalendar.getSimpleMonth();
            int currentYear = HijriCalendar.getSimpleYear();
            intvalue = (day - currentDay) + ((month - currentMont) * 30) + ((year - currentYear) * 360);
            value = Integer.toString(intvalue);
        }
        return value;
    }
}
