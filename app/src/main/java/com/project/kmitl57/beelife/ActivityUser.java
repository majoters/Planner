package com.project.kmitl57.beelife;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ActivityUser {

    public String name;
    public String activity;

    // Default constructor required for calls to
    // DataSnapshot.getValue(ActivityUser.class)
    public ActivityUser() {
    }

    public ActivityUser(String name, String activity) {
        this.name = name;
        this.activity = activity;
    }
}