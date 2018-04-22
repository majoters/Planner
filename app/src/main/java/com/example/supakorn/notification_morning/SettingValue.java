package com.example.supakorn.notification_morning;

/**
 * Created by supakorn on 9/1/2561.
 */

public class SettingValue {

    private int ID;
    private int Hours;
    private int Minutes;

    public SettingValue(int ID, int hours, int minutes) {
        this.ID = ID;
        Hours = hours;
        Minutes = minutes;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(int hours) {
        Hours = hours;
    }

    public int getMinutes() {
        return Minutes;
    }

    public void setMinutes(int minutes) {
        Minutes = minutes;
    }
}
