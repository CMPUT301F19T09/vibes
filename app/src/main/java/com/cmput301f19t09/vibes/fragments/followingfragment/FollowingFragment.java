package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.Observable;
import java.util.Observer;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * This fragment displays the list of users that have requested to follow
 * the current user (but have not yet been accepted) and the users that the
 * current users follows.
 */
public class FollowingFragment extends Fragment {
    private User user;
    private FollowingFragmentAdapter followingAdapter;
    private FollowingFragmentAdapter requestedAdapter;
    private ListView followingLinearLayout;
    private ListView requestedLinearLayout;

    /**
     * @return followingFragment : FollowingFragment
     * <p>
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
     * @param inflater           : LayoutInflater
     * @param container          : ViewGroup
     * @param savedInstanceState : Bundle
     * @return view : View
     * <p>
     * Gets the User object passed via serialization. A readData() is performed
     * on this object. For every followee and requestee in the current user's
     * following_list and requested_list, a readData is performed on that
     * user and that user is added to the followingList or requestedList
     * respectively.
     * <p>
     * After the followingList and requestedList have been made, an ArrayAdapter
     * (for each) is made and filled with the data from the data list. That
     * ArrayAdapter is then connected to a ListView.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        View view = inflater.inflate(R.layout.following_fragment, container, false);

        // Gets the user object provided
        user = UserManager.getCurrentUser();

        followingAdapter = new FollowingFragmentAdapter(getActivity(), "following");
        followingLinearLayout = view.findViewById(R.id.following_list);
        followingLinearLayout.setAdapter(followingAdapter);

        requestedAdapter = new FollowingFragmentAdapter(getActivity(), "request");
        requestedLinearLayout = view.findViewById(R.id.requested_list);
        requestedLinearLayout.setAdapter(requestedAdapter);

        followingAdapter.refreshData(user.getFollowingList());
        requestedAdapter.refreshData(user.getRequestedList());

        // Allowing two listviews to scroll as one
        // Ref: https://stackoverflow.com/questions/27329419/merging-two-listviews-one-above-another-with-a-common-scroll
        ViewTreeObserver listVTO = followingLinearLayout.getViewTreeObserver();
        listVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                followingLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
                resizeListView(followingLinearLayout);
            }
        });

        ViewTreeObserver listVTO2 = requestedLinearLayout.getViewTreeObserver();
        listVTO2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                requestedLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
                resizeListView(requestedLinearLayout);
            }
        });

        return view;
    }

    /**
     * @param listView
     */
    private void resizeListView(ListView listView) {
        ArrayAdapter listAdapter = (ArrayAdapter) listView.getAdapter();
        int count = listAdapter.getCount();
        int itemHeight;

        View oneChild = listView.getChildAt(0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();

        if (oneChild == null) {
            itemHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                    getResources().getDisplayMetrics()));
        } else if (count > 0) {
            itemHeight = oneChild.getHeight();

        } else {
            itemHeight = 0;
        }

        params.height = itemHeight * count;
        listView.setLayoutParams(params);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        user.deleteObservers();

        for (String uid : user.getFollowingList()) {
            UserManager.removeUserObservers(uid);
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        UserManager.addUserObserver(UserManager.getCurrentUserUID(), new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                followingAdapter.refreshData(user.getFollowingList());
                requestedAdapter.refreshData(user.getRequestedList());

                resizeListView(followingLinearLayout);
                resizeListView(requestedLinearLayout);
            }
        });

        if (user.isLoaded()) {
            followingAdapter.refreshData(user.getFollowingList());
            requestedAdapter.refreshData(user.getRequestedList());
        }

        resizeListView(followingLinearLayout);
        resizeListView(requestedLinearLayout);

        super.onResume();
    }
}