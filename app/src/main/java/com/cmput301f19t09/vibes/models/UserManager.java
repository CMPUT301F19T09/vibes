package com.cmput301f19t09.vibes.models;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.function.Consumer;

/*
This class holds a static map of loaded users, indexed by their UIDs. Allows getting a user by UID,
getting current user, and adding Observers to users
 */
public class UserManager
{
    private static Map<String, Pair<ListenerRegistration, User>> registeredUsers;
    private static String UID;

    static
    {
        registeredUsers = new HashMap<String, Pair<ListenerRegistration, User>>();
    }

    /*
    Add a user to the registeredUsers, create a snapshot listener in the db
    @param user_id
        the user's id to register
     */
    public static void registerUser(String user_id)
    {
        // Check if user is already registered
        if (registeredUsers.containsKey(user_id))
        {
            return;
        }
        else
        {
            // Create a new user and add it to registeredUsers
            User user = new User(user_id);

            // Immediately make a call to read the user data from the DB
            user.readData(new User.FirebaseCallback() {
                @Override
                public void onCallback(User u) {
                }
            });

            // Create a snapshot listener for this user (document) so the object will be
            // updated whenever the user document in the DB is
            ListenerRegistration registration = user.getSnapshotListener();
            registeredUsers.put(user_id,
                    new Pair(registration, user));
        }
    }

    /*
    Remove a user from registeredUsers. This removes the snapshot listener from the DB
    @param user_id
        the UID of the user to remove
     */
    public static void unregisterUser(String user_id)
    {
        // Make sure the user is already registered, also don't unregister current user
        if (registeredUsers.containsKey(user_id) && !UID.equals(user_id))
        {
            // Get the user, remove the registration then remove the user from the map
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            ListenerRegistration registration = p.first;
            registration.remove();

            registeredUsers.remove(user_id);
        }
    }

    /*
    Add an observer to the user specified by user_id
    @param user_id
        the id of the user to observe
    @param observer
        the observer
     */
    public static void addUserObserver(String user_id, Observer observer)
    {
        // If the user doesn't exist, create it
        if ( !registeredUsers.containsKey(user_id))
        {
            registerUser(user_id);
        }

        Log.d("TEST/UserManager", "add observer to " + user_id);

        Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
        User u = p.second;

        u.addObserver(observer);
    }

    /*
    Remove the observer from the user specified by user_id
    @param user_id
        the UID of the user to remove observer from
    @param observer
        the observer to remove
     */
    public static void removeUserObserver(String user_id, Observer observer)
    {
        Log.d("TEST/UserManager", "remove observer of " + user_id);
        if ( registeredUsers.containsKey(user_id))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            User user = p.second;

            user.deleteObserver(observer);

            if (user.countObservers() == 0)
            {
                unregisterUser(user_id);
            }
        }
    }

    /*
    remove all observers from the specified user
    @param user_id
        the UID of the user whose observers you are removing
     */
    public static void removeUserObservers(String user_id)
    {
        Log.d("TEST/UserManager", "remove observers of " + user_id);
        if ( registeredUsers.containsKey(user_id))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            User user = p.second;

            user.deleteObservers();
        }
    }

    /*
    return the user specified by user_id, if the user doesn't exist register it
    @param user_id
        The UID of the User to get
    @return
        The User with UID user_id
     */
    public static User getUser(String user_id)
    {
        if ( !registeredUsers.containsKey(user_id) )
        {
            registerUser(user_id);
        }

        return registeredUsers.get(user_id).second;
    }

    /*
    Set the current user to the user specified by the UID associated with the current Firebase user
    Anything calling this method should not return until callback is executed.
    @param callback
        A method to be called once the user's data has been read in. This is necessary due to many
        things depending on current user being valid.
     */
    public static void registerCurrentUser(final Consumer<User> callback)
    {
        // Get the UID from Firebase
        UID = FirebaseAuth.getInstance().getUid();
        User user = new User(UID);

        // Register the user
        ListenerRegistration registration = user.getSnapshotListener();
        registeredUsers.put(UID,
                new Pair(registration, user));

        // Get the user data from the DB, call callback once the read is complete
        user.readData(new User.FirebaseCallback() {
            @Override
            public void onCallback(User u) {
                callback.accept(u);
            }
        });
    }

    /*
    // Return the current user (as specified by Firebase)
    @return
        The user that has UID UID, should be the current user as specified by Firebase
     */
    public static User getCurrentUser()
    {
        if (registeredUsers.containsKey(UID))
        {
            return registeredUsers.get(UID).second;
        }
        else
        {
            return null;
        }
    }

    /*
    get the current user UID
    @return
        The UID associated with currentUser
     */
    public static String getCurrentUserUID()
    {
        return UID;
    }
}
