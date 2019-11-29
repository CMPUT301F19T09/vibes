package com.cmput301f19t09.vibes.fragments.profilefragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.mooddetailsfragment.MoodDetailsFragment;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * ProfileFragment contains information about the the current user logged in or information of
 * other users being viewed based on whether you're following the other user or not. Calls a child
 * fragment to view user's own mood history if on own user profile but calls a child fragment to
 * view other user's most recent mood event if following
 *
 * Following other users not implemented yet
 */
public class ProfileFragment extends Fragment implements Observer {
    private TextView fullNameTextView;
    private TextView userNameTextView;
    private ImageView profilePictureImageView;
    private Button followButton;
    private User otherUser;
    private User user;
    private String otherUserUID;
    private Fragment childFragment;
    private enum Mode {OWN, FOLLOWING, REQUESTED, NONE}

    /**
     * Creates a new instance of the profile fragment for the current user
     * @return ProfileFragment of the current user
     */
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    /**
     * Creates a new instance of the profile fragment for the user being viewed
     * @param otherUserUID The UID of the other user you want to view
     * @return ProfileFragment of the user you want to view
     */
    public static ProfileFragment newInstance(String otherUserUID) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("otherUserUID", otherUserUID);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    /**
     * Gets bundled arguments when creating the view and creates a child fragment based on which
     * user you are observing. Creates other profile if not yours and creates your own profile
     * if yours.
     * @param savedInstanceState Saved instance state of the MainActivity
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        // Verifies if other user's UID is passed in through new instance
        otherUserUID = null;
        if (getArguments() != null) {
            otherUserUID = getArguments().getString("otherUserUID");
        }

        // Creates a user and observer for the user being viewed
        otherUser = null;
        // Gets the current user from UserManager
        user = UserManager.getCurrentUser();
        if (otherUserUID != null) {
            otherUser = UserManager.getUser(otherUserUID);
        }

        assert user != null;
        UserManager.addUserObserver(user.getUid(), this);

        // Verifies if user exists
        if (user == null) {
            throw new RuntimeException("[ERROR]: USER IS NOT DEFINED");
        }

        // Creates the child fragment onCreate
        if (childFragment == null) {
            if (otherUser == null) {
                childFragment = MoodListFragment.newInstance(MoodListFragment.OWN_MOODS_LOCKED);
            } else {
                childFragment = MoodDetailsFragment.newInstance(otherUser.getMostRecentMoodEvent());
            }

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.user_mood_list, childFragment,
                    childFragment.getClass().getSimpleName());
            transaction.commit();
        }

        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view of the ProfileFragment and loading specific fields with values based on
     * who's profile is being viewed
     * @param inflater Makes the view of the fragment from the XML layout file
     * @param container Parent container to store the fragment in
     * @param savedInstanceState Saved instance state of the MainActivity
     * @return The created ProfileFragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        // Get specific views
        fullNameTextView = view.findViewById(R.id.fullname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        profilePictureImageView = view.findViewById(R.id.profile_picture);
        followButton = view.findViewById(R.id.follow_button);

        return view;
    }

    /**
     * Shows information of whichever user you're view when the view is created
     * @param view Gets the current view of the fragment
     * @param savedInstanceState Saved instance state of the MainActivity
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (otherUser != null) {
            setInfo(otherUser);
        } else {
            setInfo(user);
        }

        checkMode();
    }

    /**
     * Updates the fragment with new values if there was an update from the database.
     * @param user The user object being observed for changes
     * @param object Any object being handed in (will always be null)
     */
    @Override
    public void update(Observable user, Object object) {
        if (((User)user).isLoaded() &&
                (otherUser == null || (otherUser != null &&
                        ((User)user).getUid().equals(otherUserUID)))) {
            setInfo((User) user);
        }

        if (otherUser != null) {
            if (otherUser.getMostRecentMoodEvent() == null) {
                hideChild();
            } else {
                ((MoodDetailsFragment)childFragment)
                        .setMoodEvent(otherUser.getMostRecentMoodEvent());
            }
        }

        checkMode();
    }

