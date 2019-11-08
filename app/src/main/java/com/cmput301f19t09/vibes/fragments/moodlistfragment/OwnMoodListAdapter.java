package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.util.Log;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

/*
Subclass of MoodListAdapter, this loads a user's own mood events
 */
public class OwnMoodListAdapter extends MoodListAdapter
{
    public OwnMoodListAdapter(Context context)
    {
        super(context);
    }

    /*
    Clear the data list, iterates through the User's mood events and adds them to the data list
     */
    @Override
    public void refreshData()
    {
        clear();
        data = new ArrayList<MoodEvent>();

        List<MoodEvent> events = user.getMoodEvents();
        if (events == null)
        {
            Log.d("TEST/OwnMoodListAdapter", "null MoodEvent list");
            return;
        }

        for (MoodEvent event : events)
        {
            data.add(event);
        }

        //Collections.sort(data);
        data.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((MoodEvent) o2).compareTo(o1);
            }
        });

        addAll(data);
        notifyDataSetChanged();
    }

    /*
    Calls refresh data
     */
    @Override
    public void initializeData()
    {
        refreshData();
    }

    /*
    When the User notifies it's observers this refreshesData incase any MoodEvents have changed
    @param user
        the user, should be current user
    @param arg
        optional argument, unused
     */
    @Override
    public void update(Observable user, Object arg)
    {
        refreshData();
    }
}
