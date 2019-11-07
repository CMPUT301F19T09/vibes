package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.MoodItem;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/*
    Subclass of
 */
public class FollowedMoodListAdapter extends MoodListAdapter implements Observer
{
    private List<User> followed_users;

    public FollowedMoodListAdapter(Context context, User user)
    {
        super(context, user);
        followed_users = new ArrayList<User>();
    }

    @Override
    public void refreshData()
    {
        clear();

        data = new ArrayList<MoodEvent>();
        for (User followed_user : followed_users)
        {
            data.add(followed_user.getMostRecentMoodEvent());
        }
        data.sort(COMPARE_BY_DATE);

        addAll(data);
    }

    private void populateList()
    {
        clear();

        for (String username : user.getFollowingList())
        {
            User followed_user = new User(username);
            followed_users.add(followed_user);
            followed_user.addObserver(this);
        }

        for (User followed_user : followed_users)
        {
            followed_user.readData();
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        User u = (User) o;

        if (user.equals(u) || u.getFollowingList().size() != followed_users.size())
        {
            populateList();
        }
        else
        {
            refreshData();
        }
    }
}
