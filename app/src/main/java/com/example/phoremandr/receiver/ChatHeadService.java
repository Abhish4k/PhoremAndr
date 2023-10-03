package com.example.phoremandr.receiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.phoremandr.fragment.CreateMemoFragment;

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
        broadcastIntent.setClass(getApplicationContext(), CallReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onTaskRemoved(rootIntent);
    }


}