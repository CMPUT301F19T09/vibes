package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;

import com.cmput301f19t09.vibes.models.MoodItem;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/*
    Subclass of
 */
public class FollowedMoodListAdapter extends MoodListAdapter implements Observer
{
    public FollowedMoodListAdapter(Context context, User user)
    {
        super(context, user);
    }

    @Override
    protected void initializeData()
    {
        clear();
        data = new ArrayList<MoodItem>();
        for (String followed_username : user.getFollowingList())
        {
            User followed_user = new User(followed_username);

            user.exists(new User.UserExistListener()
            {
                @Override
                public void onUserExists()
                {
                    addUser(followed_user);
                }

                @Override
                public void onUserNotExists()
                {

                }
            });
        }
        addAll(data);
    }

    @Override
    public void refreshData()
    {
        initializeData();
    }

    private void addUser(User user)
    {
        data.add(new MoodItem(user, null));
        user.addObserver(this);
        /*

        user.readData(new User.FirebaseCallback()
        {
            @Override
            public void onCallback(User user)
            {
                setMoodEvent(user);
            }
        });

         */
    }

    private void setMoodEvent(User user)
    {
        for (MoodItem item : data)
        {
            if (item.user == user)
            {
                item.event = user.getMostRecentMoodEvent();
                notifyDataSetChanged();
            }
        }

        data.sort(MoodItem.date_comparator);
        //TODO MAKE A BETTER FIX THAN THIS
        //clear();
        //addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        User u = (User) o;
        for (MoodItem i : data)
        {
            if (i.user == o)
            {
                i.event = user.getMostRecentMoodEvent();
            }
        }
    }
}
