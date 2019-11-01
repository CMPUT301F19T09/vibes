package com.cmput301f19t09.vibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;
import com.cmput301f19t09.vibes.fragments.followingfragment.MoodData;
import com.cmput301f19t09.vibes.models.Mood;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static MoodData dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //Ref: https://stackoverflow.com/questions/15392261/android-pass-dataextras-to-a-fragment#15392591
        FollowingFragment followingFragment = new FollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MoodData", dataList);
        followingFragment.setArguments(bundle);
        //Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linear_layout, followingFragment);
        fragmentTransaction.commit();

    }
}