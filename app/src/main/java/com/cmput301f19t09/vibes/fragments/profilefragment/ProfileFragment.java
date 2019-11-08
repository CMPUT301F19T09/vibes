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
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.models.User;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Observable;
import java.util.Observer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment implements Observer {
    public static final String PROFILE_FRAGMENT_TAG = "ProfileFragment";
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView userNameTextView;
    private ImageView profilePictureImageView;
    private Button followButton;
    private ListenerRegistration registration;

    public static ProfileFragment newInstance(User user) {
        ProfileFragment profileFragment = new ProfileFragment();
        user.addObserver(profileFragment);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    public static ProfileFragment newInstance(User user, User otherUser) {
        ProfileFragment profileFragment = new ProfileFragment();
        user.addObserver(profileFragment);
        otherUser.addObserver(profileFragment);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("otherUser", otherUser);
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

        firstNameTextView = view.findViewById(R.id.firstname_textview);
        lastNameTextView = view.findViewById(R.id.lastname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        profilePictureImageView = view.findViewById(R.id.profile_picture);
        followButton = view.findViewById(R.id.follow_button);
//        ImageView profileMask = view.findViewById(R.id.profile_mask);
//        profileMask.setImageResource(R.drawable.round_mask);

        User user = (User) getArguments().getSerializable("user");
        User otherUser = (User) getArguments().getSerializable("otherUser");

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
            if (registration == null) {
                Log.d("CHECK", "SHOULD BE CALLED ONCE");
                registration = user.readData();
            }
            user.addMood();
            MoodListFragment moodListFragment = MoodListFragment.newInstance(user, MoodListFragment.OWN_MOODS_LOCKED);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.user_mood_list, moodListFragment).commit();

        } else {
            followButton.setVisibility(View.VISIBLE);
            otherUser.readData();
            MoodListFragment moodListFragment = MoodListFragment.newInstance(otherUser, MoodListFragment.OWN_MOODS_LOCKED);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.user_mood_list, moodListFragment).commit();
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
        firstNameTextView.setText(user.getFirstName());
        lastNameTextView.setText(user.getLastName());
        userNameTextView.setText(user.getUserName());
        Glide.with(this).load(user.getProfileURL()).into(profilePictureImageView);
        profilePictureImageView.setClipToOutline(true);
    }

    @Override
    public void onPause() {
        super.onPause();
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
        registration.remove();
        Log.d(PROFILE_FRAGMENT_TAG, "DESTROYED");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(PROFILE_FRAGMENT_TAG, "DETACHED");
    }
}
