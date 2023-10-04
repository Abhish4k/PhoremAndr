package com.example.phoremandr.firebase_messaging_services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.phoremandr.R;
import com.example.phoremandr.activities.DashboardActivity;
import com.example.phoremandr.helper.SharedPrefHelper;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    // Override onNewToken to get new token

    public static SharedPrefHelper sharedPrefHelper;
    @Override
    public void onNewToken(@NonNull String token)
    {
        AppValidator.logData("messagingToken", "Refreshed token: " + token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        AppValidator.logData("receiveNotification","" + remoteMessage.getNotification().getSound());

        showNotification(
                getApplicationContext(),
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                remoteMessage.getNotification().getSound());
        super.onMessageReceived(remoteMessage);
    }

    @SuppressLint("RemoteViewLayout")
    public  RemoteViews getCustomDesign(Context context,String title, String message )
    {
       RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tvTitle, title);
        remoteViews.setTextViewText(R.id.tvMsg, message);

        return remoteViews;
    }

    // Method to display the notifications
 public void showNotification(Context context,String title, String message,  String sound) {

        sharedPrefHelper = new SharedPrefHelper();
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getActivity
                    (context, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity
                    (context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }



        Uri soundUri = null;
        String channel_id = "";
        if (sound != null) {
            switch (sound) {
                case "alarmChannel":
                    channel_id = context.getString(R.string.default_notification_channel_id);
                    soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);
                    break;

                case "emergencyAlarmChannel":
                    channel_id = context.getString(R.string.emergency_alarm_channel_id);
                    soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emergency_alarm);
                    break;

                case "alarmToneChannel":
                    channel_id = context.getString(R.string.alarm_tone_channel_id);
                    soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_tone);
                    break;

                case "alertAlarmChannel":
                    channel_id = context.getString(R.string.alert_alarm_channel_id);
                    soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm);
                    break;
                case "alert_alarm_channel":
                    channel_id = context.getString(R.string.default_notification_channel_id);
                    soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.alarm);
                    break;
            }
        } else {
            soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);
            channel_id = context.getString(R.string.default_notification_channel_id);
        }




        AppValidator.logData("uri", "This is uri response " +channel_id + "," + soundUri);

        if ( soundUri!=null) {
            // Check if the Android Version is greater than Oreo
            NotificationManager notificationManager
                    = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            createChannel( notificationManager, context);
                NotificationCompat.Builder builder
                        = new NotificationCompat
                        .Builder(context,context.getString(R.string.default_notification_channel_id) )
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);

                builder.setContent(getCustomDesign(context,title, message));
            AppValidator.logData("notificationChannel", " "  + soundUri);




            notificationManager.notify(0, builder.build());


        }

    }


    public void createChannel(NotificationManager notificationManager, Context context) {


        sharedPrefHelper = new SharedPrefHelper();

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(
                  context.getString(R.string.default_notification_channel_id),"web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            notificationChannel.setSound(Uri.parse(sharedPrefHelper.getValue(SharedPreferencesKeys.sound)),att);


            AppValidator.logData("channelIdSound", "" + notificationChannel.getSound() + ", " + notificationManager);

            if(notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }


        }

    }

}