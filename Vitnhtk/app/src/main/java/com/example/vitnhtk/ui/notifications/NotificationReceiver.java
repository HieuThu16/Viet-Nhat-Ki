package com.example.vitnhtk.ui.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import com.example.vitnhtk.MainActivity;
import com.example.vitnhtk.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification when the alarm goes off
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, "notification_channel")
                .setContentTitle("Thông báo")
                .setContentText("Đây là thông báo của bạn.")
                .setSmallIcon(R.drawable.ic_notification)  // Replace with your icon
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
