package com.project.kmitl57.beelife;

/**
 * Created by supakorn on 22/1/2561.
 */

public class DataAnalysis {
    public int Time;
    public int TimeAct;
    public String Description;
    public String LocationName;
    public double latitude;
    public double longitude;
    public int frequency;
    public int group;
    public String DayOfWeek;
    public int important;
    public int arrive;

    public DataAnalysis(int time, int timeAct, String description, String locationName, double latitude, double longitude, int frequency, int group) {
        this.Time = time;
        this.TimeAct = timeAct;
        this.Description = description;
        this.LocationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.frequency = frequency;
        this.group = group;
    }
    public DataAnalysis(String DayOfWeek, int timeAct, String description, String locationName, double latitude, double longitude, int frequency) {
        this.DayOfWeek = DayOfWeek;
        this.TimeAct = timeAct;
        this.Description = description;
        this.LocationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.frequency = frequency;
    }
    public DataAnalysis(int timeAct, String description, String locationName, double latitude, double longitude,int important,int arrive, int frequency) {
        this.TimeAct = timeAct;
        this.Description = description;
        this.LocationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.important = important;
        this.arrive = arrive;
        this.frequency = frequency;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public int getTimeAct() {
        return TimeAct;
    }

    public void setTimeAct(int timeAct) {
        TimeAct = timeAct;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String DayOfWeek) {
        this.DayOfWeek = DayOfWeek;
    }

    public int getImportant() {
        return important;
    }

    public int getArrive() {
        return arrive;
    }
}
