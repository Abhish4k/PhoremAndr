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
import android.service.notification.StatusBarNotification;
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

import java.io.Serializable;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    // Override onNewToken to get new token

    public static SharedPrefHelper sharedPrefHelper;

    @Override
    public void onNewToken(@NonNull String token)
    {
        AppValidator.logData("messagingToken", "Refreshed token: " + token);

    }



//    @Override
//    public void onMessageReceived()
//    {


//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel("alarmChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setShowBadge(true);
//            notificationChannel.setDescription("");
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm), att);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{400, 400});
//            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel("alarmToneChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setShowBadge(true);
//            notificationChannel.setDescription("");
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm_tone), att);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{400, 400});
//            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel("alertAlarmChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setShowBadge(true);
//            notificationChannel.setDescription("");
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert_alarm), att);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{400, 400});
//            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel("emergencyAlarmChannel", "phorem", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setShowBadge(true);
//            notificationChannel.setDescription("");
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.emergency_alarm), att);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{400, 400});
//            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);
//        }
//    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
      /*  AppValidator.logData("receiveNotification","" + remoteMessage.getNotification().getSound());

        showNotification(
                getApplicationContext(),
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                remoteMessage.getNotification().getSound());*/
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
        int notificationID = 0;
     int id = (int) System.currentTimeMillis();
     NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (sound != null) {
            channel_id = sound;
            switch (sound) {
                case "alarmChannel":
                    notificationID = 1;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        NotificationChannel notificationChannel = new NotificationChannel("alarmChannel", "phorem", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationChannel.setShowBadge(true);
                        notificationChannel.setDescription("");
                        AudioAttributes att = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build();
                        notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm), att);
                        soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);
                        notificationChannel.enableVibration(true);
                        notificationChannel.setVibrationPattern(new long[]{400, 400});
                        notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                        manager.createNotificationChannel(notificationChannel);
                    }break;

                case "emergencyAlarmChannel":
                    notificationID = 2;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel("emergencyAlarmChannel", "phorem", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationChannel.setShowBadge(true);
                        notificationChannel.setDescription("");
                        AudioAttributes att = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build();
                        notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emergency_alarm), att);
                        soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emergency_alarm);
                        notificationChannel.enableVibration(true);
                        notificationChannel.setVibrationPattern(new long[]{400, 400});
                        notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                        manager.createNotificationChannel(notificationChannel);
                    }
                   break;

                case "alarmToneChannel":
                    notificationID = 3;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        NotificationChannel notificationChannel = new NotificationChannel("alarmToneChannel", "phorem", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationChannel.setShowBadge(true);
                        notificationChannel.setDescription("");
                        AudioAttributes att = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build();
                        notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_tone), att);
                        soundUri = Uri.parse("android.resource://" +context.getPackageName() + "/" + R.raw.alarm_tone);
                        notificationChannel.enableVibration(true);
                        notificationChannel.setVibrationPattern(new long[]{400, 400});
                        notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                        manager.createNotificationChannel(notificationChannel);
                    }  break;

                case "alertAlarmChannel":
                    notificationID = 4;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel("alertAlarmChannel", "phorem", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationChannel.setShowBadge(true);
                        notificationChannel.setDescription("");
                        AudioAttributes att = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build();
                        notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm), att);
                        soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm);
                        notificationChannel.enableVibration(true);
                        notificationChannel.setVibrationPattern(new long[]{400, 400});
                        notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                         manager.createNotificationChannel(notificationChannel);
                    } break;
//                case "alert_alarm_channel":
//                    channel_id = context.getString(R.string.default_notification_channel_id);
//                    soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.alarm);
//                    break;
            }
        }
        else {
            channel_id = "alertAlarmChannel";
            notificationID = 4;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("alertAlarmChannel", "phorem", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setShowBadge(true);
                notificationChannel.setDescription("");
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();
                notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm), att);
                soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{400, 400});
                notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                manager.createNotificationChannel(notificationChannel);
            }}




        AppValidator.logData("uri", "This is uri response " +channel_id + "," + soundUri);

     NotificationCompat.Builder builder
                        = new NotificationCompat
                        .Builder(context,channel_id)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);

                builder.setContent(getCustomDesign(context,title, message));
     manager.notify(0, builder.build());
//        if ( soundUri!=null) {







            // Check if the Android Version is greater than Oreo
//            NotificationManager notificationManager
//                    = (NotificationManager) context.getSystemService(
//                    Context.NOTIFICATION_SERVICE);
//            createAlertAlarmChannel( notificationManager, context);
//            createEmergencyChannel( notificationManager, context);
//            createAlarmChannel( notificationManager, context);
//            createAlarmToneChannel( notificationManager, context);
//                NotificationCompat.Builder builder
//                        = new NotificationCompat
//                        .Builder(context,channel_id)
//                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                        .setSound(soundUri)
//                        .setContentIntent(pendingIntent);
//
//                builder.setContent(getCustomDesign(context,title, message));
//            AppValidator.logData("notificationChannel", " "  + soundUri);
//
//
//
//
//


//        }

    }


