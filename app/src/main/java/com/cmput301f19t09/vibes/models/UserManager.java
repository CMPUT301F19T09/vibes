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

    private static void registerUser(String username)
    {
        if (registeredUsers.containsKey(username))
        {
            return;
        }
        else
        {
            User user = new User(username);
            ListenerRegistration registration = user.getSnapshotListener();
            registeredUsers.put(username,
                    new Pair(registration, user));
        }
    }

    private static void unregisterUser(String username)
    {
        if (registeredUsers.containsKey(username))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(username);
            ListenerRegistration registration = p.first;
            registration.remove();

            registeredUsers.remove(username);
        }
    }

    public static void addUserObserver(String username, Observer observer)
    {
        if ( !registeredUsers.containsKey(username))
        {
            registerUser(username);
        }

        Pair<ListenerRegistration, User> p = registeredUsers.get(username);
        User u = p.second;

        u.addObserver(observer);
    }

    public static void removeUserObserver(String username, Observer observer)
    {
        if ( registeredUsers.containsKey(username))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(username);
            User user = p.second;

            user.deleteObserver(observer);

            if (user.countObservers() == 0)
            {
                unregisterUser(user.getUserName());
            }
        }
    }

    public static User getUser(String username)
    {
        if ( !registeredUsers.containsKey(username) )
        {
            registerUser(username);
        }

        return registeredUsers.get(username).second;
    }
}
