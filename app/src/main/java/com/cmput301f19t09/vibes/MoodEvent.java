package com.cmput301f19t09.vibes;

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
    // date not optional
    // time not optional
    // description optional
    private EmotionalState state; // not optional
    private int socialSituation; // optional
    private Location location; // optional
    private User user;

    public MoodEvent(LocalDate date, LocalTime time, String description,
                     EmotionalState state, int socialSituation, Location location, User user) {
        super(date, time, description);
        this.state = state;
        this.socialSituation = socialSituation;
        this.location = location;
        this.user = user;
    }

    public EmotionalState getState() {
        return state;
    }

    public void setState(EmotionalState state) {
        this.state = state;
    }

    public int getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(int social_situation) {
        this.socialSituation = social_situation;
    }

    public Location getLocation() {
        return location;
    }

    public String getLocationString() {
        return Location.convert(
                location.getLatitude(), Location.FORMAT_DEGREES)
                + " "
                + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES
        );
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