//    public void createAlarmChannel(NotificationManager notificationManager, Context context) {
//
//
//        sharedPrefHelper = new SharedPrefHelper();
//
//        NotificationChannel notificationChannel = null;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notificationChannel = new NotificationChannel(
//                  context.getString(R.string.default_notification_channel_id),"alarm_channel" ,
//                    NotificationManager.IMPORTANCE_HIGH);
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
//            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm),att);
//
//
//            AppValidator.logData("channelIdSound", "" + notificationChannel.getSound() + ", " + notificationManager);
//
//            if(notificationManager != null){
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//
//        }
//
//    }
//    public void createAlarmToneChannel(NotificationManager notificationManager, Context context) {
//        sharedPrefHelper = new SharedPrefHelper();
//
//        NotificationChannel notificationChannel = null;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notificationChannel = new NotificationChannel(
//                  context.getString(R.string.alarm_tone),"alarm_tone_channel" ,
//                    NotificationManager.IMPORTANCE_HIGH);
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
//            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_tone),att);
//
//
//            AppValidator.logData("channelIdSound", "" + notificationChannel.getSound() + ", " + notificationManager);
//
//            if(notificationManager != null){
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//
//        }
//
//    }
//    public void createAlertAlarmChannel(NotificationManager notificationManager, Context context) {
//
//
//        sharedPrefHelper = new SharedPrefHelper();
//
//        NotificationChannel notificationChannel = null;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notificationChannel = new NotificationChannel(
//                  context.getString(R.string.alert_alarm_channel_id),"alert_alarm_channel" ,
//                    NotificationManager.IMPORTANCE_HIGH);
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
//            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert_alarm),att);
//
//
//            AppValidator.logData("channelIdSound", "" + notificationChannel.getSound() + ", " + notificationManager);
//
//            if(notificationManager != null){
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//
//        }
//
//    }
//    public void createEmergencyChannel(NotificationManager notificationManager, Context context) {
//
//
//        sharedPrefHelper = new SharedPrefHelper();
//
//        NotificationChannel notificationChannel = null;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notificationChannel = new NotificationChannel(
//                  context.getString(R.string.emergency_alarm_channel_id),"emergency_alarm_channel" ,
//                    NotificationManager.IMPORTANCE_HIGH);
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
//            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emergency_alarm),att);
//
//
//            AppValidator.logData("channelIdSound", "" + notificationChannel.getSound() + ", " + notificationManager);
//
//            if(notificationManager != null){
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//
//        }
//
//    }


    private static int notificationCount=0;
    @Override
    public void handleIntent(Intent intent) {
        AppValidator.logData( "FCM", "handleIntent");

        //if you don't know the format of your FCM message,
        //just print it out, and you'll know how to parse it
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                AppValidator.logData("FCM", "Key: " + key + " Value: " + value);
            }
        }

        removeFirebaseOrigianlNotificaitons();

        if (bundle ==null)
        {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                AppValidator.logData("FCM", "Key: " + key + " Value: " + value);
            }

            return;}

        //pares the message
        CloudMsg cloudMsg = parseCloudMsg(bundle);
        AppValidator.logData("FCM", "Key: " + cloudMsg.msg + " Value: " + cloudMsg.getTestValue());

        //if you want take the data to Activity, set it
       /*Bundle myBundle = new Bundle();
        myBundle.putSerializable("type", (Serializable) cloudMsg);*/
        Intent myIntent = new Intent(this, DashboardActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        myIntent.putExtras(myBundle);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }

        AppValidator.logData("soundGetFromNoti","" + cloudMsg.getTestValue());
        //set the Notification
        Uri soundUri = null;
        if(cloudMsg != null){
            if(cloudMsg.getTestValue() != null){
                if(cloudMsg.getTestValue().equals("alarmToneChannel")){
                    soundUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.alarm_tone);
                }
            }

        }

        AppValidator.logData("soundUri","" + soundUri);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, this.getString(R.string.channel_id))
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(cloudMsg.getTitle())
                .setContentText(cloudMsg.getMsg())
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationChannel notificationChannel = new NotificationChannel(
                  this.getString(R.string.channel_id),"emergency_alarm_channel" ,
                    NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            notificationChannel.setSound(soundUri,att);

            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notificationCount++, notificationBuilder.build());

        super.handleIntent(intent);
    }

    private void removeFirebaseOrigianlNotificaitons() {

        //check notificationManager is available
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel("alarmChannel");
        }
        /*if (notificationManager == null )
            return;

        //check api level for getActiveNotifications()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //if your Build version is less than android 6.0
            //we can remove all notifications instead.
            //notificationManager.cancelAll();
            return;
        }

        //check there are notifications
        StatusBarNotification[] activeNotifications =
                notificationManager.getActiveNotifications();
        if (activeNotifications == null)
            return;

        //remove all notification created by library(super.handleIntent(intent))
        for (StatusBarNotification tmp : activeNotifications) {
            AppValidator.logData("FCM StatusBarNotification",
                    "tag/id: " + tmp.getTag() + " / " + tmp.getId());
            String tag = tmp.getTag();
            int id = tmp.getId();

            //trace the library source code, follow the rule to remove it.
            if (tag != null && tag.contains("FCM-Notification"))
                notificationManager.cancel(tag, id);
        }*/
    }

    private CloudMsg parseCloudMsg(Bundle bundle) {
        String title = null, msg=null;

        //if the message is sent from Firebase platform, the key will be that
        msg = (String) bundle.get("gcm.notification.body");

        if(bundle.containsKey("gcm.notification.title"))
            title = (String) bundle.get("gcm.notification.title");

        String testValue=null;
        if(bundle.containsKey("gcm.notification.sound2"))
            testValue = (String) bundle.get("gcm.notification.sound2");
        //parse your custom message


        //package them into a object(CloudMsg is your own structure), it is easy to send to Activity.
        CloudMsg cloudMsg = new CloudMsg(title, msg, testValue);
        return cloudMsg;
    }
}