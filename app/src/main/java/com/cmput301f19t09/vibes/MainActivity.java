package com.cmput301f19t09.vibes;

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
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.Map;

public class MainActivity extends FragmentActivity
{
    //private final static Class defaultFragment = MoodListFragment.class;

    private enum ButtonMode
    {
        LIST,
        MAP;
    }

    private ButtonMode currentButtonMode;

    /*
    Initialize the activity, setting the button listeners and setting the default fragment to a MoodList
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currentFragment = FragmentType.NONE;
        currentButtonMode = ButtonMode.MAP;
        updateViewButton();

        ImageButton addButton, searchButton, filterButton, profileButton, followingButton,
                viewButton;

        addButton = findViewById(R.id.add_button);
        searchButton = findViewById(R.id.search_button);
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
                //setMainFragment(FollowingFragment.class);
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

                switch (currentButtonMode)
                {
                    case MAP:
                        //setMainFragment(MapFragment.class);
                        currentButtonMode = ButtonMode.LIST;
                        break;
                    case LIST:
                    default:
                        //setMainFragment(ListFragment.class);
                        currentButtonMode = ButtonMode.MAP;
                        break;
                }

                updateViewButton();
            }
        });
    }

    /*
    Set the fragment displayed in the main_fragment_root container in the MainActivity layout file
     */
    public void setMainFragment(Class fragmentClass)
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

        // Get the activity fragment manager and begin a new transaction
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // Set the root of the fragment, replacing any fragment that already exists
        transaction.replace(R.id.main_fragment_root, f);
        transaction.addToBackStack(null);
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
    If arguments is null, this does not pass arguments to the dialog
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
        if (arguments != null)
        {
            dialog.setArguments(arguments);
        }

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
        ImageButton viewButton = (ImageButton) findViewById(R.id.view_button);
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
}

