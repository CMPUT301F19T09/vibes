package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;

public class MapFilter extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            data = (MapData) bundle.getSerializable("MapData");
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.map_filter, container, false);

        RadioButton youButton = v.findViewById(R.id.radioYou);
        RadioButton everyoneButton = v.findViewById(R.id.radioFollowed);

        youButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((MainActivity) getActivity()).getMapFilter() == MapFragment.Filter.SHOW_EVERYONE) {
                    // Setting it to be mine if it is everyone
                    ((MainActivity) getActivity()).switchMapFilter(MapFragment.Filter.SHOW_MINE);
                }
            }
        });

        everyoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((MainActivity) getActivity()).getMapFilter() == MapFragment.Filter.SHOW_MINE){
                    // Setting it to be everyone if it is only mine.
                    ((MainActivity) getActivity()).switchMapFilter(MapFragment.Filter.SHOW_EVERYONE);
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void  setFilterToYou(RadioButton v){

    }

    public void setFilterEveryone(RadioButton v){

    }

}
