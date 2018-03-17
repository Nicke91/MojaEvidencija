package com.example.nicke.mojaevidencija.Util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.R;
import com.example.nicke.mojaevidencija.TaskDetailsActivity;

import java.util.List;

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
