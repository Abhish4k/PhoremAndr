package com.example.phoremandr.firebase_messaging_services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.phoremandr.R;
import com.example.phoremandr.activities.DashboardActivity;
import com.example.phoremandr.utils.AppValidator;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    // Override onNewToken to get new token
    @Override
    public void onNewToken(@NonNull String token)
    {
        AppValidator.logData("messagingToken", "Refreshed token: " + token);

    }

    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage)
    {
        AppValidator.logData("receiveNotification","" + remoteMessage.getNotification());
        if (remoteMessage.getNotification() != null) {
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(),
                    remoteMessage.getNotification().getChannelId());

        }
    }

//    @SuppressLint("RemoteViewLayout")
    private RemoteViews getCustomDesign(String title,
                                        String message)
    {
       RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tvTitle, title);
        remoteViews.setTextViewText(R.id.tvMsg, message);

        return remoteViews;
    }


    // Method to display the notifications
    public void showNotification(String title, String message, String channelId) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, DashboardActivity.class);
        // Assign channel ID
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }


        Uri uri = null;

        String appPackage = "android.resource://com.example.phoremandr";
        if (channelId != null) {

            switch (channelId) {
                case "emergencyAlarmChannel":
                    uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.emergency_alarm);
                    break;
                case "alarmChannel":
                    uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.alarm);
                    break;
                case "alarmToneChannel":
                    uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.alarm_tone);
                    break;
                case "alertAlarmChannel":
                    uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.alert_alarm);
                    break;
            }
        } else {
            channelId = "alarmChannel";
            uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.emergency_alarm);
        }

        AppValidator.logData("channelId", "" + channelId);
        AppValidator.logData("uri", "" + uri);

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder( this.getApplicationContext(),
                channelId)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.app_logo)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setContentIntent(pendingIntent);

      //  builder.setContent( getCustomDesign(title, message));
        NotificationManager notificationManager
                = (NotificationManager)getSystemService(
                Context.NOTIFICATION_SERVICE);

        // Check if the Android Version is greater than Oreo

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channelId, "web_app",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }
    }