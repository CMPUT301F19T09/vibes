package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;

/**
 * This subclass of MoodListAdapter is responsible for managing the dataset of MoodEvents containing
 * the (single) most recent MoodEvent of each of the Users that the primary (signed in) User follows
 */
public class FollowedMoodListAdapter extends MoodListAdapter {

    // A list of the UIDs associated with Users that this instance has created an adapter for
    private List<String> observed_users;
    private User mainUser;

    public FollowedMoodListAdapter(Context context)
    {
        super(context);
        observed_users = new ArrayList<>();
        this.mainUser = UserManager.getCurrentUser();
        this.resume();
    }

    /**
     * Add observers to all of mainUser's followed Users. This should be called when the parent Fragment resumes
     * so that Observers can be re-added
     */
    @Override
    public void resume()
    {
        addObservers();
        refreshData();
    }

    /**
     * Remove all observers on followed users. This should be called when the parent fragment pauses so that
     * the instance is not being updated in the background
     */
    public void pause()
    {
        clearObservers();
    }

    /**
     * For each User that the current User is following, add an Observer to refresh their most recent
     * MoodEvent. Also adds an Observer to User to observe when the following list changes
     */
    private void addObservers()
    {
        observed_users.clear();
        observed_users.addAll(mainUser.getFollowingList());

        for (String user_id : observed_users)
        {
            User followed_user = UserManager.getUser(user_id);

            // When the observer is notified, it will update this user's event
            followed_user.addObserver(new Observer()
            {
                @Override
                public void update(Observable observable, Object arg)
                {
                    setUserEvent((User)observable);
                }
            });
        }

        // The mainUser's observer clears and reinitialises the Observers in case its following list has changed
        mainUser.addObserver(new Observer()
        {
            public void update (Observable observable, Object arg)
            {
                clearObservers();
                addObservers();

                refreshData();
            }
        });
    }

    /**
     * For all observed Users and the main User, remove their Observers
     */
    private void clearObservers()
    {
        for (String user_id : observed_users)
        {
            UserManager.removeUserObservers(user_id);
        }

        mainUser.deleteObservers();
    }

    /**
     * Go through all observed_users and add their most recent MoodEvent to the List
     */
    @Override
    public void refreshData()
    {
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

        // Make sure the mainUser is loaded
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

        // Search the list of shown events for one matching the current mainUser. If such an event is found, it
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
