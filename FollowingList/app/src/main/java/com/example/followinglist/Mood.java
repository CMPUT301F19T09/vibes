package com.example.followinglist;

import android.media.Image;

public class Mood {
    private String name;
    private EmotionalState emotion;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public Mood(String name, String emotionName, int year, int month, int day, int hour, int minute) {
        this.name = name;
        this.emotion = new EmotionalState(emotionName);
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getName(){
        return this.name;
    }

    public int getImageFile(){
        return this.emotion.getImageFile();
    }

    public int getYear(){
        return this.year;
    }

    public int getMonth(){
        return this.month;
    }

    public int getDay(){
        return this.day;
    }

    public int getHour(){
        return this.hour;
    }

    public int getMinute(){
        return this.minute;
    }
}
