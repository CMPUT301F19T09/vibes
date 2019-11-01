package com.cmput301f19t09.vibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;
import com.cmput301f19t09.vibes.fragments.followingfragment.MoodData;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.Mood;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static MoodData dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User("testuser");

        dataList = new MoodData();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String mood;
            if (i % 2 == 0) {
                mood = "HAPPY";
            } else {
                mood = "SAD";
            }

            //Ref: https://www.mkyong.com/java/java-generate-random-integers-in-a-range/
            dataList.add(new Mood("John Doe",
                    mood,
                    random.nextInt(2000) + 0,
                    random.nextInt(11) + 1,
                    random.nextInt(27) + 1,
                    random.nextInt(24),
                    random.nextInt(60)));
        }

        //Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        FollowingFragment followingFragment = new FollowingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linear_layout, followingFragment);
        fragmentTransaction.commit();

        // TESTING
        Button test1 = findViewById(R.id.test1);
        Button test2 = findViewById(R.id.test2);

        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putBoolean("my_profile", true);
                bundle.putSerializable("otherUser", new User("testuser2"));

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.linear_layout, profileFragment).addToBackStack("HOME");
                fragmentTransaction.commit();
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putBoolean("my_profile", false);
                bundle.putSerializable("otherUser", new User("testuser2"));

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.linear_layout, profileFragment).addToBackStack("HOME");
                fragmentTransaction.commit();
            }
        });
    }

    public static ArrayList<Mood> getDataList() {
        return dataList;
    }
}