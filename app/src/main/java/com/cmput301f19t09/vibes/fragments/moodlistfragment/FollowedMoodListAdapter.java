package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/*
Subclass of MoodListAdapter, this loads the each of the most recent MoodEvents of the User that the
current user follows
 */
public class FollowedMoodListAdapter extends MoodListAdapter {
    interface FollowedUserListener extends Observer
    {
        @Override
        void update(Observable o, Object arg);
    }

    // Maintain a list of the UIDs of users that this user observes
    private List<String> observed_users;
    private boolean filterChanged;

    public FollowedMoodListAdapter(Context context)
    {
        super(context);
        filterChanged = false;
    }

    /*
    initialize the observed users list and call refresh data
     */
    @Override
    public void onResume()
    {
        addObservers();
        refreshData();
    }

    public void onPause()
    {
        clearObservers();
    }

    private void addObservers()
    {
        observed_users = user.getFollowingList();

        for (String user_id : observed_users)
        {
            User followed_user = UserManager.getUser(user_id);

            followed_user.addObserver((Observable observable, Object arg) ->
            {
                setUserEvent((User)observable);
            });
        }

        user.addObserver((Observable observable, Object arg) ->
        {
            clearObservers();
            addObservers();

            refreshData();
        });
    }

    private void clearObservers()
    {
        for (String user_id : observed_users)
        {
            UserManager.removeUserObservers(user_id);
        }

        user.deleteObservers();
    }

    /*
    For each User that the current User is following, make sure that that user has an entry in the
    data set for their most recent MoodEvent
     */
    @Override
    public void refreshData()
    {
        if (observed_users == null || observed_users.size() == 0)
        {
            return;
        }

        for (String user_id : observed_users)
        {
            User followed_user = UserManager.getUser(user_id);
            setUserEvent(followed_user);
        }
    }

    /*
    Finds the MoodEvent with User user in the data. If that MoodEvent doesn't exist, add the User's
    most recent MoodEvent, otherwise update the displayed MoodEvent to be the User's most recent MoodEvnet
     */
    public void setUserEvent(User user) {

        if (user == null || !user.isLoaded())
        {
            return;
        }

        MoodEvent event = user.getMostRecentMoodEvent();
        boolean show_event = false;

        if (event != null &&
                (filter == null ||
                filter.equals("") ||
                event.getState().getEmotion().equals(filter)))
        {
            show_event = true;
        }

        int index;

        for (index = 0; index < data.size(); index++)
        {
            if (data.get(index).getUser().equals(user))
            {
                break;
            }
        }

        index = (index == data.size()) ? -1 : index;

        if (index == -1 && show_event)
        {
            data.add(event);
        }
        else if (index >= 0 && show_event)
        {
            data.set(index, event);
        }
        else
        {
            data.remove(index);
        }

        data.sort((MoodEvent a, MoodEvent b) -> { return b.compareTo(a); });
        clear();
        addAll(data);
    }
}
