package com.example.phoremandr.firebase_messaging_services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotification(
                        getApplicationContext(),
                        remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody(),
                        remoteMessage.getNotification().getChannelId());
            }

        }
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
 public void showNotification(Context context,String title, String message, String channel_Id) {

        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getActivity
                    (context, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity
                    (context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }

        AppValidator.logData("channelIdRetrieve","" + channel_Id);


        Uri sound = null;
        if (channel_Id != null) {
            switch (channel_Id) {
                case "alarmChannel":
                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.alarm);
                    break;

                case "emergencyAlarmChannel":
                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.emergency_alarm);
                    break;

                case "alarmToneChannel":
                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.alarm_tone);
                    break;

                case "alertAlarmChannel":
                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.alert_alarm);
                    break;
//                case "new_email_arrived_channel":
//                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.alarm);
//                    break;
            }
        } else {
            channel_Id = getString(R.string.default_notification_channel_id);
            sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.alarm);
        }


       // AppValidator.showToast(context, channel_Id);

        AppValidator.logData("channelId", "This is my Channel id " + channel_Id);

        AppValidator.logData("uri", "This is uri response " + sound);


        if ( sound!=null) {
            // Check if the Android Version is greater than Oreo
                NotificationCompat.Builder builder
                        = new NotificationCompat
                        .Builder(context, channel_Id)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                builder.setContent(getCustomDesign(context,title, message));
                NotificationManager notificationManager
                        = (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE);
                AppValidator.logData("check", "Hello" + sound);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel
                        = new NotificationChannel(
                        channel_Id,"web_app",
                        NotificationManager.IMPORTANCE_HIGH);

                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationChannel.setSound(sound, att);


                notificationManager.createNotificationChannel(notificationChannel);

            }
            notificationManager.notify(0, builder.build());


        }
//            AppValidator.logData("uriError", "The URI for the sound is null or invalid");



    }

    }