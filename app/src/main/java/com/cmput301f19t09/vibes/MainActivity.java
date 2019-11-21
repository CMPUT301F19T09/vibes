package com.cmput301f19t09.vibes;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;

import java.util.List;

import com.cmput301f19t09.vibes.fragments.editfragment.EditFragment;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFragment;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.fragments.searchfragment.SearchFragment;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

/**
 * MainActivity is the main activity that shows up in the app right now.
 */
public class MainActivity extends FragmentActivity {

    private enum ButtonMode {LIST, MAP}

    private ButtonMode currentButtonMode;
    private @IdRes
    int fragment_root;
    private User user;

    private MapFragment.Filter mapFilter = MapFragment.Filter.SHOW_MINE; // The filter of the map.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the button in the bottom left to open the map fragment

        user = UserManager.getCurrentUser();

        // Defines onClickListeners for the components defined above in the class.
        intializeViews();

        setMainFragment(MoodListFragment.newInstance(MoodListFragment.OWN_MOODS));

        /*
            Add a backstack listener to change what the "view" (list/map) button displays
            when a new fragment is opened
         */
        final FragmentManager manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                List<Fragment> fragments = manager.getFragments();
                Fragment currentFragment = fragments.get(fragments.size() - 1);

                if (currentFragment == null || currentFragment.getClass().equals(MoodListFragment.class)) {
                    currentButtonMode = ButtonMode.MAP;
                } else {
                    currentButtonMode = ButtonMode.LIST;
                }

                updateViewButton();
            }
        });
    }

    /*
     *  Initializes the views in the main activity layout
     */
    private void intializeViews() {

        fragment_root = R.id.main_fragment_root;
        currentButtonMode = ButtonMode.MAP;

        View addButton, searchButton, profileButton, followingButton, viewButton;
        addButton = findViewById(R.id.main_add_button);
        profileButton = findViewById(R.id.main_profile_button);
        followingButton = findViewById(R.id.main_follow_list_button);
        searchButton = findViewById(R.id.main_search_button);
        viewButton = findViewById(R.id.main_view_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMainFragment(EditFragment.newInstance());
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMainFragment(SearchFragment.newInstance());
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMainFragment(ProfileFragment.newInstance());
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMainFragment(FollowingFragment.newInstance(UserManager.getCurrentUser()));
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Set the button to represent which fragment will be opened the NEXT TIME the button
                is pressed (i.e. the current fragment)
                 */

                switch (currentButtonMode) {
                    case LIST:
                        setMainFragment(MoodListFragment.newInstance(MoodListFragment.OWN_MOODS));
                        currentButtonMode = ButtonMode.MAP;
                        break;
                    default:
                        setMainFragment(MapFragment.newInstance(getApplicationContext()));
                        currentButtonMode = ButtonMode.LIST;
                        break;
                }
            }
        });

        updateViewButton(); // Updates the view button only.
    }


    /*
        Creates a FragmentTransaction which replaces the current fragment with the specified one
        @param fragment
            the fragment to place in the main view
     */
    public void setMainFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        String name = fragment.getClass().getSimpleName();

        /*
            If there already exists an instance of this fragment on the backstack, simply reuse
            it with new arguments
         */
        if (manager.findFragmentByTag(name) != null)
        {
            Fragment oldInstance = manager.findFragmentByTag(name);
            oldInstance.setArguments(fragment.getArguments());
            fragment = oldInstance;
        }

        /*
            If the new fragment i
         */
        if ((manager.getBackStackEntryCount() > 0 &&
                !manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName().equals(name)) ||
            manager.getBackStackEntryCount() == 0)
        {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(fragment_root, fragment, name);
            transaction.addToBackStack(name);
            transaction.commit();
        }
    }

    /*
        Creates a FragmentTransaction to open a DialogFragmnent in the main view
        @param fragment
            the fragment to open
     */
    public void openDialogFragment(DialogFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        fragment.show(transaction, null);
    }

    /*
    Update the image of the list/map button to reflect the type of fragment it will open if pressed
    @param fragmentType
        The type of fragment that the button will open if pressed
    */
    private void updateViewButton() {
        ImageButton viewButton = findViewById(R.id.main_view_button);
        @DrawableRes int image;

        switch (currentButtonMode) {
            case LIST:
                image = R.drawable.ic_list_white_36dp;
                break;
            case MAP:
            default:
                image = R.drawable.ic_map_white_36dp;
        }

        viewButton.setImageResource(image);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 1) {
            manager.popBackStack();
        }
    }

    public void openListFragment()
    {

    }
}

