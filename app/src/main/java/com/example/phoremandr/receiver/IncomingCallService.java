package com.example.phoremandr.receiver;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.phoremandr.MainActivity;
import com.example.phoremandr.R;

import java.security.Provider;

public class IncomingCallService extends Service {
    private IncomingCallReceiver callReceiver;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        IncomingCallService getService() {
            return IncomingCallService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onCreate() {
        super.onCreate();
        callReceiver = new IncomingCallReceiver(); // Instantiate the BroadcastReceiver
        registerReceiver(callReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));

    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callReceiver); // Unregister the BroadcastReceiver

    }

    private class IncomingCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive","" + intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showAlertDialog(context);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Incoming Call");
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("You have an Incoming call! Pick Up.");
        builder.setPositiveButton("Answer", (dialog, which) -> {

            Intent openAppIntent = new Intent(context, MainActivity.class);
            openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openAppIntent);

        });
        builder.setNegativeButton("Decline", (dialog, which) -> {
            // Code to decline the call by the alert dialog box will be here
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();
    }


}