    /**
     * Updates the button, show or hide the child fragment, and set listeners based on the current
     * mode
     * @param mode Mode to set views and listeners
     */
    private void updateButton(Mode mode) {
        switch (mode) {
            case OWN:
                followButton.setVisibility(View.INVISIBLE);
                profilePictureImageView.setOnClickListener(view -> openFileExplorer());
                break;

            case REQUESTED:
                hideChild();
                followButton.setText(R.string.requested);
                followButton.setTextColor(Color.parseColor("#FF6A88"));
                followButton.setBackgroundResource(R.drawable.rounded_button_outline);
                followButton.setOnClickListener(view -> {
                    otherUser.removeRequest(user.getUid());
                });
                break;

            case FOLLOWING:
                showChild();
                followButton.setText(R.string.unfollow);
                followButton.setTextColor(Color.parseColor("#A2A2A2"));
                followButton.setBackgroundResource(R.drawable.rounded_button_grey_outline);
                followButton.setOnClickListener(view -> {
                    user.removeFollowing(otherUser.getUid());
                });
                break;

            case NONE:
                hideChild();
                followButton.setText(R.string.follow);
                followButton.setTextColor(Color.parseColor("#FFFFFF"));
                followButton.setBackgroundResource(R.drawable.rounded_button);
                followButton.setOnClickListener(view -> {
                    otherUser.addRequest(user.getUid());
                });
                break;
        }
    }

    /**
     * Updates the fields with user information
     * @param user The object to get the values from
     */
    private void setInfo(final User user) {
            fullNameTextView.setText(String.format("%s %s",
                    user.getFirstName(), user.getLastName()));
            userNameTextView.setText(user.getUserName());
            Glide.with(this).load(user.getProfileURL()).into(profilePictureImageView);
            profilePictureImageView.setClipToOutline(true);
    }

    /**
     * Adds observers on resume of fragment. Creates an observer of the other user if the fragment
     * is for the other user
     */
    @Override
    public void onResume() {
        super.onResume();
        if (otherUser != null) {
            UserManager.addUserObserver(otherUserUID, this);
            ((MoodDetailsFragment)childFragment).setMoodEvent(otherUser.getMostRecentMoodEvent());
        }
        UserManager.addUserObserver(UserManager.getCurrentUserUID(), this);

        checkMode();
    }

    /**
     * Removes observers when switching fragments. Removes an observer of the other user if the
     * fragment is for the other user
     */
    @Override
    public void onPause() {
        super.onPause();
        UserManager.removeUserObserver(UserManager.getCurrentUserUID(), this);
        if (otherUser != null) {
            UserManager.removeUserObserver(otherUser.getUid(), this);
        }
    }

    /**
     * Opens the default Android file explorer to select a new profile picture
     */
    private void openFileExplorer() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, 42);
    }

    /**
     * Gets the data returned when returned back to the profile fragment
     * @param requestCode The code of the file explorer
     * @param resultCode Result code from the file explorer
     * @param data The new photo received from the file explorer
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 42) {
            Uri uri;

            if (data != null) {
                uri = data.getData();
                user.changeProfilePicture(uri);
            }
        }
    }

    /**
     * Hides the child fragment from the profile fragment
     */
    private void hideChild() {
        View childView = Objects.requireNonNull(getChildFragmentManager()
                .findFragmentByTag(MoodDetailsFragment.class.getSimpleName())).getView();
        if (childView != null) {
            childView.setVisibility(View.GONE);
        }
    }

    /**
     * Shoes the child fragment from the profile fragment
     */
    private void showChild() {
        if (otherUser.getMostRecentMoodEvent() == null) {
            return;
        }

        View childView = Objects.requireNonNull(getChildFragmentManager()
                .findFragmentByTag(MoodDetailsFragment.class.getSimpleName())).getView();
        if (childView != null) {
            childView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Conditions to check what to set the mode for the updateButton method
     */
    private void checkMode() {
        if (otherUser == null) {
            updateButton(Mode.OWN);
            return;
        }

        if (user.getFollowingList().contains(otherUserUID)) {
            updateButton(Mode.FOLLOWING);
        } else if (otherUser.getRequestedList().contains(user.getUid())) {
            updateButton(Mode.REQUESTED);
        } else {
            updateButton(Mode.NONE);
        }
    }
}
