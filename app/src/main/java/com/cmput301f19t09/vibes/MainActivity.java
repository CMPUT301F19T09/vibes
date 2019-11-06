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
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cmput301f19t09.vibes.fragments.mapfragment.MapData;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFilter;
import java.util.List;
import com.cmput301f19t09.vibes.fragments.EditFragment;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFragment;
import com.cmput301f19t09.vibes.fragments.mapfragment.UserPoint;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.Mood;
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

    private MapFragment.Filter mapFilter = MapFragment.Filter.SHOW_MINE; // The filter of the map.

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

        user = new User("testuser");
//        user = new User("testuser4", "Test", "User4", "testuser4@example.com");

        initListeners(); // Defines onClickListeners for the components defined above in the class.

        setMainFragment(MoodListFragment.newInstance(user, MoodListFragment.OWN_MOODS));
        updateViewButton(); // Updates the view button only.

        FragmentManager manager = getSupportFragmentManager();

        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener()
        {
            @Override
            public void onBackStackChanged()
            {
                List<Fragment> fragments = manager.getFragments();
                Fragment currentFragment = fragments.get(fragments.size() - 1);

                if (currentFragment == null || currentFragment.getClass().equals(MoodListFragment.class))
                {
                    currentButtonMode = ButtonMode.MAP;
                }
                else
                {
                    currentButtonMode = ButtonMode.LIST;
                }

                updateViewButton();
            }
        });
    }

    /**
     * Stacks two fragments on top of each other. It is different than replaceFragment().
     * You have to define the fragments, combine the fragments with
     * their bundles then send it into this function.
     * You can later access the fragments using the fragmentTitles you have used.
     * @param fragment1
     * @param fragmentTitle
     * @param fragment2
     * @param fragmentTitle2
     */
    public void stackFragment(Fragment fragment1, String fragmentTitle, Fragment fragment2, String fragmentTitle2){
        ViewGroup root = findViewById(R.id.main_fragment_root);
        root.removeAllViewsInLayout();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.main_fragment_root, fragment1, fragmentTitle);
        transaction.add(R.id.main_fragment_root, fragment2, fragmentTitle2);
        transaction.addToBackStack(null);
        transaction.commit();
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
                setMainFragment(ProfileFragment.newInstance(user));
//                User user = new User("testuser");
                //setMainFragment(ProfileFragment.newInstance(user, true));

//                setMainFragment(ProfileFragment.newInstance(user, new User("testuser2")));
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

                switch (currentButtonMode)
                {
                    case LIST:
                        setMainFragment(MoodListFragment.newInstance(user, MoodListFragment.OWN_MOODS));
                        currentButtonMode = ButtonMode.MAP;
                        break;
                    case MAP:
                    default:
                        showMap();
                        currentButtonMode = ButtonMode.LIST;
                        break;
                }
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
            case LIST:
                image = R.drawable.ic_list_white_36dp;
                break;
            case MAP:
            default:
                image = R.drawable.ic_map_white_36dp;
        }

        viewButton.setImageResource(image);
    }

    /**
     * Updates only the map portion in the main root fragment.
     * This is called after having a change in the mapFilter fragment.
     */
    public void updateMap(){
        this.showMap();
    }

    /**
     * Shows the map fragment in the main fragment container.
     */
    public void showMap(){
        // Test user for now. This will be updated
        User user = new User("testuser");

        if(this.mapFilter == MapFragment.Filter.SHOW_MINE){
            user.readData(new User.FirebaseCallback() {
                @Override
                public void onCallback(User user) {
                    List<Mood> moodsShowing = user.getMoods();
                    MapData mapData = new MapData();
                    for(Mood mood: moodsShowing){
                        UserPoint userpoint = new UserPoint(mood.getName(), new LatLng(mood.getLocation().getLatitude(), mood.getLocation().getLongitude()), mood.getStringEmotion(),mood.getReason());
                        mapData.add(userpoint);
                    }

                    Bundle mapBundle = new Bundle();
                    mapBundle.putSerializable("MapData", mapData);
                    Fragment mapFragment = new MapFragment();
                    mapFragment.setArguments(mapBundle);

                    Fragment filterFragment = new MapFilter();

                    stackFragment(filterFragment, "filterFragment", mapFragment, "mapFragment");
                }
            });
        }else if(mapFilter == MapFragment.Filter.SHOW_EVERYONE){
            Log.d("D", "Everyone isn't developed yet. So, it won't go to that screen rn.");
            // Getting everyone's last moods.
//            user.readData(new User.FirebaseCallback() {
//                @Override
//                public void onCallback(User user) {
//                    List<String> followingPeople = user.getFollowingList();
//                    MapData mapData = new MapData();
//
//                    for(String person : followingPeople){
//                        Log.d("D", "Getting Last mood of " + person);
//                        User newUser = new User(person);
//                        newUser.readData(new User.FirebaseCallback() {
//                            @Override
//                            public void onCallback(User user) {
//                                Mood mood = user.getMostRecentMood();
//                                Log.d("D", "Found: " + mood.toString());
//                                UserPoint userpoint = new UserPoint(mood.getName(), new LatLng(mood.getLocation().getLatitude(), mood.getLocation().getLongitude()), mood.getStringEmotion(),mood.getReason());
//                                mapData.add(userpoint);
//
//                            }
//                        });
//                    }
//                    Bundle mapBundle = new Bundle();
//                    mapBundle.putSerializable("MapData", mapData);
//                    Fragment mapFragment = new MapFragment();
//                    mapFragment.setArguments(mapBundle);
//
//                    Fragment filterFragment = new MapFilter();
//
//                    stackFragment(filterFragment, "filterFragment", mapFragment, "mapFragment");
//                }
//            });

        }else{
            throw new RuntimeException("Given map filter isn't known");
        }

    }

    /**
     * Switches the viewing in the map
     * @param filter
     */
    public void switchMapFilter(MapFragment.Filter filter){
        Log.d("DEBUG", "switched");
        this.mapFilter = filter;
        if(this.mapFilter == MapFragment.Filter.SHOW_EVERYONE){
            Log.d("D", "Showing everyone on the map");
        }else if(this.mapFilter == MapFragment.Filter.SHOW_MINE){
            Log.d("D", "Showing mine on the map");
        }

        updateMap();
    }

    /**
     * Returns the filter we have for the map. It is used in filter.
     * @return
     */
    public MapFragment.Filter getMapFilter(){
        return this.mapFilter;
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 1)
        {
            manager.popBackStack();
        }
    }
}

