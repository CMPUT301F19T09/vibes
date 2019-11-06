package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.dialogs.MoodFilterDialog;

import java.util.ArrayList;
import java.util.List;

public class MoodListFilterFragment extends Fragment
{
    private List<MoodFilterListener> listeners;

    public static MoodListFilterFragment newInstance()
    {
        return new MoodListFilterFragment();
    }

    public MoodListFilterFragment()
    {
        listeners = new ArrayList<MoodFilterListener>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.mood_list_filter, container, false);

        View adapterSelectorLayout = view.findViewById(R.id.adapter_selector);

        ImageButton filterButton = view.findViewById(R.id.filter_button);
        RadioButton ownMoodsButton = adapterSelectorLayout.findViewById(R.id.radioYou);
        RadioButton followedMoodsButton = adapterSelectorLayout.findViewById(R.id.radioFollowed);

        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Open filter dialog
            }
        });

        ownMoodsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (MoodFilterListener listener : listeners)
                {
                    listener.showOwnMoods();
                }
            }
        });

        followedMoodsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (MoodFilterListener listener : listeners)
                {
                    listener.showFollowedMoods();
                }
            }
        });

        return view;
    }

    public void addOnFilterListener(MoodFilterListener listener)
    {
        listeners.add(listener);
    }
}
