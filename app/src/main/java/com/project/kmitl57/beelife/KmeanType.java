package com.project.kmitl57.beelife;

/**
 * Created by supakorn on 21/1/2561.
 */

public class KmeanType {

    private double TimeAgent;
    private double Latitude;
    private double Longitude;
    private int Frequency;

    public KmeanType(double timeAgent,double latitude,double longitude, int frequency) {
        TimeAgent = timeAgent;
        Latitude = latitude;
        Longitude = longitude;
        Frequency = frequency;
    }

    public double getTimeAgent() {
        return TimeAgent;
    }

    public void setTimeAgent(double timeAgent) {
        TimeAgent = timeAgent;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public int getFrequency() {
        return Frequency;
    }

    public void setFrequency(int frequency) {
        Frequency = frequency;
    }
}
