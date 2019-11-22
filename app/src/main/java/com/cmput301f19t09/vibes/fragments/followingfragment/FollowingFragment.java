package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 * This fragment displays the list of users that have requested to follow
 * the current user (but have not yet been accepted) and the users that the
 * current users follows.
 */
public class FollowingFragment extends Fragment {

    /**
     * @param user : User
     *
     * @return followingFragment : FollowingFragment
     *
     * Given a User object (corresponding to the current user), the constructor returns
     * a FollowingFragment that corresponds to the passed in user.
     */
    public static FollowingFragment newInstance(User user) {
        FollowingFragment followingFragment = new FollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        followingFragment.setArguments(bundle);
        return followingFragment;
    }

    // Class variables
    private LinearLayout followingLinearLayout;
    private FollowingFragmentAdapter followingAdapter;
    private ArrayList<User> followingList;
    private LinearLayout requestedLinearLayout;
    private FollowingFragmentAdapter requestedAdapter;
    private ArrayList<User> requestedList;

    /**
     * @param inflater : LayoutInflater
     * @param container : ViewGroup
     * @param savedInstanceState : Bundle
     * @return view : View
     *
     * Gets the User object passed via serialization. A readData() is performed
     * on this object. For every followee and requestee in the current user's
     * following_list and requested_list, a readData is performed on that
     * user and that user is added to the followingList or requestedList
     * respectively.
     *
     * After the followingList and requestedList have been made, an ArrayAdapter
     * (for each) is made and filled with the data from the data list. That
     * ArrayAdapter is then connected to a ListView.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        View view = inflater.inflate(R.layout.following_fragment, container, false);

        // Gets the user object provided
        User user = (User) getArguments().getSerializable("user");

        // Initializes ArrayList's for the users that are being followed and
        // the users that have requested to follow the current user.
        followingList = new ArrayList<User>();
        requestedList = new ArrayList<User>();

        // Gets the data of the current user from the database
        user.readData(new User.FirebaseCallback() {
            @Override
            public void onCallback(User user) {
                // Gets the list of username's of the users that are being followed by the
                // current user
                List<String> followingUsernames = user.getFollowingList();
                // For every username, a user object is initialized and the data of that user is read
                for (String username : followingUsernames) {
                    User followee = new User(username);
                    followee.readData(new User.FirebaseCallback() {
                        @Override
                        public void onCallback(User user) {
                            // After the data has been read, the user being followed is added to the
                            // followingList and we notify a change in the data set
                            followingList.add(user);
                            followingAdapter.notifyDataSetChanged();
                            followingLinearLayout.addView(followingAdapter.getView(followingAdapter.getCount()-1, null ,null));
                        }
                    });
                }
                // Gets the list of username's of the users that the user requests to follow
                List<String> requestedUsernames = user.getRequestedList();
                // For every username, a user object is initialized and the data of that user is read
                for (String username : requestedUsernames) {
                    User requester = new User(username);
                    requester.readData(new User.FirebaseCallback() {
                        @Override
                        public void onCallback(User user) {
                            // After the data has been read, the user being followed is added to the
                            // requestedList and we notify a change in the data set
                            requestedList.add(user);
                            requestedAdapter.notifyDataSetChanged();
                            requestedLinearLayout.addView(requestedAdapter.getView(requestedAdapter.getCount()-1, null ,null));
                        }
                    });
                }
            }
        });

        // followingAdapter is created and is given the correct layout (requestedAdapter
        // is given a different layout). followingListView is provided a view and has
        // its adapter set to followingAdapter
        followingAdapter = new FollowingFragmentAdapter(getActivity(), followingList);
        followingAdapter.setLayout(R.layout.following_list);
        followingAdapter.setActivity(getActivity());
        followingLinearLayout = view.findViewById(R.id.following_list);

        // requestedAdapter is created and is given the correct layout (followingAdapter
        // is given a different layout). requestedListView is provided a view and has
        // its adapter set to requestedAdapter
        requestedAdapter = new FollowingFragmentAdapter(getActivity(), requestedList);
        requestedAdapter.setLayout(R.layout.requested_list);
        requestedAdapter.setActivity(getActivity());
        requestedLinearLayout = view.findViewById(R.id.requested_list);

        return view;
    }
}