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
        //User user = new User("testuser2", "Test", "User2", "testuser2@example.com");

//        while ((user.getFirstName() == null) &&
//                (user.getLastName() == null) &&
//                (user.getEmail() == null) &&
//                (user.getProfileURL() == null)) {
//            System.out.println(user.getUserName() + user.getFirstName() + user.getLastName() + user.getEmail() + user.getProfileURL());
//        }

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ProfileFragment profileFragment = new ProfileFragment(user);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.add(R.id.view_group, profileFragment);
//                fragmentTransaction.commit();
//            }
//        }, 3000);

        ProfileFragment profileFragment = new ProfileFragment(user);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.view_group, profileFragment);
        fragmentTransaction.commit();
    }
}
