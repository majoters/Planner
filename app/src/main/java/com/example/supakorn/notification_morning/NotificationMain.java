package com.example.supakorn.notification_morning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.supakorn.checking.Checking;

import java.util.Calendar;
import java.util.Date;

import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by hotmildc on 17/1/2561.
 */

public class NotificationMain {

    Context context;
    public int ID_Request;
    public static int CancelEmergency;

    public NotificationMain(Context context) {
        this.context = context;
    }

    public void setRoutine(){

        Calendar now = Calendar.getInstance();
        Date date = new Date();
        int gettime=MainActivity4.mSetting_clone.getTimeSetting();
        int Hour=gettime/100;
        int Minute=gettime%100;
        AlarmManager routine = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent al_every = new Intent(context,AlarmRoutine.class);

        now.set(Calendar.DATE,date.getDate());
        now.set(Calendar.MONTH,date.getMonth());
        now.set(Calendar.YEAR,date.getYear()%100+2000);
        now.set(Calendar.HOUR_OF_DAY,Hour);
        now.set(Calendar.MINUTE,Minute);
        now.set(Calendar.SECOND,0);

        Log.w("On Routine",String.valueOf(now.getTime().toString()));

        PendingIntent pop = PendingIntent.getBroadcast(context,9000,al_every
                ,PendingIntent.FLAG_UPDATE_CURRENT);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 9000,
                al_every,PendingIntent.FLAG_NO_CREATE) == null);

        if(alarmUp && date.getHours()<Hour)
            routine.set(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),pop);
        else if(date.getHours()==Hour){
            if(date.getMinutes()<Minute)
                routine.set(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),pop);
        }

    }

    public void setAlarm(List_Database listDatabase){
        Calendar now = Calendar.getInstance();
        this.ID_Request=MainActivity4.mDbAdabter_Model.ListToID(listDatabase);

        AlarmManager mgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarm = new Intent(context,AlarmNotification.class);
        //alarm.putExtra("Latitude",latitude);
        alarm.putExtra("ID",ID_Request);
        alarm.putExtra("Time", MainActivity4.DataToTime(listDatabase.getTime()));
        alarm.putExtra("Description",listDatabase.getDescription());
        alarm.putExtra("LocationName",listDatabase.getLocationName());
        alarm.putExtra("Latitude",listDatabase.getLatitude());
        alarm.putExtra("Longitude",listDatabase.getLongitude());

        PendingIntent popup = PendingIntent.getBroadcast(
                context,ID_Request,alarm,PendingIntent.FLAG_UPDATE_CURRENT);


        now.set(Calendar.DATE,listDatabase.getDate()/10000);
        now.set(Calendar.MONTH,(listDatabase.getDate()/100)%100-1);
        now.set(Calendar.YEAR,listDatabase.getDate()%100+2000);
        now.set(Calendar.HOUR_OF_DAY,listDatabase.getTime()/100);
        now.set(Calendar.MINUTE,listDatabase.getTime()%100);
        now.set(Calendar.SECOND,0);

        mgr.set(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),popup);

        Toast.makeText(context,"Successful Press",Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent checking = new Intent(context, Checking.class);
        checking.putExtra("ID",ID_Request+1000);
        checking.putExtra("Latitude",listDatabase.getLatitude());
        checking.putExtra("Longitude",listDatabase.getLongitude());
        //
        Log.d("id noti", String.valueOf(ID_Request));
        PendingIntent popup2 = PendingIntent.getBroadcast(
                context,ID_Request+1000,checking,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),popup2);
    }

    public Context getcontext() {
        return context;
    }

    public void setcontext(Context context) {
        this.context = context;
    }

    public int getID_Request() {
        return ID_Request;
    }

    public void setID_Request(int ID_Request) {
        this.ID_Request = ID_Request;
    }

    public void CancleAlarm(){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmNotification.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                context,ID_Request,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent1);
        pendingIntent1.cancel();
    }

    public void CancleAlarmChecking(){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,Checking.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                context,ID_Request+1000,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent1);
        pendingIntent1.cancel();
    }

    public void CancleAlarmRoutine(){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmRoutine.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                context,9000,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent1);
        pendingIntent1.cancel();
    }

}
