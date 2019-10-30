package com.cmput301f19t09.vibes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView userNameTextView;
    private ImageView profilePictureImageView;
    private Button followButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);
        firstNameTextView = view.findViewById(R.id.firstname_textview);
        lastNameTextView = view.findViewById(R.id.lastname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        profilePictureImageView = view.findViewById(R.id.profile_picture);
        followButton = view.findViewById(R.id.follow_button);
        return view;
    }

    public ProfileFragment(User user) {
        System.out.println(user.getUserName() + user.getFirstName() + user.getLastName() + user.getEmail() + user.getProfileURL());
        if (user.getFirstName() != null) {
            setInfo(user);
        } else {
            user.readData(new User.FirebaseCallback() {
                @Override
                public void onCallback(User user) {
                    setInfo(user);
                }
            });
        }
    }

    public void setInfo(User user) {
        firstNameTextView.setText(user.getFirstName());
        lastNameTextView.setText(user.getLastName());
        userNameTextView.setText(user.getUserName());
        Glide.with(this).load(user.getProfileURL()).into(profilePictureImageView);
    }
}
