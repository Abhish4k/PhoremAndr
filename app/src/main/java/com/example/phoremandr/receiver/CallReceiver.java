package com.example.phoremandr.receiver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.example.phoremandr.R;

import java.util.Date;

public class CallReceiver extends PhoneCallReceiver {
    Context context;

    @Override
    protected void onIncomingCallStarted(final Context ctx, String number, Date start)
    {
        Toast.makeText(ctx,"New Incoming Call"+ number,Toast.LENGTH_LONG).show();
        context =   ctx;

        if(!number.isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            }
        }

        Log.d("phoneNumber","" + number);

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        final Intent intent = new Intent(ctx, ChatHeadService.class);
        ctx.stopService(intent);
        Toast.makeText(ctx,"Bye Bye"+ number,Toast.LENGTH_LONG).show();
    }





}