package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.R;

public class MoodListFilterFragment extends Fragment
{
    public static MoodListFilterFragment newInstance()
    {
        return new MoodListFilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.mood_list_filter, container, null);

        return view;
    }
}
