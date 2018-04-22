package com.example.supakorn.checking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.project.kmitl57.beelife.R;

import java.util.Calendar;

public class PlacePickerActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    public TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        txt = (TextView) findViewById(R.id.textView);
        Button btn = (Button) findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(PlacePickerActivity.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setAlarm(double latitude,double longitude) {
        AlarmManager alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
        Intent alarm = new Intent(this,Checking.class);
        alarm.putExtra("Latitude",latitude);
        alarm.putExtra("Longitude",longitude);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,0,alarm,0);

        Calendar settime = Calendar.getInstance();
        //settime.set(Calendar.HOUR_OF_DAY,15);
//        settime.set(Calendar.MINUTE,31);
  //      settime.set(Calendar.SECOND,30);

        settime.set(Calendar.HOUR_OF_DAY,settime.getTime().getHours());
        settime.set(Calendar.MINUTE,settime.getTime().getMinutes());
        settime.set(Calendar.SECOND,settime.getTime().getSeconds()+5);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,settime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);

        String txt =String.valueOf(settime.getTime());
        Toast.makeText(getApplicationContext(),String.valueOf(latitude)+" "+
                String.valueOf(longitude),Toast.LENGTH_SHORT).show();
        //this.finish();
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        if (requestCode==PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){

                Place place = PlacePicker.getPlace(data,this);
                setAlarm(place.getLatLng().latitude,place.getLatLng().longitude);
                txt.setText(place.getAddress()+" "+place.getLatLng().latitude+" "+
                place.getLatLng().longitude);
            }
        }

    }

}
