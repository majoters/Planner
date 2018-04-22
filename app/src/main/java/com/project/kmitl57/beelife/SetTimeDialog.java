package com.project.kmitl57.beelife;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.supakorn.notification_morning.NotificationMain;

import devs.mulham.raee.sample.MainActivity4;

public class SetTimeDialog extends Dialog {
    Button select;
    Button cancel;
    int Hour;
    int Minute;
    public SetTimeDialog(@NonNull final Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_dialog);
        select = (Button) findViewById(R.id.select);
        cancel = (Button) findViewById(R.id.cancel);
        final TimePicker timeSet = (TimePicker)findViewById(R.id.timeset);
        int alltime=MainActivity4.mSetting_clone.getTimeSetting();
        Hour=alltime/100;
        Minute=alltime%100;
        timeSet.setIs24HourView(true);

        timeSet.setCurrentHour(Hour);
        timeSet.setCurrentMinute(Minute);



        //---------------------------------------------------------------

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NotificationMain notificationMain = new NotificationMain(getContext());
                        notificationMain.CancleAlarmRoutine();
                        Hour=timeSet.getCurrentHour();
                        Minute=timeSet.getCurrentMinute();

                        MainActivity4.mSetting_clone.updateSetting(Hour,Minute);
                        notificationMain.setRoutine();

                        cancel();
                    }
                }, 500);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                }, 500);

            }
        });
    }
}
