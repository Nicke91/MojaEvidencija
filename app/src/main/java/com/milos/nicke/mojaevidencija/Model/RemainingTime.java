package com.milos.nicke.mojaevidencija.Model;

import android.content.Context;

import com.example.nicke.mojaevidencija.R;
import com.milos.nicke.mojaevidencija.Util.AppConfig;

/**
 * Created on 3/12/2018.
 */

public class RemainingTime {
    private Context context;
    private long days;
    private long hours;
    private long minutes;

    public RemainingTime() {
    }

    public RemainingTime(long days, long hours, long minutes) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {

        String formattingText = "";

        String dayString = AppConfig.getSystemString(context, R.string.toast_time_desc_day_multiple);
        String dayValue = String.valueOf(days);
        if (dayValue.equals("1")) {
            dayString = AppConfig.getSystemString(context, R.string.toast_time_desc_day_one);
        }
        if (!dayValue.equals("0")) {
            formattingText += String.format(dayString, dayValue) + " ";
        }

        String hourValue = String.valueOf(hours);
        String hourString = AppConfig.getSystemString(context, R.string.toast_time_desc_hour_multiple);

        if (hourValue.equals("1")) {
            hourString = AppConfig.getSystemString(context, R.string.toast_time_desc_hour_one);
        }

        if (!hourValue.equals("0")) {
            formattingText += String.format(hourString, hourValue) + " ";
        }

        String minuteValue = String.valueOf(minutes);
        String minutesString = AppConfig.getSystemString(context, R.string.toast_time_desc_minute_multiple);

        if (minuteValue.equals("1")) {
            minutesString = AppConfig.getSystemString(context, R.string.toast_time_desc_minute_one);
        }

        if (!minuteValue.equals("0")) {
            formattingText += String.format(minutesString, minuteValue) + " ";
        }

        if (formattingText.trim().equals("")) {
            return AppConfig.getSystemString(context, R.string.toast_time_alarm_less_then_minute);
        }

        return AppConfig.getSystemString(context, R.string.toast_time_alarm_set_for) + " " + formattingText;
    }

}
