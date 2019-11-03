package com.cmput301f19t09.vibes;

import android.util.Log;

import com.cmput301f19t09.vibes.models.User;

import org.junit.Test;


public class UserTests {
    @Test
    public void testUser(){
        User user = new User("testuser");
        user.readData(new User.FirebaseCallback(){
            @Override
            public void onCallback(User user) {
                Log.d("debugging", user.getFirstName());

            }
        });
    }
}
