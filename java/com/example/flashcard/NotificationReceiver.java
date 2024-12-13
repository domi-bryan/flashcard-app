package com.example.flashcard;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Intents
        Intent repeatedIntent = new Intent (context, SCREEN1_MainActivity.class);
        repeatedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 100, repeatedIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Notification elements
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "remindMe")
                .setContentIntent(pendingIntent2)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FLASHCARD TIME!")
                .setContentText("Review your flashcards daily to maximize retention!")
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }

}
