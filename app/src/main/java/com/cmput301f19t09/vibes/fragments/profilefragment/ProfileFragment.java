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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        User user = (User) getArguments().getSerializable("user");
        if (user == null) {
            throw new RuntimeException("YOU DUN GOOFED");

        }
        System.out.println(user.getEmail());

        FollowingFragment followingFragment = new FollowingFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.user_mood_list, followingFragment).commit();

        firstNameTextView = view.findViewById(R.id.firstname_textview);
        lastNameTextView = view.findViewById(R.id.lastname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        profilePictureImageView = view.findViewById(R.id.profile_picture);
        followButton = view.findViewById(R.id.follow_button);

        setInfo(user);

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "REQUESTED", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

//    public ProfileFragment(User user) {
//        user.readData(new User.FirebaseCallback() {
//            @Override
//            public void onCallback(User user) {
//                setInfo(user);
//                followButton.setVisibility(View.INVISIBLE);
//            }
//        });
//        setInfo(user);
//        followButton.setVisibility(View.INVISIBLE);
//    }

//    public ProfileFragment(User user, User otherUser) {
//        otherUser.readData(new User.FirebaseCallback() {
//            @Override
//            public void onCallback(User user) {
//                setInfo(otherUser);
//                followButton.setVisibility(View.VISIBLE);
//            }
//        });
//    }

    public void setInfo(User user) {
        firstNameTextView.setText(user.getFirstName());
        lastNameTextView.setText(user.getLastName());
        userNameTextView.setText(user.getUserName());
        Glide.with(this).load(user.getProfileURL()).into(profilePictureImageView);
    }
}
