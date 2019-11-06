package com.cmput301f19t09.vibes.fragments.followingfragment;

import com.cmput301f19t09.vibes.models.User;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData extends ArrayList<User> implements Serializable {
    @Override
    public boolean add(User user){
        super.add(user);
        return true;
    }
}
