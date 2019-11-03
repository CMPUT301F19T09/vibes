package com.cmput301f19t09.vibes;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cmput301f19t09.vibes.fragments.mapfragment.MapData;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFilter;
import com.cmput301f19t09.vibes.fragments.mapfragment.MapFragment;
import com.cmput301f19t09.vibes.fragments.mapfragment.UserPoint;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.fragments.followingfragment.FollowingFragment;
import com.cmput301f19t09.vibes.fragments.followingfragment.MoodData;
import com.cmput301f19t09.vibes.models.Mood;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
/**
 * MainActivity is the main activity that shows up in the app right now.
 */
public class MainActivity extends FragmentActivity {

    private enum ButtonMode {LIST, MAP}

    private ButtonMode currentButtonMode;
    private String username;
    ImageButton addButton, searchButton, filterButton, profileButton, followingButton,
            viewButton;
    private boolean startedDefault = false;
    private MapFragment.Filter mapFilter = MapFragment.Filter.SHOW_EVERYONE;

    /**
    Initialize the activity, setting the button listeners and setting the default fragment to a MoodList
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currentFragment = FragmentType.NONE;
        currentButtonMode = ButtonMode.MAP;

        initComponents(); // uses findViewById to set the components above in the class.
        initListeners(); // Defines onClickListeners for the components defined above in the class.

        updateViewButton(); // Updates the view button only.
        updateScreen(); // Updates main fragment depending on what it is set to
    }

    /**
     * Shows the map fragment in the main fragment container.
     */
    public void showMap(){
        // Getting only the user moods
        User user = new User("testuser");
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

                ViewGroup root = findViewById(R.id.main_fragment_root);
                root.removeAllViewsInLayout();

                // Get the activity fragment manager and begin a new transaction
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                // Set the root of the fragment, replacing any fragment that already exists
                transaction.add(R.id.main_fragment_root,filterFragment, "filterFragment");
                transaction.add(R.id.main_fragment_root, mapFragment, "mapFragment");
                if(!startedDefault){
                    transaction.addToBackStack(null);
                    startedDefault = true;
                }
                transaction.commit();
            }
        });
    }

    /**
     * Shows the list fragment in the main fragment contianer.
     */
    public void showList(){
        MoodData dataList = new MoodData();
        dataList.add(new Mood("Joe", "HAPPY", 2000, 12, 12, 0, 0));
        //Ref: https://www.tutorialspoint.com/fragment-tutorial-with-example-in-android-studio
        Bundle bundle = new Bundle();
        bundle.putSerializable("MoodData", dataList);
        replaceFragment(FollowingFragment.class, bundle);
    }

    /**
     * Updates the screen with the selected view mode = MAP | LIST
     */
    public void updateScreen(){
        if(currentButtonMode == ButtonMode.MAP){ // Show the map
            showMap();
        }else if(currentButtonMode == ButtonMode.LIST){// show the list fragment.
            showList();
        }
    }

    /**
     * Inititates the components defined above in the class using findViewById
     */
    private void initComponents(){
        addButton = findViewById(R.id.add_button);
        searchButton = findViewById(R.id.search_button);
        profileButton = findViewById(R.id.profile_button);
        followingButton = findViewById(R.id.follow_list_button);
        viewButton = findViewById(R.id.view_button);
    }

    /**
     * Puts in listeners
     */
    private void initListeners(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //replaceFragment(AddFragment.class);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //replaceFragment(SearchFragment.class);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User("testuser");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putBoolean("my_profile", true);
                bundle.putSerializable("otherUser", new User("testuser2"));
                replaceFragment(ProfileFragment.class, bundle);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User("testuser");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putBoolean("my_profile", false);
                bundle.putSerializable("otherUser", new User("testuser2"));
                ProfileFragment profileFragment = new ProfileFragment();
                replaceFragment(ProfileFragment.class, bundle);
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
                    case MAP:
                        //replaceFragment(MapFragment.class);
                        currentButtonMode = ButtonMode.LIST;
                        break;
                    case LIST:
                    default:
                        //replaceFragment(ListFragment.class);
                        currentButtonMode = ButtonMode.MAP;
                        break;
                }

                updateViewButton();
                updateScreen();
            }
        });
    }

    public void replaceFragment(Class fragmentClass)
    {
        replaceFragment(fragmentClass, null);
    }

    /*
    Set the fragment displayed in the main_fragment_root container in the MainActivity layout file
     */
    public void replaceFragment(Class fragmentClass, Bundle arguments)
    {
        ViewGroup root = findViewById(R.id.main_fragment_root);
        root.removeAllViewsInLayout();
        if (!Fragment.class.isAssignableFrom(fragmentClass))
        {
            throw new IllegalArgumentException("Argument is not subclass of Fragment!");
        }

        Fragment f;

        try
        {
            // instantiate the fragment
            f = (Fragment) fragmentClass.newInstance();
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException();
        }
        catch (InstantiationException e)
        {
            throw new IllegalArgumentException();
        }

        if (arguments == null)
        {
            arguments = new Bundle();
        }

        arguments.putSerializable("username", username);
        f.setArguments(arguments);

        // Get the activity fragment manager and begin a new transaction
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // Set the root of the fragment, replacing any fragment that already exists
        transaction.replace(R.id.main_fragment_root, f);
        if(!startedDefault){
            transaction.addToBackStack(null);
            startedDefault = true;
        }
        transaction.commit();
    }

    public void addFragment(Class fragmentClass)
    {
        if (!Fragment.class.isAssignableFrom(fragmentClass))
        {
            throw new IllegalArgumentException("Argument is not subclass of Fragment!");
        }

        Fragment f;

        try
        {
            f = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException();
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.main_fragment_root, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    Open the DialogFragment specified by dialogClass, with no arguments.
     */
    public void openDialog(Class dialogClass)
    {
        openDialog(dialogClass, null);
    }

    /*
    Open the DialogFragment specified by dialogClass, with the arguments specified by arguments.
    This will always add the key "username", with the value username to the bundle
    @param dialogClass
        The class template of the dialog fragment you want to open. Must be a subclass of DialogFragment
    @param arguments
        A map of arguments to provide the new dialogClass with.
        key : argument name
        value : argument value
     */
    public void openDialog(Class dialogClass, Bundle arguments)
    {
        // If the dialogClass is not a subclass of DialogFragment throw an exception
        if (!DialogFragment.class.isAssignableFrom(dialogClass))
        {
            throw new IllegalArgumentException("Argument is not subclass of DialogFragment!");
        }

        DialogFragment dialog;

        try
        {
            // Instantiate a new class of dialogClass
            dialog = (DialogFragment) dialogClass.newInstance();
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException();
        }
        catch (InstantiationException e)
        {
            throw new IllegalArgumentException();
        }

        // If arguments were provided put them in a bundle and pass them to the new fragment
        if (arguments == null)
        {
            arguments = new Bundle();
        }

        arguments.putSerializable("username", username);
        dialog.setArguments(arguments);

        // Show the dialog using FragmentTransaction
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        dialog.show(transaction, null);
    }

    /*
    Update the image of the list/map button to reflect the type of fragment it will open if pressed
    @param fragmentType
        The type of fragment that the button will open if pressed
     */
    private void updateViewButton()
    {
        ImageButton viewButton = findViewById(R.id.view_button);
        @DrawableRes int image;

        switch (currentButtonMode)
        {
            case MAP:
                image = R.drawable.ic_map_white_36dp;
                break;
            case LIST:
            default:
                image = R.drawable.ic_list_white_36dp;
        }

        viewButton.setImageResource(image);
    }

    /**
     * Updates only the map portion in the main root fragment
     */
    public void updateMap(){

    }

    /**
     * Switches the viewing in the map
     * @param filter
     */
    public void switchMapFilter(MapFragment.Filter filter){
        Log.d("DEBUG", "switched");
        this.mapFilter = filter;
        updateMap();
    }
}

