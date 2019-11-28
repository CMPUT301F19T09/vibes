package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/*
Subclass of MoodListAdapter, this loads a user's own mood events
 */
public class OwnMoodListAdapter extends MoodListAdapter
{
    private Observer userObserver = (Observable user, Object arg) ->
    {
        refreshData();
    };

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
        data.clear();

        for (MoodEvent event : user.getMoodEvents())
        {
            if (filter == null || event.getState().getEmotion().equals(filter))
            {
                data.add(event);
            }
        }

        data.sort((MoodEvent a, MoodEvent b) -> { return b.compareTo(a); });

        clear();
        addAll(data);
    }

    /*
    Calls refresh data
     */
    @Override
    public void resume()
    {
        user.addObserver(userObserver);
        refreshData();
    }

    @Override
    public void pause()
    {
        user.deleteObserver(userObserver);
    }
}
