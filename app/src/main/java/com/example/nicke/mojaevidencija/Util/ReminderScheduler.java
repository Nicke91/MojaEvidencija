package com.example.nicke.mojaevidencija.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.Model.RemainingTime;
import com.example.nicke.mojaevidencija.Model.Reminder;
import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 3/4/2018.
 */

public class ReminderScheduler {

    public static final String NO_REPEAT = "no_repeat";
    public static final String REPEAT_EVERY_DAY = "Svaki dan";
    public static final String REPEAT_MINUTES_FIVE = "Na 5 minuta";
    public static final String REPEAT_MINUTES_FIFTEEN = "Na 15 minuta";
    public static final String REPEAT_HALF_HOUR = "Na 30 minuta";
    public static final String REPEAT_HOUR = "Na svakih sat vremena";

    private static final int ADD_DAY = 1;
    private static final int ADD_MINUTES_FIVE = 5;
    private static final int ADD_MINUTES_FIFTEEN = 15;
    private static final int ADD_HALF_HOUR = 30;
    private static final int ADD_HOUR = 60;

    private static final long REMINDER_NO_REPEAT = 0;
    private static final long REMINDER_REPEAT_DAILY = AlarmManager.INTERVAL_DAY;
    private static final long REMINDER_MINUTES_FIVE = 1000*60*5L;
    private static final long REMINDER_MINUTES_FIFTEEN = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    private static final long REMINDER_HALF_HOUR = AlarmManager.INTERVAL_HALF_HOUR;
    private static final long REMINDER_REPEAT_HOUR = AlarmManager.INTERVAL_HOUR;

    public static final String EXTRA_TASK_ID = "extra_task_id";
    public static boolean showInfo = false;

    public static void setReminder(Reminder reminder, Context context) {

        boolean hasId = false;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent;

        Task task = PrefManager.getTask(context);
        if (task.getId() != null) {
            hasId = true;
        }
        if (reminder != null && hasId && alarmManager != null) {

            intent.putExtra(EXTRA_TASK_ID, task.getId().toString());
            alarmIntent = PendingIntent.getBroadcast(context, task.getId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(alarmIntent);

            String date = reminder.getDate();
            String time = reminder.getTime();
            String repeat = reminder.getRepeat();

            Calendar calendarCurrent = Calendar.getInstance();

            Calendar calendarToSet = Calendar.getInstance();
            calendarToSet.setTime(Utill.getFormattedDate(date, time));

            //KOMENTAR BR. 1
            //showInfo == true; provera da li da se poruka ispise za koliko ce alarm zvoniti (notifikacija), ukoliko je alarm podesen tog trenutka
            //Vidi komentar br 2
            //showInfo == false; sistem je rebutovan pa se ponovo postavlja alarm, ne pokazuj poruku
            // isto ako je telefon ugasen i vreme za alarm je isteklo dok je telefon bio ugasen, i alarm nema ponavljanja (svaki dan, na 5 minuta) onda pusti odmah notifikaciju
            switch (repeat) {
                case NO_REPEAT:
                    if (!showInfo) {
                        while (calendarToSet.before(calendarCurrent)) {
                            calendarToSet = CalendarHelper.getAdvancedDate(calendarToSet, ADD_DAY);
                        }
                        setAlarm(alarmManager, calendarToSet, REMINDER_NO_REPEAT, alarmIntent);
                    } else {
                        setAlarm(alarmManager, calendarCurrent, REMINDER_NO_REPEAT, alarmIntent);
                    }
                    break;
                case REPEAT_EVERY_DAY:
                    while (calendarToSet.before(calendarCurrent)) {
                        calendarToSet = CalendarHelper.getAdvancedDate(calendarToSet, ADD_DAY);
                    }
                    setAlarm(alarmManager, calendarToSet, REMINDER_REPEAT_DAILY, alarmIntent);
                    break;
                case REPEAT_MINUTES_FIVE:
                    while (calendarToSet.before(calendarCurrent)) {
                        calendarToSet = CalendarHelper.getAdvancedTime(calendarToSet, ADD_MINUTES_FIVE);
                    }
                    setAlarm(alarmManager, calendarToSet, REMINDER_MINUTES_FIVE, alarmIntent);
                    break;
                case REPEAT_MINUTES_FIFTEEN:
                    while (calendarToSet.before(calendarCurrent)) {
                        calendarToSet = CalendarHelper.getAdvancedTime(calendarToSet, ADD_MINUTES_FIFTEEN);
                    }
                    setAlarm(alarmManager, calendarToSet, REMINDER_MINUTES_FIFTEEN, alarmIntent);
                    break;
                case REPEAT_HALF_HOUR:
                    while (calendarToSet.before(calendarCurrent)) {
                        calendarToSet = CalendarHelper.getAdvancedTime(calendarToSet, ADD_HALF_HOUR);
                    }
                    setAlarm(alarmManager,calendarToSet, REMINDER_HALF_HOUR, alarmIntent);
                    break;
                case REPEAT_HOUR:
                    while (calendarToSet.before(calendarCurrent)) {
                        calendarToSet = CalendarHelper.getAdvancedTime(calendarToSet, ADD_HOUR);
                    }
                    setAlarm(alarmManager,calendarToSet, REMINDER_REPEAT_HOUR, alarmIntent);
                    break;
            }

            Date dateSet = calendarToSet.getTime();
            //KOMENTAR BR. 2
            // ovde se vrsi provera da li treba poruka ili ne, pomenuta u komentaru br. 1
            if (showInfo) {
                Utill.displayRemainingTime(CalendarHelper.getRemainingTime(dateSet, calendarCurrent.getTime()), context);
            }

        } else if (reminder == null && hasId) {
            intent.putExtra(EXTRA_TASK_ID, task.getId().toString());
            alarmIntent = PendingIntent.getBroadcast(context, task.getId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmManager != null) {
                alarmManager.cancel(alarmIntent);
            }
        }
    }

    private static void setAlarm(AlarmManager alarmManager, Calendar c, long interval, PendingIntent alarmIntent) {
        if (Build.VERSION.SDK_INT < 19) {
            if (interval == REMINDER_NO_REPEAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmIntent);
                return;
            }
        } else {
            if (interval == REMINDER_NO_REPEAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmIntent);
                return;
            }
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), interval, alarmIntent);
    }

    public static void cancelReminder(Task task, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TASK_ID, task.getId());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getId().intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }

    static void setReminderAfterBoot(Context context) {
        List<Category> categoryList = Category.listAll(Category.class);
        List<Task> taskList;
        taskList = new ArrayList<>();
        List<Task> tempTaskList;
        showInfo = false;

        if (categoryList.size() > 0) {

            for (Category category : categoryList) {
                tempTaskList = category.getTaskList();
                taskList.addAll(tempTaskList);
            }
            if (taskList.size() > 0) {
                for (Task task : taskList) {
                    if (task.getReminder() != null) {
                        PrefManager.addTask(task, context);
                        setReminder(task.getReminder(), context);
                    }
                }
            }
        }
    }
}
