package com.example.hotmildc.shareact;

/**
 * Created by hotmildc on 16/1/2561.
 */

public class Post {

    private int Id;
    private String Username;
    private String Detail ;
    private String Date ;
    private String Time;
    private PositionTarget Location;

    public Post() {
    }

    public Post(String detail, String dat, String time,PositionTarget Location) {
        Detail = detail;
        Date = dat;
        Time = time;
        this.Location=Location;
    }

    public Post(int id,String username,String detail, String dat, String time,PositionTarget Location) {
        Id = id;
        Username = username;
        Detail = detail;
        Date = dat;
        Time = time;
        this.Location=Location;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String dat) {
        Date = dat;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public PositionTarget getLocation() {
        return Location;
    }

    public void setLocation(PositionTarget location) {
        Location = location;
    }

}

