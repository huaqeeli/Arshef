package controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateSum {

    public static void main(String args[]) {

        getdiff(01, 01, 1441, 01, 02, 1442);

    }

    public static void getdiff(int day1, int month1, int year1, int day2, int month2, int year2) {
        if (day2 < day1) {
            day2 = day2 + 30;
            month2 = month2 - 1;
        }
        if (month2 < month1) {
            month2 = month2 + 12;
            year2 = year2 - 1;
        }
        if (year2 < year1) {
            System.out.println("ادخل تاريخ صحيح");
        } else {
            int deffday = day2 - day1;
            int deffmonth = month2 - month1;
            int deffyear = year2 - year1;
            String dayText = null, monthText = null, yearText = null;
            if (deffday == 1) {
                dayText = "يوم";
            } else if (deffday == 2) {
                dayText = "يومان";
            } else if (deffday <= 10 && deffday > 2) {
                dayText = "أيام";
            } else if (deffday > 10) {
                dayText = "يوما";
            }
            if (deffmonth == 1) {
                monthText = "شهر";
            } else if (deffmonth == 2) {
                monthText = "شهران";
            } else if (deffmonth <= 10 && deffmonth > 2) {
                monthText = "أشهر";
            } else if (deffmonth > 10) {
                monthText = "شهرا";
            }
            if (deffyear == 1) {
                yearText = "سنة";
            } else if (deffyear == 2) {
                yearText = "سنتان";
            } else if (deffyear <= 10 && deffmonth > 2) {
                yearText = "سنوات";
            } else if (deffyear > 10) {
                yearText = "سنة";
            }
            if (deffday == 0  && deffyear ==0) {
                System.out.println(deffmonth + " " + monthText);
            } else if (deffday == 0 && deffyear !=0 ) {
                System.out.println(deffyear + yearText + "و" +deffmonth + " " + monthText);
            } else if (deffmonth == 0 ) {
                System.out.println(deffday + " " + dayText);
            } else if (deffyear <= 0) {
                System.out.println(deffmonth + monthText + "و" + deffday + " " + dayText);
            } else if (deffyear == 1 || deffyear == 2) {
                System.out.println(yearText + "و" + deffmonth + monthText + "و" + deffday + " " + dayText);
            } else {
                System.out.println(deffyear + yearText + "و" + deffmonth + monthText + "و" + deffday + " " + dayText);
            }
        }
    }
//    public static boolean dateDifference(String startdat, String enddat) {
//        boolean state = true;
//        LocalDate startdate = LocalDate.parse(startdat);
//        LocalDate endtdate = LocalDate.parse(enddat);
//        long day = ChronoUnit.DAYS.between(startdate, endtdate);
//        long year = ChronoUnit.YEARS.between(startdate, endtdate);
//        long month = ChronoUnit.MONTHS.between(startdate, endtdate);
//        String value = null;
//        if (day <= 30 && day <= 10) {
//            value = "أيام" + day;
//        } else if (day <= 30 && day > 10) {
//            value = "يوما" + day;
//        } else if (day > 30 && day < 360) {
//            day = day - (month * 30);
//            value = month + "أشهر" + "و" + day + "يوما";
//        }
//        System.out.println(value);
////        System.out.println(startdate + "---" + endtdate); //3
////        System.out.println(d);
////        if (d < 0) {
////            state = false;
////            System.out.println("تاريخ النهاية اقل من البداية");
////        }
////        System.out.println(state);
//        return state;
//    }

    public static void dateDifferen(String startdat, String enddate) {

        LocalDate startdate = LocalDate.parse(startdat);
        LocalDate endtdate = LocalDate.parse(enddate);
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
            value = "أشهر" + month + "و" + "يوما" + day;
        }
        long theday = 0;
        long themonth = 0;
        long theyear = 0;
        theday = day - ((month - (year * 12)) * 30 + year * 360);
        System.out.println(theday);
        System.out.println(month - (year * 12));
        System.out.println(year);
        System.out.println(day);
        System.out.println(month);

    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

}
