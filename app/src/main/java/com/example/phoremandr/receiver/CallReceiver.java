package com.example.phoremandr.receiver;

import android.content.Context;
import android.content.Intent;
import java.util.Date;

public class CallReceiver extends PhoneCallReceiver {
    Context context;

    @Override
    protected void onIncomingCallStarted(final Context ctx, String number, Date start)
    {
        context =   ctx;

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        final Intent intent = new Intent(ctx, ChatHeadService.class);
        ctx.stopService(intent);
    }


}