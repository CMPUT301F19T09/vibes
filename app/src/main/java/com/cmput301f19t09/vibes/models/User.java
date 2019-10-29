package com.cmput301f19t09.vibes.models;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public User(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String username) {
        this.username = username;
        /*
        Initialize with values from firebase
         */
    }

    // getFollowersLatest()

    // getFollowerLastest(String username)

    // addMood(MoodEvent moodEvent)

    // editMood(MoodEvent moodEvent, Integer index)

    // deleteMood(Integer index)
}
