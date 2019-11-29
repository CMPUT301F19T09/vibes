package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * This Fragment is responsible for displaying a list of MoodEvents. Depending on its mode it will show
 * the User's MoodEvents, or the most recent MoodEvents of the Users that they follow.
 */
public class MoodListFragment extends Fragment implements MoodFilterListener
{

    public static final int OWN_MOODS = 0;
    public static final int FOLLOWED_MOODS = 1;
    public static final int OWN_MOODS_LOCKED = 2;

    /**
     * The adapter for the ListView. This determines which MoodEvents are shown
     */
    MoodListAdapter adapter;

    private int mode;
    private String filter;
    private MoodListFilterFragment filterFragment;

    /**
     * Create a new MoodListFragment with the specified mode
     * @param mode The initial mode to create the Fragment with (one of OWN_MOODS, FOLLOWED_MOODS, or OWN_MOODS_LOCKED)
     * @return An instance of MoodListFragment with the specified mode
     */
    public static MoodListFragment newInstance(int mode)
    {
        MoodListFragment fragment = new MoodListFragment();
        Bundle arguments = new Bundle();

        arguments.putInt("type", mode);
        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     * Unpack the arguments only on creation
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        mode = getArguments().getInt("type");
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the Fragment view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return The view object created from the Fragment layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.mood_list, container, false);
        ListView listView = view.findViewById(R.id.ml_listview);

        User user = UserManager.getCurrentUser();

        // When an item in the list is clicked, call the parent activity to open a dialog displaying the details of the MoodEvent
        listView.setOnItemClickListener((AdapterView<?> parent, View v, int position, long id) ->
        {
            MoodEvent event = (MoodEvent) parent.getItemAtPosition(position);
            boolean editable = event.getUser().getUid().equals(user.getUid());  // MoodEvents are editable if they belong to the primary User


            // Call the main activity to open a MoodDetailsDialog for the clicked MoodEvent
            ((MainActivity) MoodListFragment.this.getActivity()).openDialogFragment(MoodDetailsDialogFragment.newInstance(event, editable));
        });

        return view;
    }

    /**
     * When the View is created ensures that the correct adapter is enabled
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        switch (mode)
        {
            case FOLLOWED_MOODS:
                showFollowedMoods();
                break;
            case OWN_MOODS:
            case OWN_MOODS_LOCKED:
            default:
                showOwnMoods();
                break;
        }

        // Create a filter fragment and add it to the view
        // this allows switching between own moods and others' moods
        if (filterFragment == null)
        {
            filterFragment = MoodListFilterFragment.newInstance();

            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.filter_root, filterFragment);
            transaction.commit();

            filterFragment.addOnFilterListener(this);
            // If locked, then disable the radio buttons that allow switching between own/followed moods
            if (mode == OWN_MOODS_LOCKED)
            {
                filterFragment.disableRadioButtons();
            }
        }

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Swaps the current MoodListAdapter with a new one. Applies the selected filter to the adapter
     * @param adapter
     */
    private void setAdapter(MoodListAdapter adapter)
    {
        // 'Pause' the current adapter. This has the effect of removing any adapters that the adapter might have for Users
        if (this.adapter != null)
        {
            this.adapter.pause();
        }

        this.adapter = adapter;

        adapter.setFilter(filter);

        ListView listView = getView().findViewById(R.id.ml_listview);
        listView.setAdapter(this.adapter);
    }

    /**
     * Set the adapter to an OwnMoodListAdapter
     */
    public void showOwnMoods()
    {
        setAdapter(new OwnMoodListAdapter(getContext()));
    }

    /**
     * Set the adapter to an OwnMoodListAdapter
     */
    public void showFollowedMoods()
    {
        setAdapter(new FollowedMoodListAdapter(getContext()));
    }

    /**
     * When the Fragment is paused, 'Pause' the adapter.
     */
    @Override
    public void onPause()
    {
        adapter.pause();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        adapter.resume();
        super.onResume();
    }

    /**
     * Set the filter to the EmotionalState key emotion. Filter works so that the events shown = {MoodEvents where EmotionalState key == filter}
     * @param emotion The key to filter for
     */
    public void setFilter(String emotion)
    {
        this.filter = emotion;
        adapter.setFilter(emotion);
    }
}
