package com.cmput301f19t09.vibes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;
import com.cmput301f19t09.vibes.fragments.followingfragment.MoodData;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.Mood;
import com.cmput301f19t09.vibes.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
//        FollowingFragment followingFragment = new FollowingFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.linear_layout, followingFragment);
//        fragmentTransaction.commit();

//        user.readData(new User.FirebaseCallback() {
//            @Override
//            public void onCallback(User user) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("user", user);
//                bundle.putBoolean("my_profile", true);
//                bundle.putSerializable("otherUser", new User("testuser2"));
//
//                ProfileFragment profileFragment = new ProfileFragment();
//                profileFragment.setArguments(bundle);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.linear_layout, profileFragment);
//                fragmentTransaction.commit();
//            }
//        });

//        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document("testuser");
//
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                String firstName = documentSnapshot.getString("first");
//                String lastName = documentSnapshot.getString("last");
//                String email = documentSnapshot.getString("email");
//                String picturePath = documentSnapshot.getString("profile_picture");
//                List<String> followingList = (List<String>) documentSnapshot.get("following_list");
//                List<Map> moodEvents = (List<Map>) documentSnapshot.get("moods");
//
//                User user = new User("testuser", firstName, lastName, email);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("user", user);
//                bundle.putBoolean("my_profile", true);
//                bundle.putSerializable("otherUser", new User("testuser2"));
//
//                ProfileFragment profileFragment = new ProfileFragment();
//                profileFragment.setArguments(bundle);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.linear_layout, profileFragment);
//                fragmentTransaction.commit();
//            }
//        });

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putBoolean("my_profile", false);
        bundle.putSerializable("otherUser", new User("testuser2"));

        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linear_layout, profileFragment);
        fragmentTransaction.commit();

    }

    public static ArrayList<Mood> getDataList() {
        return dataList;
    }
}