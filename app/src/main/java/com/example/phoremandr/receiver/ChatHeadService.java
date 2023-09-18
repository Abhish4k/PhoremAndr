package com.example.phoremandr.receiver;

import static android.os.SystemClock.elapsedRealtime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.telecom.Call;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.phoremandr.R;
import com.example.phoremandr.activities.DashboardActivity;
import com.example.phoremandr.utils.AppValidator;

public class ChatHeadService extends Service {

     CallReceiver callReceiver;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);

        callReceiver = new CallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_LOCKED_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);// Instantiate the BroadcastReceiver
        registerReceiver( callReceiver, filter);

        return START_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onDestroy() {
        unregisterReceiver(callReceiver);
        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, CallReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onTaskRemoved(rootIntent);
    }


}