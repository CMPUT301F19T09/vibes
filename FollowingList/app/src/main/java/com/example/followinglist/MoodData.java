package com.example.followinglist;

import java.util.ArrayList;

public class MoodData extends ArrayList<Mood> {
    @Override
    public boolean add(Mood mood){
        super.add(mood);
        return true;
    }
}
