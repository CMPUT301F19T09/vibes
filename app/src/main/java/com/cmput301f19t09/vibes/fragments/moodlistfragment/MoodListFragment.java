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

import java.util.Observable;
import java.util.Observer;

public class MoodListFragment extends Fragment implements MoodFilterListener//, Observer
{
    public static final int OWN_MOODS = 0;
    public static final int FOLLOWED_MOODS = 1;
    public static final int OWN_MOODS_LOCKED = 2;

    MoodListAdapter adapter;
    private int displayType;
    private int filter;
    private User user;
    private MoodListFilterFragment filterFragment;

    public static MoodListFragment newInstance(String user_id, int displayType)
    {
        MoodListFragment fragment = new MoodListFragment();
        Bundle arguments = new Bundle();

        arguments.putString("id", user_id);
        arguments.putInt("type", displayType);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.mood_list, container, false);

        ListView listView = view.findViewById(R.id.ml_listview);
        FrameLayout filterContainer = view.findViewById(R.id.filter_root);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //MoodItem item = (MoodItem) parent.getItemAtPosition(position);
                MoodEvent event = (MoodEvent) parent.getItemAtPosition(position);
                boolean editable = event.getUser() == user;

                ((MainActivity) getActivity()).openDialogFragment(MoodDetailsDialogFragment.newInstance(event, user, editable));
            }
        });

        Bundle arguments = getArguments();

        displayType = arguments.getInt("type");
        user = UserManager.getUser(arguments.getString("id"));

        switch (displayType)
        {
            case OWN_MOODS:
            case OWN_MOODS_LOCKED:
                adapter = new OwnMoodListAdapter(getContext(), user);
                break;
            case FOLLOWED_MOODS:
                adapter = new FollowedMoodListAdapter(getContext(), user);
                break;
        }

        listView.setAdapter(adapter);
        user.addObserver(adapter);

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

    @Override
    public void onDestroy()
    {
        adapter.destroy();
        super.onDestroy();
    }

    private void setAdapter(MoodListAdapter adapter)
    {
        adapter.destroy();
        this.adapter = adapter;
        ListView listView = getView().findViewById(R.id.ml_listview);
        listView.setAdapter(this.adapter);
        adapter.refreshData();
    }

    public void showOwnMoods()
    {
        if (displayType != OWN_MOODS)
        {
            Log.d("MoodListFragment", "Setting adapter to OwnMoodAdapter");
            setAdapter(new OwnMoodListAdapter(getContext(), user));
        }

        displayType = OWN_MOODS;
    }

    public void showFollowedMoods()
    {
        if (displayType != FOLLOWED_MOODS)
        {
            Log.d("MoodListFragment", "Setting adapter to FollowedMoodAdapter");
            setAdapter(new FollowedMoodListAdapter(getContext(), user));
        }

        displayType = FOLLOWED_MOODS;
    }

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
