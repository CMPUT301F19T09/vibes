package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.MoodItem;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
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

        //List<MoodEvent> eventList = user.getMoodEvents();
        //int index = 0;
        for (MoodEvent event : user.getMoodEvents())
        {
            //data.add(new MoodItem(user, event, index));
            //addMoodItem(new MoodItem(user, event, index));
            data.add(event);
            //index++;
        }

        //data.sort(MoodItem.date_comparator);
        data.sort(COMPARE_BY_DATE);
        addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable user, Object arg)
    {
        refreshData();
    }
}
