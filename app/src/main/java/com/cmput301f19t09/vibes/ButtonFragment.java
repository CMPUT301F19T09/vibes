package com.cmput301f19t09.vibes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ButtonFragment extends Fragment {
    private Button test1Button;
    private Button test2Button;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buttons, container, false);
        test1Button = view.findViewById(R.id.test1);
        test2Button = view.findViewById(R.id.test2);

        test1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.view_group, new ProfileFragment(user)).addToBackStack("button_fragment");
                fragmentTransaction.commit();
            }
        });

        test2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.view_group, new ProfileFragment(new User("testuser2")));
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public ButtonFragment(User user) {
        this.user = user;
    }
}
