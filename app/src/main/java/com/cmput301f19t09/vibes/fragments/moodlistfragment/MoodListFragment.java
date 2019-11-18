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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
    This class is a Fragment that displays either a users own list of MoodEvents, or the list
    of their followers' most recent MoodEvents
 */
public class MoodListFragment extends Fragment implements MoodFilterListener
{
    public static final int OWN_MOODS = 0;
    public static final int FOLLOWED_MOODS = 1;
    public static final int OWN_MOODS_LOCKED = 2;

    private static int gid = 0;
    private int id;

    MoodListAdapter adapter;
    private int displayType;
    private int filter;
    private User user;
    private MoodListFilterFragment filterFragment;

    public MoodListFragment()
    {
        this.id = gid++;
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("uid", UserManager.getCurrentUserUID());
        //String[] filters = {"ANTICIPATION"};
        List<String> filters = new ArrayList<>();
        filters.add("ANTICIPATION");
        testMap.put("filters", filters);
        testMap.put("push", true);
        FirebaseFunctions.getInstance().getHttpsCallable("getMostRecentMoodEvent").call(testMap)
                .continueWith(new Continuation<HttpsCallableResult, Object>() {
                    @Override
                    public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        //Log.d("TEST/Firebase", );
                        if (task.getResult().getData() == null) Log.d("TEST/Firebase", "null return");
                        Map<String, Object> result = (Map) task.getResult().getData();
                        Log.d("TEST/Firebase/MostRecent", "username: " + (String) result.get("username"));
                        Log.d("TEST/Firebase/MostRecent", "emotion: " + (String) result.get("emotion"));
                        Log.d("TEST/Firebase/MostRecent", "reason: " + (String) result.get("reason"));
                        //Log.d("TEST/Firebase/MostRecent", "username: " + (String) result.get("username"));
                        //Log.d("TEST/Firebase/MostRecent", "username: " + (String) result.get("username"));
                        //Log.d("TEST/Firebase/MostRecent", "username: " + (String) result.get("username"));
                        //Log.d("TEST/Firebase", task.getResult().toString());
                        return null;
                    }
                });
    }

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

    /*
    Intializes the View and adapter
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
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

        Bundle arguments = getArguments();

        displayType = arguments.getInt("type");
        user = UserManager.getCurrentUser();

        /*
        Set the adapter to the one specified by displayType
         */
        switch (displayType)
        {
            case OWN_MOODS:         // Show own moods
            case OWN_MOODS_LOCKED:  // Show own moods and disable viewing other's
                adapter = new OwnMoodListAdapter(getContext());
                break;
            case FOLLOWED_MOODS:    // Show the most recent mood events of users you follow
                adapter = new FollowedMoodListAdapter(getContext());
                break;
        }

        listView.setAdapter(adapter);

        // Create a filter fragment and add it to the view
        // this allows switching between own moods and others' moods
        filterFragment = MoodListFilterFragment.newInstance();

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.filter_root, filterFragment);
        transaction.commit();

        filterFragment.addOnFilterListener(this);

        if (displayType == OWN_MOODS_LOCKED)
        {
            filterFragment.disableRadioButtons();
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
        this.adapter.destroy();
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
        adapter.destroy();
        super.onPause();
    }

    @Override
    public void onResume() {
        adapter.initializeData();
        super.onResume();
    }

    /*
        Set the filter for mood types
        TODO: Create docs, but this stuff isn't implemented yet
         */
    public void addFilter(int filter)
    {
        this.filter |= filter;
    }

    public void removeFilter(int filter)
    {
        this.filter ^= filter;
    }

    public void clearFilter()
    {
        this.filter &= 0;
    }
}
