package com.example.vibes;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.Map;

public class MainActivity extends FragmentActivity
{
    //private final static Class defaultFragment = MoodListFragment.class;

    private enum FragmentType
    {
        NONE,
        LIST,
        MAP,
        SEARCH,
        PROFILE;
    }

    private String filterMode;
    private FragmentType currentFragment;

    /*
    Initialize the activity, setting the button listeners and setting the default fragment to a MoodList
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filterMode = null;

        //currentFragment = FragmentType.NONE;
        currentFragment = FragmentType.LIST;

        //setMainFragment(MoodListFragment.class);

        ImageButton addButton, searchButton, filterButton, profileButton, followingButton,
                viewButton;

        addButton = findViewById(R.id.add_button);
        searchButton = findViewById(R.id.search_button);
        filterButton = findViewById(R.id.filter_button);
        profileButton = findViewById(R.id.profile_button);
        followingButton = findViewById(R.id.follow_list_button);
        viewButton = findViewById(R.id.view_button);

        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //setMainFragment(AddFragment.class);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //setMainFragment(SearchFragment.class);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //openDialog(FilterFragment.class);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //setMainFragment(ProfileFragment.class);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //addFragment(FollowingFragment.class);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*
                Set the button to represent which fragment will be opened the NEXT TIME the button
                is pressed (i.e. the current fragment)
                 */
                updateViewButton(currentFragment);

                switch (currentFragment)
                {
                    case NONE:
                    case MAP:
                        //setMainFragment(ListFragment.class);
                        currentFragment = FragmentType.LIST;
                        break;
                    case LIST:
                        //setMainFragment(MapFragment.class);
                        currentFragment = FragmentType.MAP;
                        break;
                }
            }
        });
    }

    /*
    Set the fragment displayed in the main_fragment_root container in the MainActivity layout file
     */
    public void setMainFragment(Class fragmentClass)
    {
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

        // Get the activity fragment manager and begin a new transaction
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // Set the root of the fragment, replacing any fragment that already exists
        transaction.replace(R.id.main_fragment_root, f);
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
    If arguments is null, this does not pass arguments to the dialog
    @param dialogClass
        The class template of the dialog fragment you want to open. Must be a subclass of DialogFragment
    @param arguments
        A map of arguments to provide the new dialogClass with.
        key : argument name
        value : argument value
     */
    public void openDialog(Class dialogClass, Map<String, Serializable> arguments)
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
        if (arguments != null)
        {
            Bundle bundle = new Bundle();
            for (String key : arguments.keySet())
            {
                bundle.putSerializable(key, arguments.get(key));
            }

            dialog.setArguments(bundle);
        }

        // Show the dialog using FragmentTransaction
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        dialog.show(transaction, null);
        transaction.commit();
    }

    /*
    Update the image of the list/map button to reflect the type of fragment it will open if pressed
    @param fragmentType
        The type of fragment that the button will open if pressed
     */
    private void updateViewButton(FragmentType fragmentType)
    {
        ImageButton viewButton = (ImageButton) findViewById(R.id.view_button);
        @DrawableRes int image;

        switch (fragmentType)
        {
            case MAP:
                image = R.drawable.ic_map_white_36dp;
                break;
            case NONE:
            case LIST:
            default:
                image = R.drawable.ic_list_white_36dp;
        }

        viewButton.setImageResource(image);
    }
}

