package com.phorem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phorem.R;
import com.phorem.base.BaseActivity;
import com.phorem.utils.AppValidator;

import org.w3c.dom.Text;

public class Dialog extends AppCompatActivity {
    RelativeLayout alert_small;
    TextView alertMsg , alertName;
    ImageView cancel_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        alertMsg= findViewById(R.id.alertMsg);
        alert_small=findViewById(R.id.alertSmall);
        alertName=findViewById(R.id.alertName);
        cancel_icon=findViewById(R.id.cancel_icon);
        Intent intent = getIntent();
        if (intent!= null){
            String message = intent.getStringExtra("message");
            String title = intent.getStringExtra("name");
            alertMsg.setText(message);
            alertName.setText(title);
        }

        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              finishAndRemoveTask();
            }
        });

        alert_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.phorem");
              getApplicationContext().startActivity(i);
              finishAndRemoveTask();

            }
        });

    }
}