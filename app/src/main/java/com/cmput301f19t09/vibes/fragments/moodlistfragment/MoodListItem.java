package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

public class MoodListItem
{
    public User user;
    public MoodEvent event;

    public MoodListItem(User user, MoodEvent event)
    {
        this.user = user;
        this.event = event;
    }
}
