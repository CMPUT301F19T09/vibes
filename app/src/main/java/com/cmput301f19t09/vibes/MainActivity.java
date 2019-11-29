package com.cmput301f19t09.vibes;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;

import java.util.List;

import com.cmput301f19t09.vibes.fragments.editfragment.EditFragment;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFragment;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.fragments.searchfragment.SearchFragment;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This Activity hosts the different Fragments of the app. Fragments are held in the fragment_root View.
 * Fragments are reused.
 */
public class MainActivity extends FragmentActivity {

    private enum ButtonMode {LIST, MAP}

    // Whether the 'view' button (bottom left in the layout) opens the Map or List of Moods
    private ButtonMode currentButtonMode;
    AlertDialog alertDialog = null;

    /**
     * Create the activity, including adding button listeners to switch between Fragments
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Defines onClickListeners for the components defined above in the class.
        initialize();

        /**
         * Add a listener to the FragmentManager, when the current fragment is changed set the 'view'
         * button to show: MAP if on MoodListFragment and LIST if on any other fragment
         */
        final FragmentManager manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener()
        {
            @Override
            public void onBackStackChanged()
            {
                List<Fragment> fragments = manager.getFragments();
                Fragment currentFragment = fragments.get(fragments.size() - 1);
                String tag = currentFragment.getTag();

                //Log.d("MAIN-ACTIVITY", "Backstack changed -->" + tag);

                if (tag == null || !tag.equals(MapFragment.class.getSimpleName()))
                {
                    //Log.d("MAIN-ACTIVITY", "Switching to list");
                    if (tag != null && tag.equals(ProfileFragment.class.getSimpleName() + UserManager.getCurrentUserUID()))
                    {
                        findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        findViewById(R.id.logoutButton).setVisibility(View.GONE);
                    }

                    currentButtonMode = ButtonMode.MAP;
                }
                else
                {
                    //Log.d("MAIN-ACTIVITY", "Switching to map");
                    currentButtonMode = ButtonMode.LIST;
                }

                updateViewButton();
            }
        });
    }

    /**
     * Initialise the buttons in the layout
     */
    private void initialize()
    {
        currentButtonMode = ButtonMode.MAP;

        // The root View to add fragments to
        @IdRes int fragment_root = R.id.main_fragment_root;
        User user = UserManager.getCurrentUser();

        View addButton, searchButton, followingButton, viewButton, logoutButton;
        ImageView profileButton;

        addButton = findViewById(R.id.main_add_button);
        profileButton = findViewById(R.id.main_profile_button);
        followingButton = findViewById(R.id.main_follow_list_button);
        logoutButton = findViewById(R.id.logoutButton);
        searchButton = findViewById(R.id.main_search_button);
        viewButton = findViewById(R.id.main_view_button);

        logoutButton.setVisibility(View.GONE);

        Glide.with(this).load(user.getProfileURL()).into(profileButton);
        profileButton.setClipToOutline(true);

        // When the addButton is clicked, open an EditFragment
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditFragment();
            }
        });

        // When the searchButton is clicked, open a SearchFragment
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchFragment();
            }
        });

        // When the profileButton is clicked, open a ProfileFragment of the main User
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfileFragment();
            }
        });

        // When the logount button is pressed, ask to log the user out
        logoutButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Open a dialog asking the user whether they want to log out. On positive, log the user out,
             * on negative dismiss the dialog and keep the user logged in.
             * @param view
             */
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Logout");
                dialog.setMessage("Are you sure you want to logout?");
                dialog.setCancelable(true);
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserManager.unregisterAllUsers();       // Unregister all users that have snapshot listeners
                        FirebaseAuth.getInstance().signOut();   // Sign out via Firebase
                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                        finish();                               // End the MainActivity and go back to login
                    }
                });

                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        // When the followingButton is clicked, open the FollowingFragment
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setMainFragment(FollowingFragment.newInstance(UserManager.getCurrentUser()));
                setFollowingFragment();
            }
        });

        // When the 'view' button is clicked open either the MapFragment or the MoodListFragment depending on buttonMode
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentButtonMode) {
                    case LIST:
                        setListFragment();
                        currentButtonMode = ButtonMode.MAP;
                        break;
                    default:
                        setMapFragment();
                        currentButtonMode = ButtonMode.LIST;
                        break;
                }
            }
        });

        Glide.with(MainActivity.this).load(user.getProfileURL()).into(profileButton);

        setListFragment();
        updateViewButton();
    }


    /**
     * Performs a replace operation to show the given Fragment on the screen. If the Fragment that is
     * currently shown has the same tag as the provided tag, then it does not perform the replacement
     * @param fragment The fragment to show in the activity
     * @param tag The tag to associate with that fragment
     */
    private void setMainFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();

        int entryCount = manager.getBackStackEntryCount();

        // Check to see if the given Fragment is already shown (by tag), if it is don't replace it
        if (entryCount != 0 &&
                tag != null &&
                tag.equals(manager.getBackStackEntryAt(entryCount - 1).getName()))
        {
            return;
        }

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment_root, fragment, tag);    // Replace the currently shown fragment
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * Finds a fragment on the backstack with a given tag
     * @param tag The tag to search for
     * @return The Fragment matching tag, or null if there is none
     */
    private Fragment getFragmentInstance(String tag)
    {
        FragmentManager manager = getSupportFragmentManager();
        Fragment instance = manager.findFragmentByTag(tag);

        return instance;
    }

    /**
     * Set the currently shown Fragment to be MoodListFragment showing the users own moods, this is
     * the default behaviour of MoodListFragment
     */
    public void setListFragment()
    {
        // Use the class name as the tag
        String tag = MoodListFragment.class.getSimpleName();
        Fragment instance = getFragmentInstance(tag);

        // If the Fragment hasn't previously been created then create a new one
        if (instance == null)
        {
            instance = MoodListFragment.newInstance(MoodListFragment.OWN_MOODS);
        }

        setMainFragment(instance, tag);
    }

    /**
     * Set the currently shown Fragment to be MapFragment
     */
    public void setMapFragment()
    {
        // Use the class name as the tag
        String tag = MapFragment.class.getSimpleName();
        Fragment instance = getFragmentInstance(tag);

        // If the Fragment hasn't previously been created then create a new one
        if (instance == null)
        {
            instance = MapFragment.newInstance(getApplicationContext());
        }

        setMainFragment(instance, tag);
    }

    /**
     * Set the currently shown fragment to be a ProfileFragment displaying the signed-in User's own
     * profile
     */
    public void setProfileFragment()
    {
        setProfileFragment(null);
    }

    /**
     * Set the currently shown fragment to be a ProfileFragment
     * @param uid The UID of the User whose profile you want to show. If uid is null, then it displays
     *            the signed-in User's own profile
     */
    public void setProfileFragment(String uid)
    {
        // Tag begins with the class name
        String tag = ProfileFragment.class.getSimpleName();

        // Append the UID to the tag. This way a single instance of ProfileFragment will be created for
        // each UID, but the instance can still be reused
        if (uid != null)
        {
            tag += uid;
        }
        else
        {
            tag += UserManager.getCurrentUserUID();
        }

        // Check if an instance of ProfileFragment already exists for the given User
        Fragment instance = getFragmentInstance(tag);

        // If a ProfileFragment associated with tag has not already been created then create a new one
        if (instance == null)
        {
            if (uid == null)
            {
                // For the signed-in User, create a ProfileFragment for own profile
                instance = ProfileFragment.newInstance();
            }
            else
            {
                // For a different User, create a ProfileFragment for that user
                instance = ProfileFragment.newInstance(uid);
            }
        }

        setMainFragment(instance, tag);
    }

    /**
     * Open a new EditFragment in add-mood mode
     */
    public void setEditFragment()
    {
        setEditFragment(null, -1);
    }


    /**
     * Open a new EditFragment
     * @param event The event to edit or null. If null, then it will open the fragment in add-mode
     * @param index The index that the event has in its parent User object
     */
    public void setEditFragment(MoodEvent event, int index)
    {
        Fragment instance = null;

        if (event == null) {
            // Add Fragment
            instance = EditFragment.newInstance();
        } else {
            // Edit Fragment
            instance = EditFragment.newInstance(event, index);
        }

        // Use a null tag so that EditFragment cannot be returned to when pressing the back button
        setMainFragment(instance, null);
    }

    /**
     * Set the current fragment to a FollowingFragment
     */
    public void setFollowingFragment()
    {
        // Use class name as the tag
        String tag = FollowingFragment.class.getSimpleName();
        Fragment instance = getFragmentInstance(tag);

        if (instance == null)
        {
            instance = FollowingFragment.newInstance();
        }

        setMainFragment(instance, tag);
    }

    /**
     * Set the current fragment to a SearchFragment
     */
    public void setSearchFragment()
    {
        // Use class name as tag
        String tag = SearchFragment.class.getSimpleName();
        Fragment instance = getFragmentInstance(tag);

        if (instance == null)
        {
            instance = SearchFragment.newInstance();
        }

        setMainFragment(instance, tag);
    }

    /**
     * Open a DialogFragment in MainActivty
     * @param fragment The DialogFragment to open
     */
    public void openDialogFragment(DialogFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.disallowAddToBackStack();
        fragment.show(transaction, null);
    }

    /**
     * Set the image of the viewButton to correspond to the current mode
     */
    private void updateViewButton() {
        ImageButton viewButton = findViewById(R.id.main_view_button);
        @DrawableRes int image;

        switch (currentButtonMode) {

            case MAP:
                image = R.drawable.ic_map_white_36dp;
                break;
            case LIST:
            default:
                image = R.drawable.ic_list_white_36dp;
                break;
        }

        viewButton.setImageResource(image);
    }

    /**
     * When the back-button is pressed, pop the FragmentManager's backstack to show the previous
     * Fragment. If the previous Fragment's tag is null, continue popping the backstack until it isn't.
     *
     * Doesn't pop the backstack if there is only one Fragment (so that there is always one Fragment shown
     * in MainActivity. This should be whichever Fragment was first added to the activity
     */
    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 1)
        {
            manager.popBackStackImmediate();
        }

        while (manager.getBackStackEntryCount() > 1 &&
                manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName() == null)
        {
            manager.popBackStackImmediate();
        }
    }

    /**
     * Gives access to the most recently set alert dialog. Can be used to access the
     * logout dialog in tests and programmatically.
     *
     * @return
     *      The most recent alert dialog
     */
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}

