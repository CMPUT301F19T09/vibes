package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.MoodItem;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;

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

        for (MoodEvent event : user.getMoodEvents())
        {
            data.add(new MoodItem(user, event));
        }

        data.sort(MoodItem.date_comparator);
        notifyDataSetChanged();
    }
}
