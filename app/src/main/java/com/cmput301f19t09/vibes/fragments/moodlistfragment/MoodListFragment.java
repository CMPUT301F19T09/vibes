package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.mooddetailsfragment.MoodDetailsDialogFragment;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

/*
    This class is a Fragment that displays either a users own list of MoodEvents, or the list
    of their followers' most recent MoodEvents
 */
public class MoodListFragment extends Fragment implements MoodFilterListener {
    public static final int OWN_MOODS = 0;
    public static final int FOLLOWED_MOODS = 1;
    public static final int OWN_MOODS_LOCKED = 2;

    MoodListAdapter adapter;
    private int displayType;
    private String selectedFilterEmotion;
    private String filter;
    private User user;
    private MoodListFilterFragment filterFragment;

    /*
    Create a new instance of MoodListFragment, passing the displayType (int) as an argument to the instance
    @param displayType
        the display type of the new Fragment
     */
    public static MoodListFragment newInstance(int displayType)
    {
        MoodListFragment fragment = new MoodListFragment();
        Bundle arguments = new Bundle();

        arguments.putInt("type", displayType);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onStart() {
        displayType = getArguments().getInt("type");

        if (displayType == OWN_MOODS_LOCKED)
        {
            filterFragment.disableRadioButtons();
        }

        MoodListAdapter newAdapter;

        switch (displayType)
        {
            case FOLLOWED_MOODS:    // Show the most recent mood events of users you follow
                newAdapter = new FollowedMoodListAdapter(getContext());
                break;
            case OWN_MOODS:         // Show own moods
            case OWN_MOODS_LOCKED:  // Show own moods and disable viewing other's
            default:
                newAdapter = new OwnMoodListAdapter(getContext());
                break;
        }

        newAdapter.setFilter(filter);
        setAdapter(newAdapter);

        super.onStart();
    }

    @Override
    public void onResume() {
        adapter.initializeData();
        super.onResume();
    }

    /*
    Intializes the View and adapter
    */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Log.d("TEST/MoodListFragment", "onCreateView");
        View view = inflater.inflate(R.layout.mood_list, container, false);

        ListView listView = view.findViewById(R.id.ml_listview);
        FrameLayout filterContainer = view.findViewById(R.id.filter_root);

        user = UserManager.getCurrentUser();

        /*
        When an item in the listview is clicked, open a MoodDetailsDialogFragmnent for the coresponding mood
        If the MoodEvent's User is the current User, enable the editable flag
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MoodEvent event = (MoodEvent) parent.getItemAtPosition(position);
                boolean editable = event.getUser() == user;

                ((MainActivity) MoodListFragment.this.getActivity()).openDialogFragment(MoodDetailsDialogFragment.newInstance(event, editable));
            }
        });

        // Create a filter fragment and add it to the view
        // this allows switching between own moods and others' moods
        if (filterFragment == null) {
            filterFragment = MoodListFilterFragment.newInstance();

            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.filter_root, filterFragment);
            transaction.commit();

            filterFragment.addOnFilterListener(this);
        }

        return view;
    }

    /*
    Set the MoodListAdapter to adapter
    @param adapter
        The MoodListAdapter to use
     */
    private void setAdapter(MoodListAdapter adapter)
    {
        if (this.adapter != null)
        {
            this.adapter.removeObservers();
        }
        this.adapter = adapter;
        ListView listView = getView().findViewById(R.id.ml_listview);
        listView.setAdapter(this.adapter);
        adapter.refreshData();
    }

    /*
    Set the adapter to an OwnMoodListAdapter
     */
    public void showOwnMoods()
    {
        if (displayType != OWN_MOODS)
        {
            Log.d("MoodListFragment", "Setting adapter to OwnMoodAdapter");
            setAdapter(new OwnMoodListAdapter(getContext()));
        }

        displayType = OWN_MOODS;
    }

    /*
    Set the adapter to an OwnMoodListAdapter
     */
    public void showFollowedMoods()
    {
        if (displayType != FOLLOWED_MOODS)
        {
            Log.d("MoodListFragment", "Setting adapter to FollowedMoodAdapter");
            setAdapter(new FollowedMoodListAdapter(getContext()));
        }

        displayType = FOLLOWED_MOODS;
    }

    /*
    On pause, destroy the adapter. This causes it to remove any observers it has on user objects
     */
    @Override
    public void onPause()
    {
        adapter.removeObservers();
        super.onPause();
    }

    /**
     * Setter for selectedFilterEmotion,
     * it sets the current filter
     * to the emotion state string.
     * @param emotion
     */
    public void setFilter(String emotion){
        Log.d("TEST/Filter", "Setting filter to " + emotion);
        this.filter = emotion;
        adapter.setFilter(emotion);
    }
}
