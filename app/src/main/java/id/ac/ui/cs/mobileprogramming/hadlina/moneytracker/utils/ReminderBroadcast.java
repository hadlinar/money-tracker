package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "moneytracker")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Money Tracker Reminder")
                .setContentText("30 mins before due date!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
