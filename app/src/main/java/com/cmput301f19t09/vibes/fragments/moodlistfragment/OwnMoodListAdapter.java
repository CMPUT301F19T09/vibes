package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class OwnMoodListAdapter extends MoodListAdapter
{
    public OwnMoodListAdapter(Context context, User user)
    {
        super(context, user);
    }

    @Override
    public void refreshData()
    {
        clear();
        data = new ArrayList<MoodEvent>();

        List<MoodEvent> events = user.getMoodEvents();
        if (events == null)
        {
            return;
        }

        for (MoodEvent event : events)
        {
            data.add(event);
        }

        Collections.sort(data);
        addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable user, Object arg)
    {
        refreshData();
    }
}
