package com.cmput301f19t09.vibes;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * TODO determine how to handle optional attributes, implenet parcelable interface
 *
 * @see something?
 */
public class MoodEvent extends Event implements Parcelable {
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

    protected MoodEvent(Parcel in) {
        super(in.readSerializable(), in.readSerializable(), in.readString()); // date, time, description
        social_situation = in.readInt();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<MoodEvent> CREATOR = new Creator<MoodEvent>() {
        @Override
        public MoodEvent createFromParcel(Parcel in) {
            return new MoodEvent(in);
        }

        @Override
        public MoodEvent[] newArray(int size) {
            return new MoodEvent[size];
        }
    };

    public EmotionalState getState() {
        return state;
    }

    public void setState(EmotionalState state) {
        this.state = state;
    }

    public int getSocial_situation() {
        return social_situation;
    }

    public void setSocial_situation(int social_situation) {
        this.social_situation = social_situation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeSerializable(date);
        pc.writeSerializable(time);
        pc.writeString(description);
        pc.writeParcelable(state, flags); // need EmotionalState to implement Parcelable
        pc.writeInt(social_situation);
        pc.writeParcelable(location, flags);
    }
}
