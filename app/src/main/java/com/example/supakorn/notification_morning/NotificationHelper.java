package com.example.supakorn.notification_morning;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.supakorn.new_beelife_login.Login;
import com.project.kmitl57.beelife.R;

import devs.mulham.raee.sample.MainActivity4;

/**
 * Created by supakorn on 8/1/2561.
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

    public NotificationCompat.Builder getChannel1Notification(String title,String message){
        NotificationCompat.Builder nc = new NotificationCompat.Builder(getApplicationContext(),channel1ID);

        Intent notifyIntent = new Intent(getApplicationContext(),Login.class);
        MainActivity4.routine=true;
        notifyIntent.putExtra("routine",true);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);

        return nc
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_two)
                .setAutoCancel(true);
    }

}
