package com.example.supakorn.checking;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.project.kmitl57.beelife.R;

/**
 * Created by supakorn on 12/1/2561.
 */

public class NotificationHelper extends ContextWrapper {
    public static final String channel1ID = "channelID1";
    public static final String channel1Name="Channel 1";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel1 = new NotificationChannel(channel1ID,channel1Name,NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        NotificationCompat.Builder nc = new NotificationCompat.Builder(getApplicationContext(),channel1ID);
       // PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //nc.setContentIntent(pendingIntent);

        //เอาไว้ถ้าอยากเขียนคำสั่งยกเลิก Alarm
        /*AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),AlarmNotification.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                getApplicationContext(),1,intent,0);

        alarmManager.cancel(pendingIntent1);
        */

        return nc
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_two)
                .setAutoCancel(true);
    }

}

