package com.phorem.receiver;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import com.phorem.R;
import java.util.Date;

public class PhoneCallReceiver  extends BroadcastReceiver
{
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
            {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            }
            else
            {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                int state = 0;
                if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE))
                {
                    state = TelephonyManager.CALL_STATE_IDLE;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
                {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }

                Log.e("phoneState",""+ number);

                if(number != null){
                    onCallStateChanged(context, state, number);
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start){}
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){}

    public void onCallStateChanged(Context context, int state, String number)
    {
        if(lastState == state)
        {
            //No change, debounce extras

            return;
        }
        switch (state)
        {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                showAlertDialog(context, number);
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (isIncoming)
                {
                    onIncomingCallEnded(context,number,callStartTime,new Date());
                }

            case TelephonyManager.CALL_STATE_IDLE:
                if(isIncoming)
                {
                    onIncomingCallEnded(context, number, callStartTime, new Date());
                }
        }
        lastState = state;
    }

    private void showAlertDialog(Context context, String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.inc_call);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Write memo for this incoming number" + number +"?");
        builder.setPositiveButton(R.string.yess, (dialog, which) -> {

            Intent i = context.getPackageManager().getLaunchIntentForPackage("com.phorem");
            i.setComponent(new ComponentName("com.phorem", "com.phorem.activities.CreateMemoActivity"));
            context.startActivity(i);

        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            // Code to decline the call by the alert dialog box will be here
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        alertDialog.getWindow().setType(LAYOUT_FLAG);
        alertDialog.show();
    }
}
