package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.util.Log;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/*
    Subclass of
 */
public class FollowedMoodListAdapter extends MoodListAdapter implements Observer
{
    interface FollowedUserListener extends Observer
    {
        @Override
        void update(Observable o, Object arg);
    }

    private List<String> observed_users;

    public FollowedMoodListAdapter(Context context, User user)
    {
        super(context, user);
    }

    @Override
    public void initializeData()
    {
        observed_users = new ArrayList<String>();
    }

    @Override
    public void refreshData()
    {
        if (observed_users == null)
        {

        }
        List<String> followed_users = user.getFollowingList();

        for (String followed_user : followed_users)
        {
            if (!observed_users.contains(followed_user))
            {
                User user = new User(followed_user);
                user.addObserver((Observable o, Object arg) ->
                {
                    refreshEvent((User) o);
                });
                user.readData();
            }
        }
    }

    public void refreshEvent(User user)
    {
        Log.d("TEST", "Refreshing event for " + user.getUserName());

        boolean replaced = false;
        for (MoodEvent event : data)
        {
            if (event.getUser().getUserName() == user.getUserName())
            {
                clear();

                data.remove(event);
                data.add(user.getMostRecentMoodEvent());
                Collections.sort(data);

                addAll(data);

                replaced = true;

                Log.d("TEST", "Replaced the event");

                break;
            }
        }

        if (! replaced)
        {
            clear();

            data.add(user.getMostRecentMoodEvent());
            Collections.sort(data);

            addAll(data);

            Log.d("TEST", "Event didn't exist, added new event");
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        User u = (User) o;

        if (u == user)
        {
            refreshData();
        }
        else
        {
            refreshEvent(u);
        }
    }
}
