package controllers;

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
        for (int i = 1400; i <= 1490; i++) {
            year.getItems().addAll(Integer.toString(i));
        }
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

    public static String getDate(ComboBox day, ComboBox month, ComboBox year) {
        return  year.getValue().toString() + "/" + month.getValue().toString() + "/" +day.getValue().toString();
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

}
