package com.cmput301f19t09.vibes.models;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class UserManager
{
    private static Map<String, Pair<ListenerRegistration, User>> registeredUsers;

    static
    {
        registeredUsers = new HashMap<String, Pair<ListenerRegistration, User>>();
    }

    public static void registerUser(String user_id)
    {
        if (registeredUsers.containsKey(user_id))
        {
            return;
        }
        else
        {
            User user = new User(user_id);
            ListenerRegistration registration = user.getSnapshotListener();
            registeredUsers.put(user_id,
                    new Pair(registration, user));
        }
    }

    public static void unregisterUser(String user_id)
    {
        if (registeredUsers.containsKey(user_id))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            ListenerRegistration registration = p.first;
            registration.remove();

            registeredUsers.remove(user_id);
        }
    }

    public static void addUserObserver(String user_id, Observer observer)
    {
        if ( !registeredUsers.containsKey(user_id))
        {
            registerUser(user_id);
        }

        Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
        User u = p.second;

        u.addObserver(observer);
    }

    public static void removeUserObserver(String user_id, Observer observer)
    {
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

    public static User getUser(String user_id)
    {
        if ( !registeredUsers.containsKey(user_id) )
        {
            registerUser(user_id);
        }

        return registeredUsers.get(user_id).second;
    }
}
