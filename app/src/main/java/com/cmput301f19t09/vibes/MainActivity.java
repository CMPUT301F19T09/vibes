package com.cmput301f19t09.vibes;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;
import com.cmput301f19t09.vibes.fragments.EditFragment;
import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFragment;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.User;
import com.google.android.gms.maps.model.LatLng;

/**
 * MainActivity is the main activity that shows up in the app right now.
 */
public class MainActivity extends FragmentActivity {

    private enum ButtonMode {LIST, MAP}

    private ButtonMode currentButtonMode;
    private @IdRes int fragment_root;
    private User user;

    /**
     * Initialize the activity, setting the button listeners and setting the default fragment to a MoodList
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentButtonMode = ButtonMode.MAP;

        Intent intent = getIntent();
        String username = (String) intent.getSerializableExtra("username");

        user = new User(username);

        initListeners(); // Defines onClickListeners for the components defined above in the class.

        setMainFragment(MoodListFragment.newInstance(new User("testuser"), MoodListFragment.FOLLOWED_MOODS));
        updateViewButton(); // Updates the view button only.
    }


    /**
     * Puts in listeners
     */
    private void initListeners()
    {

        fragment_root = R.id.main_fragment_root;

        View addButton, searchButton, profileButton, followingButton, viewButton;
        addButton = findViewById(R.id.main_add_button);
        profileButton = findViewById(R.id.main_profile_button);
        followingButton = findViewById(R.id.main_follow_list_button);
        searchButton = findViewById(R.id.main_search_button);
        viewButton = findViewById(R.id.main_view_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setMainFragment(EditFragment.newInstance());
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //setMainFragment(SearchFragment.newInstance());
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ProfileFragment profileFragment = ProfileFragment.newInstance(user, true, new User("testuser2"));
//                replaceFragment(ProfileFragment.class);
                User user = new User("testuser");
                setMainFragment(ProfileFragment.newInstance(user, new User("testuser2")));
//                User user = new User("testuser");
                //setMainFragment(ProfileFragment.newInstance(user, true));

                setMainFragment(ProfileFragment.newInstance(user, true, new User("testuser2")));
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User("testuser");
                setMainFragment(ProfileFragment.newInstance(user, new User("testuser2")));
                //setMainFragment(FollowingFragment.newInstance(user));
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /*
                Set the button to represent which fragment will be opened the NEXT TIME the button
                is pressed (i.e. the current fragment)
                 */

                User user = new User("testuser");

                switch (currentButtonMode)
                {
                    case MAP:
                        setMainFragment(new MapFragment());
                        //setMainFragment(MapFragment.newInstance(user));
                        currentButtonMode = ButtonMode.LIST;
                        break;
                    case LIST:
                    default:
                        setMainFragment(MoodListFragment.newInstance(user, MoodListFragment.FOLLOWED_MOODS));
                        currentButtonMode = ButtonMode.MAP;
                        break;
                }

                updateViewButton();
            }
        });
    }

    public void setMainFragment(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(fragment_root, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    Open a dialog fragment
     */
    public void openDialogFragment(DialogFragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        fragment.show(transaction, null);
    }

    /*
    Update the image of the list/map button to reflect the type of fragment it will open if pressed
    @param fragmentType
        The type of fragment that the button will open if pressed
    */
    private void updateViewButton()
    {
        ImageButton viewButton = findViewById(R.id.main_view_button);
        @DrawableRes int image;

        switch (currentButtonMode)
        {
            case MAP:
                image = R.drawable.ic_list_white_36dp;
                break;
            case LIST:
            default:
                image = R.drawable.ic_map_white_36dp;
        }

        viewButton.setImageResource(image);
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager manager = getSupportFragmentManager();

        Log.d("MAINMAINMAINMAINMAINMAINMAIN", manager.getBackStackEntryCount() + "");

        if (manager.getBackStackEntryCount() > 1)
        {
            manager.popBackStack();
        }
    }
}

