package com.cmput301f19t09.vibes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MapFragment extends Fragment {

    /**
     * User structure required to display a user.
     */
    static class User{
        String username;
    }

    /**
     * Returns a mock user for dev purposes.
     * @return
     */
    public User getMockUser(){
        User mockUser = new User();
        mockUser.username = "testUser";
        return mockUser;
    }

    /**
     * Shows the mood of the user on the map fragment
     * @param User
     */
    public void showMoodOf(User User){

    }

    /**
     * Shows moods of multiple users.
     * @param mood
     */
    public void showMoods(ArrayList<User> mood){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showMoodOf(getMockUser());


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.map_fragment, container, false);

//        events = (ArrayList<HabitEvent>) getIntent().getSerializableExtra("event list");
//        if (events==null || events.size()<1){finish();}
    }
}