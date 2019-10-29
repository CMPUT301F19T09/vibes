package com.cmput301f19t09.vibes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MoodListFragment extends Fragment
{
    private ArrayAdapter<MoodEvent> adapter;
    private List<MoodEvent> data;
    private final MainActivity parent = (MainActivity) getActivity();

    private String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        username = (String) arguments.getSerializable("username");

        ListView view = parent.findViewById(R.id.mood_listview);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> self, View view, int position, long id)
            {
                Bundle arguments = new Bundle();

                MoodEvent event = (MoodEvent) self.getItemAtPosition(position);

                arguments.putSerializable("edit", true);
                arguments.putSerializable("mood", event);

                ((MainActivity) parent).replaceFragment(AddFragment.class, arguments);
            }
        });

        data = new ArrayList<MoodEvent>();
        adapter = new MoodListAdapter(getContext(), data);
    }

    private void loadData()
    {

    }
}
