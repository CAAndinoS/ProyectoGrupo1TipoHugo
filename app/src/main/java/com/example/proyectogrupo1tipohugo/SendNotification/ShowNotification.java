package com.example.proyectogrupo1tipohugo.SendNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.proyectogrupo1tipohugo.MainActivity;
import com.example.proyectogrupo1tipohugo.R;

import java.util.Random;

;

public class ShowNotification {

    public static void ShowNotif(Context context, String title, String message, String page) {
        String CHANNEL_ID = "NOTICE";
        String CHANNEL_NAME = "NOTICE";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Intent acIntent = new Intent(context, MainActivity.class);
        acIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                .setColor(ContextCompat.getColor(context, R.color.Red))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        int random = new Random().nextInt(9999 - 1) + 1;
        notificationManagerCompat.notify(random, nBuilder.build());
    }
}
