package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.util.Log;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

/*
Subclass of MoodListAdapter, this loads the each of the most recent MoodEvents of the User that the
current user follows
 */
public class FollowedMoodListAdapter extends MoodListAdapter implements Observer
{
    private List<String> followed;

    public FollowedMoodListAdapter(Context context)
    {
        super(context);
    }

    @Override
    public void update(Observable o, Object arg)
    {
    }

    @Override
    public void refreshData()
    {
        followed.clear();
        for (String s : user.getFollowingList())
        {
            followed.add(s);
            User u = new User(s);

            u.getMostRecentMoodEvent(filters, addEvent);
        }
    }

    @Override
    public void initializeData()
    {
        super.initializeData();
        followed = new ArrayList<>();
    }

    final Consumer<MoodEvent> addEvent = (MoodEvent event) ->
    {
        clear();
        Log.d("TEST/FollowedList", "Adding event");
        List<MoodEvent>new_data = new ArrayList<>();

        for (MoodEvent e : data)
        {
            if (event.compareTo(e) < 0)
            {
                new_data.add(event);
            }

            new_data.add(e);
        }

        if (data.size() == 0)
        {
            new_data.add(event);
        }

        data = new_data;
        addAll(data);
    };
}
