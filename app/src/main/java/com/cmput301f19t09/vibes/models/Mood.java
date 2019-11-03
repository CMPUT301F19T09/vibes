package com.cmput301f19t09.vibes.models;


import com.google.firebase.firestore.GeoPoint;

import java.sql.Timestamp;

public class Mood {
    private String name;
    private EmotionalState emotion;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    // I added the location and the reason - fatih
    private String reason;
    private GeoPoint location;

    public Mood(String name, String emotionName, int year, int month, int day, int hour, int minute) {
        this.name = name;
        this.emotion = new EmotionalState(emotionName);
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Mood(String name, String emotionName, int year, int month, int day, int hour, int minute, GeoPoint location) {
        this.name = name;
        this.emotion = new EmotionalState(emotionName);
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.location = location;
    }

    public String getReason() {
        if(this.reason == null){
            return "";
        }
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
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

    /**
     * Returns the string format of the mood
     * @return
     */
    @Override
    public String toString(){
        return this.name+":"+this.emotion+","+this.year+","+this.month+","+this.day;
    }

    /**
     * Returns the string of the emotion
     * @return
     */
    public String getStringEmotion(){
        return this.emotion.getEmotion();
    }
}
