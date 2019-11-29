package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import android.content.Context;

import java.util.Observable;

/**
 * This class manages a list of MoodEvents corresponding to the primary User
 */
public class OwnMoodListAdapter extends MoodListAdapter
{
    // The primary User
    User user;

    /**
     * Construct a new OwnMoodListAdapter
     * @param context The application context
     */
    public OwnMoodListAdapter(Context context)
    {
        super(context);

        // Get the primary User from the UserManager
        this.user = UserManager.getCurrentUser();
        this.resume();
    }

    /**
     * This clears the data set and populates it with the User's list of MoodEvents, sorted in
     * reverse-chronological order
     */
    @Override
    public void refreshData()
    {
        data.clear();

        for (MoodEvent event : user.getMoodEvents())
        {
            // If there is no filter or event matches the filter, add it to data
            if (filter == null || filter.equals("") || event.getState().getEmotion().equals(filter))
            {
                data.add(event);
            }
        }

        // Sort data in reverse chronological order
        data.sort((MoodEvent a, MoodEvent b) -> { return b.compareTo(a); });

        clear();
        addAll(data);
    }

    /**
     * This refreshes the data list and adds an observer to the User to call refreshData in the case of
     * the User being updated. This should be called if the Fragment or Activity containing the adapter
     * is resumed to re-add the observer
     */
    @Override
    public void resume()
    {
        user.addObserver((Observable o, Object arg) -> refreshData());
        refreshData();
    }

    /**
     * This removes the observer on User. This should be called if the Fragment or Activity containing the
     * adapter is paused, so refreshData() is not invoked in the background.
     */
    @Override
    public void pause()
    {
        user.deleteObservers();
    }
}
