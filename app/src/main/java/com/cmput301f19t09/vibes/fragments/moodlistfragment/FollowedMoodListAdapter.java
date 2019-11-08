package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.util.Log;

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
public class FollowedMoodListAdapter extends MoodListAdapter implements Observer
{
    interface FollowedUserListener extends Observer
    {
        @Override
        void update(Observable o, Object arg);
    }

    // Maintain a list of the UIDs of users that this user observes
    private List<String> observed_users;

    public FollowedMoodListAdapter(Context context, User user)
    {
        super(context, user);
    }

    /*
    initialize the observed users list and call refresh data
     */
    @Override
    public void initializeData()
    {
       observed_users = new ArrayList<String>();
       refreshData();
    }

    /*
    For each User that the current User is following, make sure that that user has an entry in the
    data set for their most recent MoodEvent
     */
    @Override
    public void refreshData()
    {
        if (observed_users == null)
        {
            return;
        }

        List<String> followed_users = user.getFollowingList();

        for (String followed_user : followed_users)
        {
            if (!observed_users.contains(followed_user))
            {
                /*
                If the user is not followed (by UID), add that user to the observed_users list and
                create an Observer that updates the entry for that user whenever that User is changed
                 */
                User user = UserManager.getUser(followed_user);
                observed_users.add(followed_user);

                /*
                If the User doesn't need to be loaded by UserManager, add the entry immediately
                 */
                if (user.isLoaded())
                {
                    refreshEvent(user);
                }

                UserManager.addUserObserver(followed_user, (Observable o, Object a) ->
                {
                    refreshEvent((User) o);
                });
            }
        }
    }

    /*
    Finds the MoodEvent with User user in the data. If that MoodEvent doesn't exist, add the User's
    most recent MoodEvent, otherwise update the displayed MoodEvent to be the User's most recent MoodEvnet
     */
    public void refreshEvent(User user) {
        Log.d("TEST", "Refreshing event for " + user.getUserName());

        if (user.getMostRecentMoodEvent() == null)
        {
            return;
        }

        Comparator<Object> reverse_chronolgical = (Object o1, Object o2) ->
        {
            return ((MoodEvent)o2).compareTo(o1);
        };

        /*
        Iterate through the list and check if an event associated with user exists, if it does,
        replace that event with the most recent MoodEvent
         */
        boolean replaced = false;
        for (MoodEvent event : data)
        {


            if (event.getUser().getUid().equals(user.getUid()))
            {
                clear();

                data.remove(event);
                data.add(user.getMostRecentMoodEvent());
                //Collections.sort(data);
                data.sort(reverse_chronolgical);

                addAll(data);

                replaced = true;

                Log.d("TEST", "Replaced the event");

                break;
            }
        }

        /*
        If there wasn't an event to replace, add a new entry to the list
         */
        if (! replaced)
        {
            clear();

            data.add(user.getMostRecentMoodEvent());
            data.sort(reverse_chronolgical);
            //Collections.sort(data);

            addAll(data);

            Log.d("TEST", "Event didn't exist, added new event");
        }
    }

    /*
    Called for notifyObservers, if the user parameter is the current user, update the entire list,
    otherwise update the event associated with the User parameter
    @param observable
        A User object
    @param arg
        An optional (ignored) argument
     */
    @Override
    public void update(Observable observable, Object arg)
    {
        User user1 = (User) user;

        if (user1 == this.user)
        {
            refreshData();
        }
        else
        {
            refreshEvent(user1);
        }
    }

    /*
    Remove any observers that observed_users have
     */
    @Override
    public void destroy()
    {
        for (String user : observed_users)
        {
            UserManager.removeUserObservers(user);
        }
        super.destroy();
    }
}
