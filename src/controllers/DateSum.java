package controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateSum {

    public static void main(String args[]) {
//        boolean DateDifference = dateDifference(Integer.parseInt("1441"), Integer.parseInt("8"), Integer.parseInt("12"), Integer.parseInt("1441"), Integer.parseInt("10"), Integer.parseInt("12"));
        Calendar birthDay = new GregorianCalendar(1980, Calendar.MAY, 05);
        Calendar today = new GregorianCalendar();
        today.setTime(new Date());
        int yearsInBetween = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int monthsDiff = today.get(Calendar.MONTH) - birthDay.get(Calendar.MONTH);
        long ageInMonths = yearsInBetween * 12 + monthsDiff;
        long age = yearsInBetween;
        System.out.println("Number of months since James gosling born : " + ageInMonths);
        System.out.println("Sir James Gosling's age : " + age);

    }

    public static boolean dateDifference(String startdat, String enddat) {
        boolean state = true;
        LocalDate startdate = LocalDate.parse(startdat);
        LocalDate endtdate = LocalDate.parse(enddat);
        long day = ChronoUnit.DAYS.between(startdate, endtdate);
        long year = ChronoUnit.YEARS.between(startdate, endtdate);
        long month = ChronoUnit.MONTHS.between(startdate, endtdate);
        String value = null;
        if (day <= 30 && day <= 10) {
            value = "أيام" + day;
        } else if (day <= 30 && day > 10) {
            value = "يوما" + day;
        } else if (day > 30 && day < 360) {
            day = day - (month * 30);
            value = month + "أشهر" + "و" + day + "يوما";
        }
        System.out.println(value);
//        System.out.println(startdate + "---" + endtdate); //3
//        System.out.println(d);
//        if (d < 0) {
//            state = false;
//            System.out.println("تاريخ النهاية اقل من البداية");
//        }
//        System.out.println(state);
        return state;
    }

//    public static void dateDifference(String startdat, String enddate) {
//
////        try {
////            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("ar"));
////            Date firstDate = sdf.parse(startdat);
////            Date secondDate = sdf.parse(enddate);
////        long day = ChronoUnit.DAYS.between(firstDate, secondDate);
////        long year = ChronoUnit.YEARS.between(startdate, endtdate);
////        long month = ChronoUnit.MONTHS.between(startdate, endtdate);
////        String value = null;
////        if (day <= 30 && day <= 10) {
////                value = "أيام" + day;
////            } else if (day <= 30 && day > 10) {
////                value = "يوما" + day;
////            } else if (day > 30 && day < 360) {
////                day = day - (month * 30);
////                value = "أشهر"+month+"و"+"يوما" + day;
////            }
////        System.out.println(value); 
////        } catch (ParseException ex) {
////            Logger.getLogger(DateSum.class.getName()).log(Level.SEVERE, null, ex);
////        }
//
//    }
    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
