package com.cmput301f19t09.vibes.fragments.mapfragment;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * User structure required to display a user.
 */
public class UserPoint implements Serializable {
    private String username;
    private LatLng location;
    private int moodId;
    private String emotion;
    private String reason;

    /**
     * Constructor for creating a userpoint.
     * @param username
     * @param location
     * @param moodId
     * @param emotion
     * @param reason
     */
    public UserPoint(String username, LatLng location, int moodId, String emotion, String reason) {
        this.username = username;
        this.location = location;
        this.moodId = moodId;
        this.emotion = emotion;
        this.reason = reason;
    }

    /**
     * constructor for creating a user point with given parameters.
     * @param username
     * @param location
     * @param emotion
     * @param reason
     */
    public UserPoint(String username, LatLng location, String emotion, String reason) {
        this.username = username;
        this.location = location;
        this.emotion = emotion;
        this.reason = reason;
    }

    /**
     * Returns a mock user for dev purposes.
     * @return Returns the mock user
     */
    public static UserPoint getMockUser(){
        return new UserPoint("testuser",new LatLng(53.5461, 113.4938), 0, "HAPPY", "I am pregnant");
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

    /**
     * Returns the location
     * @return
     */
    public LatLng getLocation() {
        return location;
    }

    /**
     * Sets the location
     * @param location
     */
    public void setLocation(LatLng location) {
        this.location = location;
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


}