package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import java.io.Serializable;

/**
 * Interface for an Object that contains MoodEvents and can be filtered
 */
public interface MoodFilterListener extends Serializable {
    void showOwnMoods();

    void showFollowedMoods();

    void setFilter(String filter);

}
