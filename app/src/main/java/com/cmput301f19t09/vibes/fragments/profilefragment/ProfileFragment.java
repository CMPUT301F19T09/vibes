package com.cmput301f19t09.vibes.fragments.profilefragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.mooddetailsfragment.MoodDetailsFragment;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.Observable;
import java.util.Observer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment implements Observer {
    public static final String PROFILE_FRAGMENT_TAG = "ProfileFragment";
    private TextView fullNameTextView;
    private TextView userNameTextView;
    private ImageView profilePictureImageView;
    private Button followButton;
    private User otherUser;
    private User user;

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    public static ProfileFragment newInstance(String otherUserUID) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("otherUserUID", otherUserUID);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        fullNameTextView = view.findViewById(R.id.fullname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        profilePictureImageView = view.findViewById(R.id.profile_picture);
        followButton = view.findViewById(R.id.follow_button);

        String otherUserUID = null;
        if (getArguments() != null) {
            otherUserUID = getArguments().getString("otherUserUID");
        }
        otherUser = null;
        if (otherUserUID != null) {
            otherUser = UserManager.getUser(otherUserUID);
            UserManager.addUserObserver(otherUserUID, this);
        }

        user = UserManager.getCurrentUser();

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "REQUESTED", Toast.LENGTH_LONG).show();
            }
        });

        if (user == null) {
            throw new RuntimeException("YOU DUN GOOFED");
        }

        if (otherUser == null) {
            followButton.setVisibility(View.INVISIBLE);
            UserManager.addUserObserver(user.getUid(), this);
            setInfo(user);
            MoodListFragment moodListFragment = MoodListFragment.newInstance(MoodListFragment.OWN_MOODS_LOCKED);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.user_mood_list, moodListFragment).commit();

        } else {
            followButton.setVisibility(View.VISIBLE);
            if (otherUser.isLoaded())
            {
                setInfo(otherUser);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.user_mood_list, MoodDetailsFragment.newInstance(otherUser.getMostRecentMoodEvent()));
                transaction.commit();
            }

            otherUser.addObserver((Observable o, Object arg) ->
            {
                User u = (User) o;
                setInfo(u);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.user_mood_list, MoodDetailsFragment.newInstance(u.getMostRecentMoodEvent()));
                transaction.commit();
            });
            setInfo(otherUser);
        }
        return view;
    }

    @Override
    public void update(Observable user, Object object) {
        setInfo((User) user);
    }

    /**
     *
     * @param user
     */
    public void setInfo(User user) {
        if (user.isLoaded()) {
            fullNameTextView.setText(user.getFirstName() + " " + user.getLastName());
            userNameTextView.setText(user.getUserName());
            Glide.with(this).load(user.getProfileURL()).into(profilePictureImageView);
            profilePictureImageView.setClipToOutline(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        UserManager.removeUserObserver(UserManager.getCurrentUserUID(), this);
        if (otherUser != null) {
            UserManager.removeUserObservers(otherUser.getUid());
        }
        Log.d(PROFILE_FRAGMENT_TAG, "PAUSED");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(PROFILE_FRAGMENT_TAG, "STOPPED");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(PROFILE_FRAGMENT_TAG, "DESTROYED VIEW");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(PROFILE_FRAGMENT_TAG, "DESTROYED");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(PROFILE_FRAGMENT_TAG, "DETACHED");
    }
}
