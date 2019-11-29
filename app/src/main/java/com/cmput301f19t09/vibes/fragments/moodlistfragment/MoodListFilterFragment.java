package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.EmotionalState;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment allows filtering of MoodFilterListener Fragments
 */
public class MoodListFilterFragment extends Fragment
{
    private List<MoodFilterListener> listeners;
    private boolean locked; //This determines whether the radio buttons are shown (i.e. disallow a user from viewing

    /**
     * Return a new instance
     * @return
     */
    public static MoodListFilterFragment newInstance()
    {
        return new MoodListFilterFragment();
    }

    /**
     * Constructor. Sets locked to false by default
     */
    public MoodListFilterFragment()
    {
        listeners = new ArrayList<MoodFilterListener>();
        locked = false;
    }

    /**
     * An adapter for lists of EmotionalStates, for selecting the EmotionalState to filter
     */
    private class CustomFilterAdapter extends ArrayAdapter<String>{

        private List<String> moodList;

        public CustomFilterAdapter(@NonNull Context context, List<String> list) {
            super(context, 0, list);
            moodList = list;
        }


        /**
         * Inflate a view for an String corresponding to EmotionalState. View will show the emotion's image,
         * along with it's name and colour.
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;

            if(listItem == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                listItem = inflater.inflate(R.layout.filter_row_mood_item, null);
            }

            String mood = moodList.get(position);
            ImageView moodImage = listItem.findViewById(R.id.moodIcon);
            TextView moodName = listItem.findViewById(R.id.moodName);

            if(mood != "No Filter") {
                EmotionalState emotion = new EmotionalState(mood.toUpperCase());
                moodImage.setImageResource(emotion.getImageFile());
                moodName.setBackgroundTintList(ColorStateList.valueOf(emotion.getColour()));
            }else{
                moodImage.setVisibility(View.INVISIBLE);
            }

            moodName.setText(mood);

            return listItem;
        }
    }

    /**
     * Create the View for the filter Fragment and set the listeners for its buttons
     */
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

        filterButton.setBackgroundResource(R.drawable.ic_filter_none_black_24dp);

        /**
         * When the filter button is clicked, open a dialog allowing the selection of an EmotionalState to filter
         * into the list
         */
        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Select a mood filter:");

                List<String> keys = EmotionalState.getListOfKeys();
                for (int i = 0; i < keys.size(); i++)
                {
                    String replacement = keys.get(i);
                    replacement = replacement.charAt(0) + replacement.substring(1).toLowerCase(); // Proper capitalisation
                    keys.set(i, replacement);
                }
                final String noFilter = "No filter";
                List<String> moods = new ArrayList<String>();
                moods.add(noFilter);
                moods.addAll(keys);

                CustomFilterAdapter arrayAdapter = new CustomFilterAdapter(getContext(),moods);

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String strName = arrayAdapter.getItem(which);
                        strName = (strName.equals(noFilter)) ? null : strName.toUpperCase();
                        filter(strName);
                    }
                });
                builderSingle.show();
            }
        });

        /**
         * Set the MoodFilterListener's mode to show the moods corresponding to the button pressed
         */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == ownMoodsButton.getId())
                {
                    for (MoodFilterListener listener : listeners)
                    {
                        listener.showOwnMoods();
                    }
                }
                else
                {
                    for (MoodFilterListener listener : listeners)
                    {
                        listener.showFollowedMoods();
                    }
                }
            }
        });

        /**
         * If locked is true, then disable the radio buttons
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
        if (getView() != null) {
            getView().findViewById(R.id.radioGroup).setVisibility(View.INVISIBLE);
        }

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
