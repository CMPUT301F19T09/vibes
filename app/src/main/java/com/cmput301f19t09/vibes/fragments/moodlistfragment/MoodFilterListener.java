package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import java.io.Serializable;

public interface MoodFilterListener extends Serializable
{
    void showOwnMoods();
    void showFollowedMoods();

    void addFilter(int filter);
    void removeFilter(int filter);
    void clearFilter();
}
