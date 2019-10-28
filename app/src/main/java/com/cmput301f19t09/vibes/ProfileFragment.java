package com.cmput301f19t09.vibes;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URI;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private String firstName;
    private String lastName;
    private String userName;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView userNameTextView;
    private ImageView profilePictureImageView;
    private Uri profileURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);
        firstNameTextView = view.findViewById(R.id.firstname_textview);
        lastNameTextView = view.findViewById(R.id.lastname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        profilePictureImageView = view.findViewById(R.id.profile_picture);
        setInfo();
        return view;
    }

    public ProfileFragment(User user) {
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.profileURL = user.getProfileURL();
    }

    public void setInfo() {
        firstNameTextView.setText(this.firstName);
        lastNameTextView.setText(this.lastName);
        userNameTextView.setText(this.userName);
        Glide.with(this).load(profileURL).into(profilePictureImageView);
    }
}
