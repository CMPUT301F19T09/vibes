package com.cmput301f19t09.vibes.fragments.followingfragment;

import com.cmput301f19t09.vibes.models.Mood;

import java.io.Serializable;
import java.util.ArrayList;

public class MoodData extends ArrayList<Mood> implements Serializable {
    @Override
    public boolean add(Mood mood){
        super.add(mood);
        return true;
    }
}
