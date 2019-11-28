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
    public void resume()
    {
        addObservers();
        refreshData();
    }

    public void pause()
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

    /**
     * Update the shown MoodEvent for each User that the primary User follows
     */
    @Override
    public void refreshData()
    {
        if (observed_users == null || observed_users.size() == 0)
        {
            clear();    // Make sure the list is clear
            return;
        }

        for (String user_id : observed_users)
        {
            User followed_user = UserManager.getUser(user_id);
            setUserEvent(followed_user);
        }
    }

    /**
     * Updates the MoodEvent shown in the list for this User. If the User already has an entry in the list
     * it is replaced with their most recent MoodEvent. If their most recent MoodEvent is null, then it
     * checks the list to remove any event associated with the User. MoodEvents are filtered by the set
     * filter
     * @param user The User whose event you are updating
     */
    public void setUserEvent(User user) {

        // Make sure the user is loaded
        if (user == null || !user.isLoaded())
        {
            return;
        }

        MoodEvent event = user.getMostRecentMoodEvent();
        boolean show_event = false;

        // If the event isn't null and matches the filter (or there is no filter), then it will be shown
        if (event != null &&
                (filter == null ||
                filter.equals("") ||
                event.getState().getEmotion().equals(filter)))
        {
            show_event = true;
        }

        int index;

        // Search the list of shown events for one matching the current user. If such an event is found, it
        // will be replaced
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
            // No existing event -> add the current event
            data.add(event);
        }
        else if (index >= 0 && show_event)
        {
            // Existing event -> replace the existing event with the current event
            data.set(index, event);
        }
        else if (index >= 0)
        {
            // Existing event and show_event is false -> remove the existing event
            data.remove(index);
        }

        // Sort the list in reverse-chronological order
        data.sort((MoodEvent a, MoodEvent b) -> { return b.compareTo(a); });

        clear();
        addAll(data);
    }
}
