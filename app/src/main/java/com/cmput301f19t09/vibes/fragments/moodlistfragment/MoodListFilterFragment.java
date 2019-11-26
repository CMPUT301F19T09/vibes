package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.dialogs.MoodFilterDialog;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFragment;
import com.cmput301f19t09.vibes.models.EmotionalState;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment holds the radio buttons for selecting MoodList's adapter and will also have a button
 * that opens the filter dialog
 */
public class MoodListFilterFragment extends Fragment
{
    private List<MoodFilterListener> listeners;
    private boolean locked; //This determines whether the radio buttons are shown (i.e. disallow a user from viewing
    // other user's moods when on their own profile

    /**
     * Return a new instance
     * @return
     */
    public static MoodListFilterFragment newInstance()
    {
        return new MoodListFilterFragment();
    }

    /**
     * initialize data
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

        ownMoodsButton.setSelected(true);
        followedMoodsButton.setSelected(false);

        ImageButton filterButton = view.findViewById(R.id.filter_button);

        filterButton.setBackgroundResource(R.drawable.ic_filter_none_black_24dp);

        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Select a mood filter:");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

                List<String> keys = EmotionalState.getListOfKeys();
                for (int i = 0; i < keys.size(); i++)
                {
                    String replacement = keys.get(i);
                    replacement = replacement.substring(0, 1) + replacement.substring(1, replacement.length()).toLowerCase();
                    keys.set(i, replacement);
                }
                final String noFilter = "No Filter";
                arrayAdapter.add(noFilter);
                arrayAdapter.addAll(keys);


                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        strName = (strName.equals(noFilter)) ? null : strName.toUpperCase();
                        filter(strName);
                    }
                });
                builderSingle.show();
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
        If it isn't locked, add listeners to the radio buttons
         */
        if (locked)
        {
            view.findViewById(R.id.radioGroup).setVisibility(View.INVISIBLE);
        }

        return view;
    }

    /**
     * The purpose of this function:
     * -    After selecting an emotion with the filter dialog,
     * This function is called to start the filterization of the
     * mood list in the main activity.
     * @param emotion
     */
    public void filter(String emotion){
        for (MoodFilterListener listener : listeners)
        {
            listener.setFilter(emotion);
        }

    }

    /**
     * Disables the radio buttons in the list filter
     */
    public void disableRadioButtons()
    {
        locked = true;
    }

    /**
     * Add a listener to be notified whenever the filter state is changed
     * @param listener
     */
    public void addOnFilterListener(MoodFilterListener listener)
    {
        listeners.add(listener);
    }
}
