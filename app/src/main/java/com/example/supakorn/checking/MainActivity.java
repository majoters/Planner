package com.example.supakorn.checking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double latitude;
    double longitude;
    public TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=(TextView)findViewById(R.id.textView);
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }

    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){

            //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null){
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                txt.setText(String.valueOf(latitude)+" "+String.valueOf(longitude));
            }
            else{
                latitude=0;
                longitude=0;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permission,grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

}
