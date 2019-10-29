package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.models.Mood;
import com.cmput301f19t09.vibes.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    private ListView moodList;
    private MoodAdapter moodAdapter;
    private ArrayList<Mood> dataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        View view = inflater.inflate(R.layout.following_fragment, container, false);


        dataList = MainActivity.getDataList();
        moodAdapter = new MoodAdapter(getActivity(), dataList);
        moodList = view.findViewById(R.id.mood_list);
        moodList.setAdapter(moodAdapter);

        return view;
    }

}
