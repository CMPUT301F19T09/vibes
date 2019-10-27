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
    static class UserPoint{
        String username;
        double lon;
        double lat;
        String timestamp;
        int moodId;
    }

    /**
     * Showing a user's mood
     * @param username
     * @return
     */
    public UserPoint showUser(String username){
        return getMockUser();
    }

    /**
     * Returns a mock user for dev purposes.
     * @return
     */
    public UserPoint getMockUser(){
        UserPoint mockUserPoint = new UserPoint();
        mockUserPoint.username = "testuser";
        mockUserPoint.lon = 113.4938;
        mockUserPoint.lat = 53.5461;
        mockUserPoint.moodId = 0;
        return mockUserPoint;
    }

    /**
     * Shows the mood of the user on the map fragment
     * @param User
     */
    public void showMoodOf(UserPoint User){

    }

    /**
     * Shows moods of multiple users.
     * @param mood
     */
    public void showMoods(ArrayList<UserPoint> mood){

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