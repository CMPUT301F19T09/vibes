package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.EditFragment;
import com.cmput301f19t09.vibes.fragments.mooddetailsfragment.MoodDetailsDialogFragment;
import com.cmput301f19t09.vibes.models.MoodItem;
import com.cmput301f19t09.vibes.models.User;

public class MoodListFragment extends Fragment
{
    public static final int OWN_MOODS = 0;
    public static final int FOLLOWED_MOODS = 1;

    MoodListAdapter adapter;
    private int displayType;
    private User user;

    public static MoodListFragment newInstance(User user, int displayType)
    {
        MoodListFragment fragment = new MoodListFragment();
        Bundle arguments = new Bundle();

        arguments.putSerializable("user", user);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MoodItem item = (MoodItem) parent.getItemAtPosition(position);
                ((MainActivity) getActivity()).openDialogFragment(MoodDetailsDialogFragment.newInstance(item, true));
            }
        });

        Bundle arguments = getArguments();

        displayType = arguments.getInt("type");
        user = (User) arguments.getSerializable("user");

        switch (displayType)
        {
            case OWN_MOODS:
                adapter = new OwnMoodListAdapter(getContext(), user);
                break;
            case FOLLOWED_MOODS:
                adapter = new FollowedMoodListAdapter(getContext(), user);
                break;
        }

        listView.setAdapter(adapter);

        return view;
    }
}
