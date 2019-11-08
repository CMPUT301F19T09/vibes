package com.cmput301f19t09.vibes.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * TODO determine how to handle optional attributes, implenet parcelable interface
 *
 * @see something?
 */
public class MoodEvent extends Event implements Serializable, Comparable {
    // date not optional
    // time not optional
    // description optional
    private EmotionalState state; // not optional
    private int socialSituation; // optional
    private Location location; // optional
    private User user; // the user that the mood is associated with

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

    // return value of -1 indicates that socialSituation was not specified
    public int getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(int social_situation) {
        this.socialSituation = social_situation;
    }

    // may return null if location was not specified
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

    // returns negative int if this MoodEvent is before m2; 0 if the same; 1 if the other way around
    @Override
    public int compareTo(Object o) {
        // if m2 has a more recent time then it will return 1
        //return -((MoodEvent) o).getLocalDateTime().compareTo(this.getLocalDateTime());
        return this.getLocalDateTime().compareTo(((MoodEvent)o).getLocalDateTime());
    }
}
