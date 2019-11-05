package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.models.User;

public class MoodListFragment extends Fragment
{

    public static MoodListFragment newInstance(User user)
    {
        MoodListFragment fragment = new MoodListFragment();
        Bundle arguments = new Bundle();

        arguments.putSerializable("user", user);
        fragment.setArguments(arguments);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}
