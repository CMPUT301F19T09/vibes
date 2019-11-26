package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.R;

/**
 * This is the MapFilter fragment that is used
 * to choose between the last mood of the people
 * that the primary user is following or the moods
 * of the primary user.
 */
public class MapFilter extends Fragment {
    /**
     * These static values help setting the filter for the MapFilter.
     * The declarations help keeping track of what the value of the filter is for the developers.
     */
    public static final int SHOW_MINE = 0;
    public static final int SHOW_EVERYONE = 1;

    /**
     * selectedRadioBox is the current status of the MapFilter.
     * It can have two values SHOW_MINE, or SHOW_EVERYONE
     */
    private int selectedRadioBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedRadioBox = bundle.getInt("SELECTED");
            Log.d("MapFilter", "SelectedMode: " + this.selectedRadioBox);
        }
    }

    /**
     * This is a static function that returns a MapFilter.
     * The filter parameter is used as an On-Start value.
     * There is no function for updating the MapFilter.
     * @param filter
     * This is a MapFragment.Filter object. It tells the MapFilter to start with Following/You filter.
     * @return
     */
    public static MapFilter getInstance(MapFragment.Filter filter){
        // The conditioning below checks if the selected filter exists.
        // Otherwise, it throws a RuntimeException.
        int mode;
        if(filter == MapFragment.Filter.SHOW_EVERYONE) {
            mode = SHOW_EVERYONE;
        }else if(filter == MapFragment.Filter.SHOW_MINE){
            mode = SHOW_MINE;
        }else{
            throw new RuntimeException("Error occured in getting instance of mapFilter");
        }

        // Sending the verified filter into the mapfilter in a bundle.
        Bundle filterBundle = new Bundle();
        filterBundle.putInt("SELECTED", mode);
        MapFilter filterFragment = new MapFilter();
        filterFragment.setArguments(filterBundle);
        return filterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_filter, container, false);

        // Setting the layout input views with their corresponding class variables.
        RadioButton youButton = v.findViewById(R.id.radioYou);
        RadioButton everyoneButton = v.findViewById(R.id.radioFollowed);
        RadioGroup group = v.findViewById(R.id.radioGroup);

        // This setOnCheckedChangeLister checks if the user interacts with the radioboxes at the top.
        // We chose to set the listener directly to the radio group as individual listeners
        // for the radio groups would require extra work to make sure only one is selected.
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == youButton.getId())
                {
                    ((MapFragment)getParentFragment()).switchFilter(MapFragment.Filter.SHOW_MINE, null);
                    everyoneButton.setChecked(false);
                }
                else {
                    ((MapFragment) getParentFragment()).switchFilter(MapFragment.Filter.SHOW_EVERYONE, null);
                    youButton.setChecked(false);
                }
        }});

        // Inflate the layout for this fragment
        return v;
    }
}
