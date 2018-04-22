package com.example.supakorn.notification_morning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by supakorn on 9/1/2561.
 */

public class AlarmNotification extends BroadcastReceiver {
    private NotificationHelper mNotificationHelper;
    int ID;
    String Time;
    String Description;
    String LocationName;
    double latitude;
    double longitude;

    @Override
    public void onReceive(Context context, Intent intent) {
        //longitude=intent.getExtras().getDouble("Longitude");
        ID=intent.getIntExtra("ID",0);
        Time=intent.getStringExtra("Time");
        Description=intent.getStringExtra("Description");
        LocationName=intent.getStringExtra("LocationName");
        latitude=intent.getDoubleExtra("Latitude",0);
        longitude=intent.getDoubleExtra("Longitude",0);

        mNotificationHelper = new NotificationHelper(context);
        notificate(Description,Time+"\n"+Description+"\n"+LocationName);

        /*Intent check = new Intent("com.example.supakorn.checking");
        context.sendBroadcast(check);*/

        /*NotificationMain notificationMain = new NotificationMain(context);
        notificationMain.CancleAlarm();*/

    }

    private void notificate(String title,String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title,message);
        mNotificationHelper.getManager().notify(ID,nb.build());
    }
}
