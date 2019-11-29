package com.cmput301f19t09.vibes.models;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * This class manages a list of all Users that have been used in components of the app. Each User object
 * is given a snapshot-listener.
 */
public class UserManager
{
    // All users that have been created, associated with their listener
    private static Map<String, Pair<ListenerRegistration, User>> registeredUsers;
    private static String mainUserUID;

    static
    {
        registeredUsers = new HashMap<String, Pair<ListenerRegistration, User>>();
    }

    /**
     * Create a new User object for user_id, add a snapshot listener to that User
     * @param user_id The UID (in firebase) of the User to create
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
            user.readData((User u) -> { });

            // Add a snapshot listener to the new User and add the User to the registeredUsers map
            ListenerRegistration registration = user.getSnapshotListener();
            registeredUsers.put(user_id,
                    new Pair(registration, user));
        }
    }

    /**
     * Remove a User from registeredUsers and remove the SnapshotListener from the User
     * @param user_id The UID of the User to remove
     */
    public static void unregisterUser(String user_id)
    {
        // Make sure the user is already registered, also don't unregister current user
        if (registeredUsers.containsKey(user_id))
        {
            // Get the user, remove the registration then remove the user from the map
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            ListenerRegistration registration = p.first;
            registration.remove();

            registeredUsers.remove(user_id);
        }
    }

    /**
     * Add an observer the the User with UID
     * @param user_id The UID of the User to add an Observer to
     * @param observer The Observer to add to the User
     */
    public static void addUserObserver(String user_id, Observer observer)
    {
        // If the user doesn't exist, create it
        if ( !registeredUsers.containsKey(user_id))
        {
            registerUser(user_id);
        }

        Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
        User u = p.second;

        u.addObserver(observer);
    }

    /**
     * Remove the Observer observer from the User with UID user_id
     * @param user_id The UID of the User to remove the Observer from
     * @param observer The Observer to remove
     */
    public static void removeUserObserver(String user_id, Observer observer)
    {
        // Make sure the User exists before trying to remove an observer
        if ( registeredUsers.containsKey(user_id))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            User user = p.second;

            user.deleteObserver(observer);
        }
    }

    /**
     * Remove all Observers from the User with UID user_id
     * @param user_id The UID of the User to remove Observers from
     */
    public static void removeUserObservers(String user_id)
    {
        // Make sure that the User actually exists
        if ( registeredUsers.containsKey(user_id))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            User user = p.second;

            user.deleteObservers();
        }
    }

    /**
     * Return the User with UID. If the User has been registered, return it from registeredUsers, if
     * it hasn't then register it first.
     * @param user_id The UID of the User to return
     * @return The User object with UID user_id
     */
    public static User getUser(String user_id)
    {
        if ( !registeredUsers.containsKey(user_id) )
        {
            registerUser(user_id);
        }

        return registeredUsers.get(user_id).second;
    }

    /**
     * Register a new User and set that User as the main application User. Set the mainUserUID to
     * the FirebaseAuth UID of the currently signed-in User
     * @param callback A callback for when the main User has been registered
     */
    public static void registerCurrentUser(final Consumer<User> callback)
    {
        // Get the mainUserUID from FirebaseAuth
        mainUserUID = FirebaseAuth.getInstance().getUid();

        User user = new User(mainUserUID);

        // Register the user
        ListenerRegistration registration = user.getSnapshotListener();
        registeredUsers.put(mainUserUID,
                new Pair(registration, user));

        // Get the user data from the DB, call callback once the read is complete
        user.readData(new User.FirebaseCallback() {
            @Override
            public void onCallback(User u) {
                callback.accept(u);
            }
        });
    }

    /**
     * Returns the User object represen
     * @return
     */
    public static User getCurrentUser()
    {
        if (registeredUsers.containsKey(mainUserUID))
        {
            return registeredUsers.get(mainUserUID).second;
        }
        else
        {
            return null;
        }
    }

    /*
    get the current user mainUserUID
    @return
        The mainUserUID associated with currentUser
     */
    public static String getCurrentUserUID()
    {
        return mainUserUID;
    }

    public static void unregisterAllUsers() {

        for (String uid : new ArrayList<String>(registeredUsers.keySet())) {
            unregisterUser(uid);
        }
    }
}
