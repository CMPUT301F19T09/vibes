package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.MoodItem;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.List;

public class OwnMoodListAdapter extends MoodListAdapter
{
    public OwnMoodListAdapter(Context context, User user)
    {
        super(context, user);
    }

    @Override
    protected void initializeData()
    {
        data = new ArrayList<MoodItem>();

        List<MoodEvent> eventList = user.getMoodEvents();
        int index = 0;
        for (MoodEvent event : user.getMoodEvents())
        {
            //data.add(new MoodItem(user, event));
            addMoodItem(new MoodItem(user, event, index));
            index++;
        }

        data.sort(MoodItem.date_comparator);
        //TODO MAKE A BETTER FIX THAN THIS
        clear();
        addAll(data);
        notifyDataSetChanged();
    }
}
