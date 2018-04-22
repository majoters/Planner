package com.example.hotmildc.shareact;

/**
 * Created by jameswich on 29/1/2561.
 */

public class RequestType {

    private String Name;
    private String Deta; //Detail
    private String Dat; //Data
    private String Tim; //Time
    private int ID;
    private String Firebasekey;
    private String LocationName;
    private double latitude;
    private double longitude;
    private int status;

    public RequestType(String description, String dat, int ID,int status) {
        Deta = description;
        this.Dat = dat;
        this.ID = ID;
        this.status=status;
    }

    public RequestType(){

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getFirebasekey() {
        return Firebasekey;
    }

    public void setFirebasekey(String firebasekey) {
        Firebasekey = firebasekey;
    }

    public String getDat() {
        return Dat;
    }

    public void setDat(String dat) {
        this.Dat = dat;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDeta() {
        return Deta;
    }

    public void setDeta(String detail) {
        Deta = detail;
    }

    public String getTim() {
        return Tim;
    }

    public void setTim(String time) {
        Tim = time;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
