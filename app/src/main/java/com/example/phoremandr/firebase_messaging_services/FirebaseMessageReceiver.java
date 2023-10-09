package com.example.phoremandr.firebase_messaging_services;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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


    @Override
    public void onNewToken(@NonNull String token){
        AppValidator.logData("messagingToken", "Refreshed token: " + token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
         AppValidator.logData("receiveNotification","" + remoteMessage.getNotification().getSound());
        if (remoteMessage.getNotification() != null) {
            showNotification(this, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getSound());


        }

    }

    public void handleIntent(Intent intent)

    {


        AppValidator.logData("FCM", "Handle Intent" );

        try
        {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("FirebaseMessageReceiver");
                AppValidator.logData("INtent", " Intent: "+builder );


                for (String key : intent.getExtras().keySet())
                {
                    builder.addData(key, intent.getExtras().get(key).toString());

                }
                AppValidator.logData("intent", " Intent: " +builder);

                onMessageReceived(builder.build());
                AppValidator.logData("intent", " OnMessage: " );


            }
            else
            {
                AppValidator.logData("intent", " Intent: " +intent);

                super.handleIntent(intent);

            }
        }
        catch (Exception e)
        {
            super.handleIntent(intent);
        }
    }

    @Override
    public  void onTaskRemoved(Intent rootIntent){
        Intent restartIntent= new Intent(getApplicationContext(), DashboardActivity.class);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent notifyIntent = PendingIntent.getActivity(getApplicationContext(), 0, restartIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


    }

    private RemoteViews getCustomDesign(Context context,String title, String message) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tvTitle, title);
        remoteViews.setTextViewText(R.id.tvMsg, message);
        return remoteViews;
    }
    // Method to display the notifications
    public void showNotification(Context context,String title, String message,String sound) {

        Intent intent  = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent notifyIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AppValidator.logData("Channel", "Sound: " +sound);

        String channel_id="";
        Uri soundUri;
        soundUri=Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);

        if(sound!=null){
            channel_id=sound;
            switch (channel_id){
                case "alarmChannel":
                    soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);

                case "emergencyAlarmChannel":
                    soundUri=  Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emergency_alarm);

                case "alarmToneChannel":
                    soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_tone);


                case "alertAlarmChannel":
                    soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm);

            }
            NotificationCompat.Builder builder
                    = new NotificationCompat
                    .Builder(context,
                    context.getString(R.string.channel_id))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(soundUri)
                    .setContentIntent(notifyIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                builder = builder.setContent(getCustomDesign(context,title, message));
            }
            else {
                builder = builder.setContentTitle(title)
                        .setContentText(message)
                        .setSound(soundUri)
                        .setSmallIcon(R.mipmap.ic_launcher);

            }

        }
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(context,
                channel_id)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(soundUri)
                .setContentIntent(notifyIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = builder.setContent(getCustomDesign(context,title, message));

            AppValidator.logData("Builder", "builder.addData: " +builder);

        }
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSound(soundUri)
                    .setSmallIcon(R.mipmap.ic_launcher);

        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(sound!=null){
            channel_id=sound;
            switch (sound){
                case "alarmChannel":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(channel_id, "phorem",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setShowBadge(true);
                            notificationChannel.setDescription("");
                            AudioAttributes att = new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .build();

                        notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm), att);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{400, 400});
                            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                        notificationManager.createNotificationChannel(
                                notificationChannel);
                    }
                    break;
                case "emergencyAlarmChannel":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel notificationChannel = new NotificationChannel("emergencyAlarmChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
                            notificationChannel.setShowBadge(true);
                            notificationChannel.setDescription("");
                            AudioAttributes att = new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .build();
                            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emergency_alarm), att);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{400, 400});
                            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                            notificationManager.createNotificationChannel(
                                    notificationChannel);
                        }
                        break;

                    case "alarmToneChannel":

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel notificationChannel = new NotificationChannel("alarmToneChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
                            notificationChannel.setShowBadge(true);
                            notificationChannel.setDescription("");
                            AudioAttributes att = new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .build();
                            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_tone), att);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{400, 400});
                            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                            notificationManager.createNotificationChannel(
                                    notificationChannel);
                        }
                        break;

                    case "alertAlarmChannel":

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel notificationChannel = new NotificationChannel("alertAlarmChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
                            notificationChannel.setShowBadge(true);
                            notificationChannel.setDescription("");
                            AudioAttributes att = new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .build();
                            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm), att);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{400, 400});
                            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                           notificationManager.createNotificationChannel(
                                notificationChannel);
                        }
                        break;
                }
            } else {
                channel_id = "alertAlarmChannel";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel("alertAlarmChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.setShowBadge(true);
                    notificationChannel.setDescription("");
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm), att);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{400, 400});
                    notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                    notificationManager.createNotificationChannel(
                            notificationChannel);
                }
            }


        notificationManager.notify(0, builder.build());
    }
}


