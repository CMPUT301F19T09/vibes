package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    public static FollowingFragment newInstance(User user) {
        FollowingFragment followingFragment = new FollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        followingFragment.setArguments(bundle);
        return followingFragment;
    }

    private ListView followingListView;
    private FollowingFragmentAdapter followingAdapter;
    private ArrayList<User> followingList;

    private ListView requestedListView;
    private FollowingFragmentAdapter requestedAdapter;
    private ArrayList<User> requestedList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        View view = inflater.inflate(R.layout.following_fragment, container, false);

        User user = (User) getArguments().getSerializable("user");

        followingList = new ArrayList<User>();
        requestedList = new ArrayList<User>();

        user.readData(new User.FirebaseCallback() {
            @Override
            public void onCallback(User user) {
                List<String> followingUsernames = user.getFollowingList();
                for (String username : followingUsernames) {
                    User followee = new User(username);
                    followee.readData(new User.FirebaseCallback() {
                        @Override
                        public void onCallback(User user) {
                            followingList.add(user);
                            followingAdapter.notifyDataSetChanged();
                        }
                    });
                }
                List<String> requestedUsernames = user.getRequestedList();
                for (String username : requestedUsernames) {
                    User requester = new User(username);
                    requester.readData(new User.FirebaseCallback() {
                        @Override
                        public void onCallback(User user) {
                            requestedList.add(user);
                            requestedAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        followingAdapter = new FollowingFragmentAdapter(getActivity(), followingList);
        followingAdapter.setLayout(R.layout.following_list);
        followingListView = view.findViewById(R.id.following_list);
        followingListView.setAdapter(followingAdapter);

        requestedAdapter = new FollowingFragmentAdapter(getActivity(), requestedList);
        requestedAdapter.setLayout(R.layout.requested_list);
        requestedListView = view.findViewById(R.id.requested_list);
        requestedListView.setAdapter(requestedAdapter);

        return view;
    }

}