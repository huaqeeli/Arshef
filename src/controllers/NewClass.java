package controllers;

import static controllers.AppDate.getDay;
import static controllers.AppDate.getMonth;
import static controllers.AppDate.getYear;
import java.io.IOException;

public class NewClass {
    
    static Config config = new Config();
    
    public static void main(String[] args) throws IOException {
         System.out.println(getRemainingDays("1443/02/07"));
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
