package com.example.code_challenge;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcastBuilder extends BroadcastReceiver {

    final int NOTIFICATION_ID = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        int seasonNumber = intent.getIntExtra("season_number", 1);
        Intent tempIntent = new Intent(context, MMCWinner.class);
        PendingIntent onClickIntent = PendingIntent.getActivity(
                context, NOTIFICATION_ID,
                tempIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder= new NotificationCompat.Builder(context, "code_challenge")
                .setSmallIcon(R.drawable.my_mommy_icon)
                .setContentTitle("MyMommy Season Results!")
                .setContentText("The current MyMommy season is now complete! Click here to see who topped the charts.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(onClickIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
