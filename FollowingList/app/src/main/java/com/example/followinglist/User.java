package com.example.followinglist;

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
}