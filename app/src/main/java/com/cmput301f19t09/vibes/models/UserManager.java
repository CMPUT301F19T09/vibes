package com.cmput301f19t09.vibes.models;

import android.util.Log;
import android.util.Pair;

import com.cmput301f19t09.vibes.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.function.Consumer;

public class UserManager
{
    private static Map<String, Pair<ListenerRegistration, User>> registeredUsers;
    private static String UID;

    static
    {
        registeredUsers = new HashMap<String, Pair<ListenerRegistration, User>>();
    }

    public static void registerUser(String user_id)
    {
        //Log.d("USERMANAGER", "Register user: " + user_id);
        if (registeredUsers.containsKey(user_id))
        {
            return;
        }
        else
        {
            User user = new User(user_id);
            user.readData((User u) -> {});
            ListenerRegistration registration = user.getSnapshotListener();
            registeredUsers.put(user_id,
                    new Pair(registration, user));
        }
    }

    public static void unregisterUser(String user_id)
    {
        if (registeredUsers.containsKey(user_id) && !UID.equals(user_id))
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

    public static void removeUserObservers(String user_id)
    {

        //Log.d("USERMANAGER", "trying to delete all observers from " + user_id);
        if ( registeredUsers.containsKey(user_id))
        {
            Pair<ListenerRegistration, User> p = registeredUsers.get(user_id);
            User user = p.second;

            //Log.d("USERMANAGER", "deleting all observers from " + p.second.getUserName());

            user.deleteObservers();
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

    public static void registerCurrentUser(Consumer<User> callback)
    {
        UID = FirebaseAuth.getInstance().getUid();
        User user = new User(UID);
        ListenerRegistration registration = user.getSnapshotListener();
        registeredUsers.put(UID,
                new Pair(registration, user));
        user.readData((User u) -> {
            callback.accept(u);
        });
    }

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

    public static String getCurrentUserUID()
    {
        return UID;
    }
}
