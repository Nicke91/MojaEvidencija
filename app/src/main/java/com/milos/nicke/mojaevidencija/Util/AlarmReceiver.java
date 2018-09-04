package com.milos.nicke.mojaevidencija.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.milos.nicke.mojaevidencija.Model.Task;

/**
 * Created on 3/3/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ReminderScheduler.setReminderAfterBoot(context);
        } else {
            String id = intent.getStringExtra(ReminderScheduler.EXTRA_TASK_ID);
            if (id != null) {
                Task task = Task.findById(Task.class, Long.parseLong(id));
                if (task != null) {
                    NotificationHelper.createNotification(context, task);
                }
            }
        }
    }
}
