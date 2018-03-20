package com.example.nicke.mojaevidencija.Util;

import android.content.Context;
import android.widget.Toast;

import com.example.nicke.mojaevidencija.Model.RemainingTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 3/1/2018.
 */

public class Utill {

    public static String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);
        return sdf.format(date);
    }

    public static Date getFormattedDate(String dateString, String timeString) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.GERMANY);
        try {
            Date date = sdf.parse(dateString + " " + timeString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return calendar.getTime();
    }

    public static String getFormattedCurrentTime() {
        DateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        Date date;
        try {
            date = sdf.parse(CalendarHelper.getCurrentTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return sdf.format(date);
    }

    public static String getFormattedTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        Date time = calendar.getTime();

        return sdf.format(time);
    }

    public static void displayRemainingTime(RemainingTime remainingTime, Context context) {
        remainingTime.setContext(context);
        Toast.makeText(context, remainingTime.toString(), Toast.LENGTH_LONG).show();
    }
}
