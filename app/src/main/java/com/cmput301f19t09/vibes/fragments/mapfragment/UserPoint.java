package com.cmput301f19t09.vibes.fragments.mapfragment;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * User structure required to display a user.
 */
public class UserPoint implements Serializable {
    private String username;
    private double Lat;
    private double Long;
    private int moodId;
    private String emotion;
    private String reason;

    /**
     *
     * @param username
     * @param Lat
     * @param Long
     * @param moodId
     * @param emotion
     * @param reason
     */
    public UserPoint(String username, double Lat, double Long, int moodId, String emotion, String reason) {
        this.username = username;
        this.Lat = Lat;
        this.Long = Long;
        this.moodId = moodId;
        this.emotion = emotion;
        this.reason = reason;
    }

    /**
     * Returns a mock user for dev purposes.
     * @return Returns the mock user
     */
    public static UserPoint getMockUser(){
        return new UserPoint("testuser",53.5461, 113.4938, 0, "HAPPY", "I am pregnant");
    }

    /**
     * Returns username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public int getMoodId() {
        return moodId;
    }

    public void setMoodId(int moodId) {
        this.moodId = moodId;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }


}