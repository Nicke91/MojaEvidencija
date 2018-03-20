package com.example.nicke.mojaevidencija.Util;

import com.example.nicke.mojaevidencija.Model.RemainingTime;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 3/4/2018.
 */

public class CalendarHelper {

    private static final int INTERVAL_WEEK= 1000*60*60*24*7;
    private static final long INTERVAL_MONTH = 1000*60*60*24*30L;

    public static String getCurrentTime() {
       Calendar calendar = Calendar.getInstance();

       int hour = calendar.get(Calendar.HOUR_OF_DAY);
       int minute = calendar.get(Calendar.MINUTE);

       if (minute < 50) {
           minute = minute + 10;
       } else {
           hour = hour + 1;
           minute = 10 - (60 - minute);
       }

       return hour + ":" + minute;
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        return calendar.getTime();
    }

    public static Calendar getAdvancedDate(Calendar calendar, int interval) {
        calendar.add(Calendar.DATE, interval);
        return calendar;
    }

    public static Calendar getAdvancedTime(Calendar calendar, int minutes) {
        calendar.add(Calendar.MINUTE, minutes);
        return calendar;
    }

    public static RemainingTime getRemainingTime(Date dateSpecified, Date currentDateTime) {
        long difference = dateSpecified.getTime() - currentDateTime.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long elapsedMinutes = difference / minutesInMilli;

        return new RemainingTime(elapsedDays, elapsedHours, elapsedMinutes);
    }
}
