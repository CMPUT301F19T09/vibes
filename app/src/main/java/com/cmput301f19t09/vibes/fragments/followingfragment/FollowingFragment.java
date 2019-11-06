package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.models.Mood;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    private ListView followingListView;
    private FollowingAdapter followingAdapter;
    private UserData followingList;

    private ListView requestedListView;
    private RequestedAdapter requestedAdapter;
    private UserData requestedList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        View view = inflater.inflate(R.layout.following_fragment, container, false);

        //Ref: https://stackoverflow.com/questions/15392261/android-pass-dataextras-to-a-fragment#15392591
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            followingList = (UserData) bundle.getSerializable("FollowingData");
            requestedList = (UserData) bundle.getSerializable("RequestedData");
        }
        else{
            Log.d("ERROR", "Bundle object passed was NULL.");
        }

        UserData userData = new UserData();
        userData.add(new User("a", "a", "a", "a"));
        followingList = userData;
        requestedList = userData;


        followingAdapter = new FollowingAdapter(getActivity(), followingList);
        followingListView = view.findViewById(R.id.following_list);
        followingListView.setAdapter(followingAdapter);

        requestedAdapter = new RequestedAdapter(getActivity(), requestedList);
        requestedListView = view.findViewById(R.id.requested_list);
        requestedListView.setAdapter(requestedAdapter);

        return view;
    }

}
