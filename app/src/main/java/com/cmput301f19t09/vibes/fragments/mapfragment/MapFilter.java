package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.R;

public class MapFilter extends Fragment {
    public static final int SHOW_MINE = 0;
    public static final int SHOW_EVERYONE = 1;

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

    public static MapFilter getInstance(MapFragment.Filter filter){
        int mode;

        if(filter == MapFragment.Filter.SHOW_EVERYONE) {
            mode = SHOW_EVERYONE;
        }else if(filter == MapFragment.Filter.SHOW_MINE){
            mode = SHOW_MINE;
        }else{
            throw new RuntimeException("Error occured in getting instance of mapFilter");
        }

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

        RadioButton youButton = v.findViewById(R.id.radioYou);
        RadioButton everyoneButton = v.findViewById(R.id.radioFollowed);

        switch(selectedRadioBox){
            case SHOW_EVERYONE:
                everyoneButton.setChecked(true);
                break;
            case SHOW_MINE:
                youButton.setChecked(true);
                break;
        }

        youButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedRadioBox == SHOW_EVERYONE) {
                    // Setting it to be mine if it is everyone
                    //((MainActivity) getActivity()).switchMapFilter(MapFragment.Filter.SHOW_MINE);
                    selectedRadioBox = SHOW_MINE;
                    ((MapFragment)getParentFragment()).switchFilter(MapFragment.Filter.SHOW_MINE, null);
                }
            }
        });

        everyoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedRadioBox == SHOW_MINE){
                    // Setting it to be everyone if it is only mine.
                    //((MainActivity) getActivity()).switchMapFilter(MapFragment.Filter.SHOW_EVERYONE);
                    selectedRadioBox = SHOW_EVERYONE;
                    ((MapFragment)getParentFragment()).switchFilter(MapFragment.Filter.SHOW_EVERYONE, null);
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}
