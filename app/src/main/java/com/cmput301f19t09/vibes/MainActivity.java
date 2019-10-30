package com.cmput301f19t09.vibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User("testuser");
//        User user = new User("testuser2", "Test", "User2", "testuser2@example.com");

//        ProfileFragment profileFragment = new ProfileFragment(user);
        ButtonFragment buttonFragment = new ButtonFragment(user);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.view_group, buttonFragment);
//        fragmentTransaction.add(R.id.view_group, profileFragment);
        fragmentTransaction.commit();
    }
}
