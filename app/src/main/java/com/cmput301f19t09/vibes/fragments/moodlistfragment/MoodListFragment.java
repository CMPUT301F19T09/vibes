package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.fragments.EditFragment;
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

public class MoodListFragment extends Fragment
{
    private ArrayAdapter<MoodEvent> adapter;
    private final MainActivity parent_activity = (MainActivity) getActivity();

    private User user;

    public static MoodListFragment newInstance(User user)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        MoodListFragment moodListFragment = new MoodListFragment();
        moodListFragment.setArguments(bundle);

        return moodListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        user = (User) arguments.getSerializable("user");

        ListView view = parent_activity.findViewById(R.id.ml_listview);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> self, View view, int position, long id)
            {

                MoodEvent event = (MoodEvent) self.getItemAtPosition(position);

                parent_activity.setMainFragment(EditFragment.newInstance(event));
            }
        });

        adapter = new MoodListAdapter(getContext(), user);
    }
}
