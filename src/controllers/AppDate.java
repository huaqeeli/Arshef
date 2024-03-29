package controllers;

import Validation.FormValidation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class AppDate {

    public static void setDateValue(ComboBox day, ComboBox month, ComboBox year) {
        for (int i = 1; i <= 30; i++) {
            if (i < 10) {
                day.getItems().addAll("0" + Integer.toString(i));
            } else {
                day.getItems().addAll(Integer.toString(i));
            }
        }
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                month.getItems().addAll("0" + Integer.toString(i));
            } else {
                month.getItems().addAll(Integer.toString(i));
            }
        }
        int yearvalue = HijriCalendar.getSimpleYear();
        for (int i = 0; i <= 50; i++) {
            year.getItems().addAll(Integer.toString(yearvalue));
            yearvalue = yearvalue - 1;
        }
    }
    public static void setCompletionDateValue(ComboBox day, ComboBox month, ComboBox year) {
        for (int i = 1; i <= 30; i++) {
            if (i < 10) {
                day.getItems().addAll("0" + Integer.toString(i));
            } else {
                day.getItems().addAll(Integer.toString(i));
            }
        }
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                month.getItems().addAll("0" + Integer.toString(i));
            } else {
                month.getItems().addAll(Integer.toString(i));
            }
        }
        int yearvalue = HijriCalendar.getSimpleYear()+2;
        for (int i = 0; i <= 50; i++) {
            year.getItems().addAll(Integer.toString(yearvalue));
            yearvalue = yearvalue - 1;
        }
    }
    public static void setGregorianDateValue(ComboBox day, ComboBox month, ComboBox year) {
        for (int i = 1; i <= 30; i++) {
            if (i < 10) {
                day.getItems().addAll("0" + Integer.toString(i));
            } else {
                day.getItems().addAll(Integer.toString(i));
            }
        }
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                month.getItems().addAll("0" + Integer.toString(i));
            } else {
                month.getItems().addAll(Integer.toString(i));
            }
        }
        int yearvalue = getGregorianYear();
        for (int i = 0; i <= 50; i++) {
            year.getItems().addAll(Integer.toString(yearvalue));
            yearvalue = yearvalue - 1;
        }
    }

    public static void setYearValue(ComboBox year) {
        int yearvalue = HijriCalendar.getSimpleYear();
        for (int i = 0; i <= 50; i++) {
            year.getItems().addAll(Integer.toString(yearvalue));
            yearvalue = yearvalue - 1;
        }
    }

    public static void setGregorianYearValue(ComboBox year) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int yearvalue = calendar.get(Calendar.YEAR);
        for (int i = 0; i <= 50; i++) {
            year.getItems().addAll(Integer.toString(yearvalue));
            yearvalue = yearvalue - 1;
        }
    }

    public static void setCurrentYear(ComboBox year) {
        year.setValue(HijriCalendar.getSimpleYear());
    }

    public static void setCurrentGregorianYear(ComboBox year) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        year.setValue(calendar.get(Calendar.YEAR));
    }

    public static void setCurrentDate(ComboBox day, ComboBox month, ComboBox year) {
        if (HijriCalendar.getSimpleDay() < 10) {
            day.setValue("0" + HijriCalendar.getSimpleDay());
        } else {
            day.setValue(HijriCalendar.getSimpleDay());
        }
        if (HijriCalendar.getSimpleMonth() < 10) {
            month.setValue("0" + HijriCalendar.getSimpleMonth());
        } else {
            month.setValue(HijriCalendar.getSimpleMonth());
        }
        year.setValue(HijriCalendar.getSimpleYear());
    }

    public static void setGregorianCurrentDate(ComboBox day, ComboBox month, ComboBox year) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            day.setValue("0" + calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        }
        if (calendar.get(Calendar.MONTH)+1 < 10) {
            month.setValue("0" + (calendar.get(Calendar.MONTH)+1));
        } else {
            month.setValue(calendar.get(Calendar.MONTH)+1);
        }
        year.setValue(calendar.get(Calendar.YEAR));
    }
    public static String getGregorianCurrentDate() {
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dateValue = dtf.format(date);
        return dateValue;
    }
    public static int getGregorianYear() {
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dateValue = calendar.get(Calendar.YEAR);
        return dateValue;
    }

    public static String getDate(ComboBox day, ComboBox month, ComboBox year) {
        return year.getValue().toString() + "/" + month.getValue().toString() + "/" + day.getValue().toString();
    }

    public static String getDay(String date) {
        String day = null;
        CharSequence seq1 = "/";
        if (date != null && date.contains(seq1)) {
            String[] parts = date.split("/");
            day = parts[2];
        }
        return day;
    }

    public static String getMonth(String date) {
        String month = null;
        CharSequence seq1 = "/";
        if (date != null && date.contains(seq1)) {
            String[] parts = date.split("/");
            month = parts[1];
        }
        return month;
    }

    public static String getYear(String date) {
        String year = null;
        CharSequence seq1 = "/";
        if (date != null && date.contains(seq1)) {
            String[] parts = date.split("/");
            year = parts[0];
        }
        return year;
    }

    public static void setSeparateDate(ComboBox day, ComboBox month, ComboBox year, String date) {
        day.setValue(getDay(date));
        month.setValue(getMonth(date));
        year.setValue(getYear(date));
    }

    public static String getDifferenceDate(int day1, int month1, int year1, int day2, int month2, int year2) {
        String value = null;
        if (day2 < day1) {
            day2 = day2 + 30;
            month2 = month2 - 1;
        }
        if (month2 < month1) {
            month2 = month2 + 12;
            year2 = year2 - 1;
        }
        if (year2 < year1) {
            FormValidation.showAlert(null, "ادخل تاريخ صحيح", Alert.AlertType.ERROR);
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
            if (deffmonth == 1 || deffmonth == 2) {
                monthText = "شهرا";
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
            if (deffday == 0 && deffyear == 0) {
                System.out.println();
                value = deffmonth + " " + monthText;
            } else if (deffday == 0 && deffyear != 0) {
                value = deffyear + yearText + " و" + deffmonth + " " + monthText;
            } else if (deffmonth == 0) {
                value = deffday + " " + dayText;
            } else if (deffyear <= 0) {
                value = deffmonth + monthText + " و" + deffday + " " + dayText;
            } else if (deffyear == 1 || deffyear == 2) {
                value = yearText + " و" + deffmonth + monthText + " و" + deffday + " " + dayText;
            } else {
                value = deffyear + yearText + " و" + deffmonth + monthText + " و" + deffday + " " + dayText;
            }
        }
        return value;
    }

    public static int getRemainingDays(String date) {
        String value = null;
        int intvalue = 0;
        if (date != null) {

            int day = Integer.parseInt(getDay(date));
            int month = Integer.parseInt(getMonth(date));
            int year = Integer.parseInt(getYear(date));
            int currentDay = HijriCalendar.getSimpleDay();
            int currentMont = HijriCalendar.getSimpleMonth();
            int currentYear = HijriCalendar.getSimpleYear();
            intvalue = (day - currentDay) + ((month - currentMont) * 30) + ((year - currentYear) * 360);
//            value = Integer.toString(intvalue);
        }
        return intvalue;
    }
}
