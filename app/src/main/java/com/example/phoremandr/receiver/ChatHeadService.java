package com.example.phoremandr.receiver;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phoremandr.R;

public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        return res;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.ic_launcher_background);
        chatHead.setClickable(true);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 400;

        windowManager.addView(chatHead, params);

        chatHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("chatHead","");
                /*startActivity(new Intent(ChatHeadService.this, SendNoti.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/
                stopSelf();
            }
        });

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            int flag=0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        if(flag==3){
                            flag=1;
                            return true;
                        }else{
                            flag=1;
                            return false;
                        }
                    case MotionEvent.ACTION_UP:
                        if(flag==3){
                            flag=2;
                            return true;
                        }else{
                            flag=2;
                            return false;
                        }
                    case MotionEvent.ACTION_MOVE:
                        flag=3;
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"You ckiced the imageview",Toast.LENGTH_LONG).show();
                        Log.i("tag","You clicked the imageview");
                /*
                Intent i = new Intent(view.getContext(),SendNoti.class);
                startActivity(i);
                stopSelf();*/
                        return true;
                }
            }
        });
        /*
        Snackbar.make(chatHead, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}