package com.example.supakorn.notification_morning;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.supakorn.new_beelife_login.Login;

import java.util.Date;

import devs.mulham.raee.sample.MainActivity4;

/**
 * Created by supakorn on 20/1/2561.
 */

public class AlarmRoutine extends BroadcastReceiver {
    private NotificationHelper mNotificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        //longitude=intent.getExtras().getDouble("Longitude");
        Date date = new Date();
        mNotificationHelper = new NotificationHelper(context);
        notificate("Activity Routines",String.valueOf(date.toString()));

        /*context.startService(new Intent(context,Login.class));
        MainActivity4.routine=true;

        /*NoticeMain noticeMain = new NoticeMain(context);
        noticeMain.show();*/

        /*Intent check = new Intent("com.example.supakorn.checking");
        context.sendBroadcast(check);*/

    }

    private void notificate(String title,String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title,message);
        mNotificationHelper.getManager().notify(0,nb.build());
    }
}
