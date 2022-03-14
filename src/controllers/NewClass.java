package controllers;

import static controllers.AppDate.getDay;
import static controllers.AppDate.getMonth;
import static controllers.AppDate.getYear;
import java.io.IOException;
import java.text.ParseException;

public class NewClass {
    
    static Config config = new Config();
    
    public static void main(String[] args) throws IOException, ParseException {
        // System.out.println(getRemainingDays("1443/02/01"));
         System.out.println(HijriCalendar.getSimpleWeekday("05/08/1443"));
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
            intvalue = (day-currentDay)+((month-currentMont)*30)+( (year - currentYear)*360);
            value = Integer.toString(intvalue);
        }
        return value;
    }
}
