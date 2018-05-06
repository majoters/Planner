package com.example.supakorn.checking;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.supakorn.notification_morning.NotificationHelper2;
import com.example.supakorn.notification_morning.NotificationMain;

import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;

/**
 * Created by supakorn on 11/1/2561.
 */

public class Checking extends BroadcastReceiver {
    public Context contextpub;
    public static boolean WorkDone=false;
    public static boolean TimeUp = false;
    private NotificationHelper2 mNotificationHelper;
    public final static double AVERAGE_RADIUS_OF_EARTH_M = 6371000;
    public static double latitude=0;
    public static double longitude=0;
    public static double CurrentLat=0;
    public static double CurrentLon=0;
    public static int ID_Request;
    public static int times;
    public boolean SetCancel=false;
    public static Activity activity;
    public List_Database list_database;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        SetCancel=false;
        contextpub=context;
        latitude=intent.getExtras().getDouble("Latitude");
        longitude=intent.getExtras().getDouble("Longitude");
        ID_Request=intent.getExtras().getInt("ID");
        times=intent.getExtras().getInt("T");
        Intent i = new Intent(context,GetUpdateCurrent.class);
        context.startService(i);
        mNotificationHelper=new NotificationHelper2(context);
        CurrentLat=GetUpdateCurrent.location.getLatitude();
        CurrentLon=GetUpdateCurrent.location.getLongitude();
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if(NotificationMain.CancelEmergency==ID_Request){
                    //context.unregisterReceiver(Checking.this);
                    SetCancel=true;
                    NotificationMain.CancelEmergency=-1;
                    this.cancel();
                }
                if(!SetCancel){
                    CurrentLat=GetUpdateCurrent.location.getLatitude();
                    CurrentLon=GetUpdateCurrent.location.getLongitude();
                    if(WorkDone) {
                        Toast.makeText(contextpub, "Work True", Toast.LENGTH_SHORT).show();
                        this.cancel();
                    }
                    else {
                        if(Interval(CurrentLat,CurrentLon)){
                            WorkDone=true;
                            Log.d("ID Checking", String.valueOf(ID_Request-1000));
                            MainActivity4.mDbAdabter_Model.UpdateArrive(ID_Request-1000,WorkDone);
                        }
                    }
                    //String text=String.valueOf(Get.get(0))+" "+String.valueOf(Get.get(1));
                    //Toast.makeText(contextpub,String.valueOf(WorkDone),Toast.LENGTH_SHORT).show();
                    Toast.makeText(contextpub,String.valueOf(millisUntilFinished/1000),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFinish() {
                TimeUp=true;
                if(WorkDone==false&&TimeUp==true&&SetCancel!=true){
                    //MainActivity4.mDbAdabter_Model.UpdateArrive(ID_Request-1000,WorkDone);
                    mNotificationHelper = new NotificationHelper2(contextpub);
                    notificate("คำเตือน","คุณกำลังจะพลาดรายการสิ่งที่ต้องทำของคุณ");
                }
            }
        }.start();

        /*if(!Interval(CurrentLat,CurrentLon)){
            notificate("You Miss Activity",String.valueOf(MainActivity4.mDbAdabter_Model.fetchByID(ID_Request-1000).getDescription()));
        }*/

    }

    public boolean Interval(double LatitudeCurrent,
                            double LongitudeCurrent){

        double distance = Math.sqrt((latitude-LatitudeCurrent)*(latitude-LatitudeCurrent)+
                (longitude-LongitudeCurrent)*(longitude-LongitudeCurrent));

        distance = 111.2*distance;

        /*Toast.makeText(contextpub,String.valueOf(LatitudeCurrent)+" "+String.valueOf(
                LongitudeCurrent),Toast.LENGTH_SHORT).show();*/

        if(distance<0.045){
            return true;
        }
        else {
            return false;
        }

    }

    private void notificate(String title,String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel2Notification(title,message);
        mNotificationHelper.getManager().notify(2,nb.build());
    }

}
