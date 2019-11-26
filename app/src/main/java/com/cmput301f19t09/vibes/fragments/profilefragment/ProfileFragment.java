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
     * Creates the view of the ProfileFragment and loading specific fields with values based on
     * who's profile is being viewed
     * @param inflater Makes the view of the fragment from the XML layout file
     * @param container Parent container to store the fragment in
     * @param savedInstanceState Saved instance state of the MainActivity
     * @return The created ProfileFragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        // Get specific views
        fullNameTextView = view.findViewById(R.id.fullname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        profilePictureImageView = view.findViewById(R.id.profile_picture);
        followButton = view.findViewById(R.id.follow_button);

        // Verifies if other user's UID is passed in through new instance
        otherUserUID = null;
        if (getArguments() != null) {
            otherUserUID = getArguments().getString("otherUserUID");
        }

        // Creates a user and observer for the user being viewed
        otherUser = null;
        if (otherUserUID != null) {
            otherUser = UserManager.getUser(otherUserUID);
            UserManager.addUserObserver(otherUserUID, this);
        }

        // Gets the current user from UserManager
        user = UserManager.getCurrentUser();

        // Verifies if user exists
        if (user == null) {
            throw new RuntimeException("[ERROR]: USER IS NOT DEFINED");
        }

        // Show's your own profile if other user wasn't passed in, set's the info, and gets the child
        // fragment MoodListFragment of the mood list of the current user
        if (otherUser == null) {
            MoodListFragment moodListFragment = MoodListFragment.newInstance(MoodListFragment.OWN_MOODS_LOCKED);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.user_mood_list, moodListFragment, MoodListFragment.class.getSimpleName()).commit();
            updateButton("OWN");
        } else {
            // Checks if the user is following the other user and show their latest mood event by
            // calling the child fragment MoodDetailsFragment

            UserManager.addUserObserver(otherUser.getUid(), this);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.user_mood_list, MoodDetailsFragment.newInstance(otherUser.getMostRecentMoodEvent()), MoodDetailsFragment.class.getSimpleName());
            transaction.commitNow();

            if (otherUser.isLoaded()) {
                if (otherUser.getRequestedList().contains(UserManager.getCurrentUserUID()) && !user.getFollowingList().contains(otherUserUID)) {
                    updateButton("REQUESTED");
                } else if (!otherUser.getRequestedList().contains(UserManager.getCurrentUserUID()) && !user.getFollowingList().contains(otherUserUID)){
                    updateButton("NONE");
                } else if (!otherUser.getRequestedList().contains(UserManager.getCurrentUserUID()) && user.getFollowingList().contains(otherUserUID)) {
                    updateButton("FOLLOWING");
                }
            }

            otherUser.addObserver((observable, o) -> {
                FragmentTransaction transaction1 = getChildFragmentManager().beginTransaction();
                transaction1.replace(R.id.user_mood_list, MoodDetailsFragment.newInstance(otherUser.getMostRecentMoodEvent()), MoodDetailsFragment.class.getSimpleName());
                transaction1.commitNow();

                if (otherUser.getRequestedList().contains(UserManager.getCurrentUserUID()) && !user.getFollowingList().contains(otherUserUID)) {
                    updateButton("REQUESTED");
                } else if (!otherUser.getRequestedList().contains(UserManager.getCurrentUserUID()) && !user.getFollowingList().contains(otherUserUID)){
                    updateButton("NONE");
                } else if (!otherUser.getRequestedList().contains(UserManager.getCurrentUserUID()) && user.getFollowingList().contains(otherUserUID)) {
                    updateButton("FOLLOWING");
                }
            });
        }

        return view;
    }

    /**
     * Updates the fragment with new values if there was an update from the database.
     * @param user The user object being observed for changes
     * @param object Any object being handed in (will always be null)
     */
    @Override
    public void update(Observable user, Object object) {
        setInfo((User) user);
    }

    private void updateButton(String mode) {
        switch (mode) {
            case "OWN":
                followButton.setVisibility(View.INVISIBLE);
                profilePictureImageView.setOnClickListener(view -> openFileExplorer());

                setInfo(user);
                break;

            case "REQUESTED":
                hideChild();
                followButton.setText(R.string.requested);
                followButton.setTextColor(Color.parseColor("#FF6A88"));
                followButton.setBackgroundResource(R.drawable.rounded_button_outline);
                followButton.setOnClickListener(view -> {
                    otherUser.removeRequest(user.getUid());
                    updateButton("NONE");
                });

                setInfo(otherUser);
                break;

            case "FOLLOWING":
                showChild();
                followButton.setText(R.string.unfollow);
                followButton.setTextColor(Color.parseColor("#A2A2A2"));
                followButton.setBackgroundResource(R.drawable.rounded_button_grey_outline);
                followButton.setOnClickListener(view -> {
                    user.removeFollowing(otherUser.getUid());
                    updateButton("NONE");
                });

                setInfo(otherUser);
                break;

            case "NONE":
                hideChild();
                followButton.setText(R.string.follow);
                followButton.setTextColor(Color.parseColor("#FFFFFF"));
                followButton.setBackgroundResource(R.drawable.rounded_button);
                followButton.setOnClickListener(view -> {
                    otherUser.addRequest(user.getUid());
                    updateButton("REQUESTED");
                });

                setInfo(otherUser);
                break;
        }
    }

    /**
     * Updates the fields with user information
     * @param user The object to get the values from
     */
    private void setInfo(final User user) {
        if (user.isLoaded()) {
            fullNameTextView.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            userNameTextView.setText(user.getUserName());
            Glide.with(this).load(user.getProfileURL()).into(profilePictureImageView);
            profilePictureImageView.setClipToOutline(true);
        }

        user.addObserver((observable, o) -> {
            fullNameTextView.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            userNameTextView.setText(user.getUserName());
            Glide.with(ProfileFragment.this).load(user.getProfileURL()).into(profilePictureImageView);
            profilePictureImageView.setClipToOutline(true);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (otherUser != null) {
            UserManager.addUserObserver(otherUserUID, this);
        } else {
            UserManager.addUserObserver(UserManager.getCurrentUserUID(), this);
        }
    }

    /**
     * Removes observers when switch fragments
     */
    @Override
    public void onPause() {
        super.onPause();
        UserManager.removeUserObserver(UserManager.getCurrentUserUID(), this);
        if (otherUser != null) {
            UserManager.removeUserObservers(otherUser.getUid());
        }
    }

    private void openFileExplorer() {
        System.out.println(user.getProfileURL());
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, 42);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 42) {
            Uri uri;

            if (data != null) {
                uri = data.getData();
                Glide.with(this).load(uri).into(profilePictureImageView);
                user.changeProfilePicture(uri);
            }
        }
    }

    private void hideChild() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.hide(Objects.requireNonNull(getChildFragmentManager().findFragmentByTag(MoodDetailsFragment.class.getSimpleName())));
        fragmentTransaction.commit();
    }

    private void showChild() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.show(Objects.requireNonNull(getChildFragmentManager().findFragmentByTag(MoodDetailsFragment.class.getSimpleName())));
        fragmentTransaction.commit();
    }
}
