package com.cmput301f19t09.vibes;

import androidx.annotation.NonNull;

import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class DeleteUser
{
    public static void deleteUser(String uid)
    {
        deleteUser(UserManager.getUser(uid));
    }

    public static void deleteUser(User u)
    {
        deleteUser(u.getUid(), u.getUserName());
    }

    public static void deleteUser(String uid, String username)
    {
        if (uid == null || username == null)
        {
            System.out.println("DELETE USER: UID or Username cannot be null");
            return;
        }

        Map<String, Object> data = new HashMap<>();

        data.put("uid", uid);
        data.put("username", username);

        FirebaseFunctions.getInstance().getHttpsCallable("deleteUser").call(data);
    }
}
