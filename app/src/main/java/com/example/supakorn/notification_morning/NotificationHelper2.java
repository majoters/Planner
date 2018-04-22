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

import com.project.kmitl57.beelife.R;

/**
 * Created by supakorn on 18/1/2561.
 */

public class NotificationHelper2 extends ContextWrapper{
        public static final String channel2ID = "channelID2";
        public static final String channel2Name="Channel 2";

        private NotificationManager mManager;

        public NotificationHelper2(Context base) {
            super(base);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                createChannel();
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private void createChannel() {
            NotificationChannel channel2 = new NotificationChannel(channel2ID,channel2Name,NotificationManager.IMPORTANCE_DEFAULT);
            channel2.enableLights(true);
            channel2.enableVibration(true);
            channel2.setLightColor(R.color.colorPrimary);
            channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel2);
        }

        public NotificationManager getManager(){
            if(mManager == null){
                mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            }
            return mManager;
        }

        public NotificationCompat.Builder getChannel2Notification(String title, String message){
            NotificationCompat.Builder nc = new NotificationCompat.Builder(getApplicationContext(),channel2ID);
            Intent notifyIntent = new Intent(getApplicationContext(),NoticeMain.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            nc.setContentIntent(pendingIntent);

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

