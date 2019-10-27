package com.cmput301f19t09.vibes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private String firstName;
    private String lastName;
    private String userName;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView userNameTextView;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);
        firstNameTextView = view.findViewById(R.id.firstname_textview);
        lastNameTextView = view.findViewById(R.id.lastname_textview);
        userNameTextView = view.findViewById(R.id.username_textview);
        return view;
    }

    public ProfileFragment(User user) {
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        setInfo();
    }

    public void setInfo() {
        firstNameTextView.setText(this.firstName);
        lastNameTextView.setText(this.lastName);
        userNameTextView.setText(this.userName);
    }
}
