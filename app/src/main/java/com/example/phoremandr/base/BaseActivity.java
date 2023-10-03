package com.example.phoremandr.base;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewbinding.ViewBinding;
import com.example.phoremandr.R;
import com.example.phoremandr.activities.DashboardActivity;
import com.example.phoremandr.api_services.APIClient;
import com.example.phoremandr.api_services.ApiInterface;
import com.example.phoremandr.databinding.ActivitySignupBinding;
import com.example.phoremandr.helper.SharedPrefHelper;
import com.example.phoremandr.receiver.CallReceiver;
import com.example.phoremandr.utils.AppValidator;

public abstract class BaseActivity extends AppCompatActivity {
    public  static ViewBinding viewBinding;
    public  static ApiInterface apiInterface;
    public static SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding =  getViewModel();
        apiInterface = APIClient.getClient();
        setStatusBarColor(R.color.green_500);
        SharedPrefHelper.getInstance(viewBinding.getRoot().getContext());
        sharedPrefHelper = new SharedPrefHelper();
        createChannel();
    }

    public abstract ViewBinding getViewModel();

    public abstract void setStatusBarColor (int color);

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, CallReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
    public void createChannel() {
        AppValidator.logData("channelId","here");
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.alarm);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(
            getString(R.string.default_notification_channel_id),"web_app",
            NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            /*notificationChannel.setSound(sound, att);*/

            notificationManager.createNotificationChannel(notificationChannel);

        }

    }





}
