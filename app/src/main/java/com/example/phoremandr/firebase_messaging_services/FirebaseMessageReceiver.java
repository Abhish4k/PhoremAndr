package com.example.phoremandr.firebase_messaging_services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.phoremandr.R;
import com.example.phoremandr.activities.DashboardActivity;
import com.example.phoremandr.helper.SharedPrefHelper;
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

    @SuppressLint("RemoteViewLayout")
    private RemoteViews getCustomDesign(String title,
                                        String message)
    {
       RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tvTitle, title);
        remoteViews.setTextViewText(R.id.tvMsg, message);

        return remoteViews;
    }
    // Method to display the notifications
    public void showNotification(String title,
                                 String message, String channelId)
    {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, DashboardActivity.class);
        // Assign channel ID
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }


        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        Uri uri = null;
        AppValidator.logData("channelId","" + channelId);
        if(channelId != null){
            String appPackage = "android.resource://com.example.phoremandr/";
            switch (channelId) {
                case "emergencyAlarmChannel":
                    uri = Uri.parse(appPackage+ R.raw.emergency_alarm);
                    break;
                case "alarmChannel":
                    uri = Uri.parse(appPackage + R.raw.alarm);
                    break;
                case "alarmToneChannel":
                    uri = Uri.parse(appPackage + R.raw.alarm_tone);
                    break;
                case "alertAlarmChannel":
                    uri = Uri.parse(appPackage + R.raw.alert_alarm);
                    break;
            }
        }else {
             uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
             channelId = "alarmChannel";
        }

        AppValidator.logData("uri","" + uri);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channelId)

                .setSmallIcon(R.drawable.app_logo)
                .setAutoCancel(true)
                .setSound(uri)
                .setVibrate(new long[] { 1000, 1000, 1000,
                        1000, 1000 })
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);


        builder = builder.setContent(
                getCustomDesign(title, message));

        NotificationManager notificationManager
                = (NotificationManager)getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channelId, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }
}