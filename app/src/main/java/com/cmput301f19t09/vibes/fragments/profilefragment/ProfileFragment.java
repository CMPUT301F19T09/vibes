package com.cmput301f19t09.vibes.fragments.profilefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;
import com.cmput301f19t09.vibes.models.User;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView userNameTextView;
    private ImageView profilePictureImageView;
    private Button followButton;

    public static ProfileFragment newInstance(User user, Boolean myProfile, User otherUser) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putBoolean("my_profile", myProfile);
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
        ImageView profileMask = view.findViewById(R.id.profile_mask);
        profileMask.setImageResource(R.drawable.round_mask);

        User user = (User) getArguments().getSerializable("user");
        Boolean mode = getArguments().getBoolean("my_profile");
        User otherUser = (User) getArguments().getSerializable("otherUser");

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "REQUESTED", Toast.LENGTH_LONG).show();
            }
        });

        /**
         * @// TODO: 2019-11-01 Fix bundling issue.
         */
//        FollowingFragment followingFragment = new FollowingFragment();
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.user_mood_list, followingFragment).commit();

        if (user == null || mode == null || otherUser == null) {
            throw new RuntimeException("YOU DUN GOOFED");
        }

        if (mode) {
            followButton.setVisibility(View.INVISIBLE);
            user.readData(new User.FirebaseCallback() {
                @Override
                public void onCallback(User user) {
                    setInfo(user);
                }
            });
        } else {
            followButton.setVisibility(View.VISIBLE);
            otherUser.readData(new User.FirebaseCallback() {
                @Override
                public void onCallback(User user) {
                    setInfo(user);
                }
            });
        }

//        MoodListFragment moodListFragment = MoodListFragment.newInstance(this);
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.user_mood_list, moodListFragment).commit();

        return view;
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
    }
}
