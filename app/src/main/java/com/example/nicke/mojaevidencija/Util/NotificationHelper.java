package com.example.nicke.mojaevidencija.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.R;
import com.example.nicke.mojaevidencija.TaskDetailsActivity;

/**
 * Created on 3/9/2018.
 */

public class NotificationHelper {

    public static final String NOTIFICATION_EXTRA_TASK_ID = "extra_task_id";

    public static void createNotification(Context context, Task task) {
        Intent activityIntent = new Intent(context, TaskDetailsActivity.class);
        activityIntent.putExtra(NOTIFICATION_EXTRA_TASK_ID, task.getId());
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

       PendingIntent pendingIntent = PendingIntent.getActivity(context,task.getId().intValue(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String description = task.getReminder().getDescription();

        Notification notification = new Notification.Builder(context)
                .setContentTitle(task.getTitle())
                .setContentText(description)
                .setSmallIcon(R.drawable.app_icon_notification)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] {0, 500, 100, 500, 1000})
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(task.getId().intValue(), notification);
        }

        setRingtone(context);
    }

    private static void setRingtone(Context context) {
        Uri notificationRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final MediaPlayer mp = MediaPlayer.create(context, notificationRingtone);

        if (mp != null) {
            mp.setLooping(false);
            mp.start();
        }
    }
}
