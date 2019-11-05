package com.cmput301f19t09.vibes.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * TODO determine how to handle optional attributes, implenet parcelable interface
 *
 * @see something?
 */
public class MoodEvent extends Event implements Serializable {
    private EmotionalState state;
    private int social_situation;
    private Location location;

    public MoodEvent(LocalDate date, LocalTime time, String description,
                     EmotionalState state, int social_situation, Location location) {
        super(date, time, description);
        this.state = state;
        this.social_situation = social_situation;
        this.location = location;
    }

    public EmotionalState getState() {
        return state;
    }

    public void setState(EmotionalState state) {
        this.state = state;
    }

    public int getSocialSituation() {
        return social_situation;
    }

    public void setSocialSituation(int social_situation) {
        this.social_situation = social_situation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
