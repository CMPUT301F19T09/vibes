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

import java.util.Map;

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

        RadioGroup group = v.findViewById(R.id.radioGroup);

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
