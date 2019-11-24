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

/*
This fragment holds the radio buttons for selecting MoodList's adapter and will also have a button
that opens the filter dialog
 */
public class MoodListFilterFragment extends Fragment
{
    private List<MoodFilterListener> listeners;
    private boolean locked; //This determines whether the radio buttons are shown (i.e. disallow a user from viewing
    // other user's moods when on their own profile

    /*
    return a new instance
     */
    public static MoodListFilterFragment newInstance()
    {
        return new MoodListFilterFragment();
    }

    /*
    initialize data
     */
    public MoodListFilterFragment()
    {
        listeners = new ArrayList<MoodFilterListener>();
        locked = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.mood_list_filter, container, false);

        View adapterSelectorLayout = view.findViewById(R.id.adapter_selector);

        RadioButton ownMoodsButton = adapterSelectorLayout.findViewById(R.id.radioYou);
        RadioButton followedMoodsButton = adapterSelectorLayout.findViewById(R.id.radioFollowed);
        ImageButton filterButton = view.findViewById(R.id.filter_button);

        filterButton.setBackgroundResource(R.drawable.ic_filter_list_black_36dp);

        //TODO: Open the filter dialog
        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Open filter dialog
            }
        });

        /*
        If it isnt locked, add listeners to the radio buttons
         */
        if (!locked)
        {
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
        }
        else
        {
            view.findViewById(R.id.radioGroup).setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public void disableRadioButtons()
    {
        locked = true;
    }

    /*
    Add a listener to be notified whenever the filter state is changed
     */
    public void addOnFilterListener(MoodFilterListener listener)
    {
        listeners.add(listener);
    }
}
