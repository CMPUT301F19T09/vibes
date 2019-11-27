package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 *
 * This fragment displays the list of users that have requested to follow
 * the current user (but have not yet been accepted) and the users that the
 * current users follows.
 */
public class FollowingFragment extends Fragment {

    /**
     * @return followingFragment : FollowingFragment
     *
     * Given a User object (corresponding to the current user), the constructor returns
     * a FollowingFragment that corresponds to the passed in user.
     */
    public static FollowingFragment newInstance() {
        return new FollowingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        final User user = UserManager.getCurrentUser();

        // Initializes ArrayList's for the users that are being followed and
        // the users that have requested to follow the current user.
        final ArrayList<String> followingList = new ArrayList<>();
        final ArrayList<String> requestedList = new ArrayList<>();

        final FollowingFragmentAdapter followingAdapter = new FollowingFragmentAdapter(getActivity(), "following");
        ListView followingLinearLayout = view.findViewById(R.id.following_list);
        followingLinearLayout.setAdapter(followingAdapter);

        final FollowingFragmentAdapter requestedAdapter = new FollowingFragmentAdapter(getActivity(), "request");
        ListView requestedLinearLayout = view.findViewById(R.id.requested_list);
        requestedLinearLayout.setAdapter(requestedAdapter);

        assert user != null;
        if (user.isLoaded()) {
            followingList.clear();
            followingAdapter.clear();

            requestedList.clear();
            requestedAdapter.clear();

            followingList.addAll(user.getFollowingList());
            requestedList.addAll(user.getRequestedList());

            followingAdapter.refreshData(followingList);
            requestedAdapter.refreshData(requestedList);
        }

        UserManager.addUserObserver(user.getUid(), (o, arg) -> {
            followingList.clear();
            followingAdapter.clear();

            requestedList.clear();
            requestedAdapter.clear();

            followingList.addAll(user.getFollowingList());
            requestedList.addAll(user.getRequestedList());

            followingAdapter.refreshData(followingList);
            requestedAdapter.refreshData(requestedList);
        });

        ViewTreeObserver listVTO = followingLinearLayout.getViewTreeObserver();
        listVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                followingLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                resizeListView(followingLinearLayout);
            }
        });

        ViewTreeObserver listVTO2 = requestedLinearLayout.getViewTreeObserver();
        listVTO2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                requestedLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                resizeListView(requestedLinearLayout);
            }
        });

        return view;
    }

    private void resizeListView(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int count = listAdapter.getCount();
        int itemHeight;

        View oneChild = listView.getChildAt(0);
        if (oneChild == null) {
            return;
        }

        itemHeight = oneChild.getHeight();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
        params.height = itemHeight * count;
        listView.setLayoutParams(params);
    }
}