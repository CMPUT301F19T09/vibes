package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
        RadioGroup radioGroup = adapterSelectorLayout.findViewById(R.id.radioGroup);
        RadioButton ownMoodsButton = adapterSelectorLayout.findViewById(R.id.radioYou);
        RadioButton followedMoodsButton = adapterSelectorLayout.findViewById(R.id.radioFollowed);

        ImageButton filterButton = view.findViewById(R.id.filter_button);

        //TODO: Open the filter dialog
        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Open filter dialog
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == ownMoodsButton.getId())
                {
                    for (MoodFilterListener listener : listeners)
                    {
                        listener.showOwnMoods();
                    }
                    followedMoodsButton.setChecked(false);
                }
                else
                {
                    for (MoodFilterListener listener : listeners)
                    {
                        listener.showFollowedMoods();
                    }
                    ownMoodsButton.setChecked(false);
                }
            }
        });

        /*
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

         */
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if (locked)
        {
            getView().findViewById(R.id.radioGroup).setVisibility(View.GONE);
        }
    }

    public void disableRadioButtons()
    {
        View view = getView();

        view.findViewById(R.id.radioGroup).setVisibility(View.INVISIBLE);
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
