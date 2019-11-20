package com.cmput301f19t09.vibes.fragments.searchfragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    private SearchListAdapter adapter;
    private List<User> userList;

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        EditText searchField = view.findViewById(R.id.search_edittext);
        ListView searchList = view.findViewById(R.id.search_listview);
        ImageButton searchButton = view.findViewById(R.id.search_button);
        searchButton.setImageResource(R.drawable.ic_search_grey_36dp);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "SEARCHING", Toast.LENGTH_LONG).show();
            }
        });

        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    private void setAdapter(SearchListAdapter adapter) {
        this.adapter.destroy();
        this.adapter = adapter;
        ListView searchList = getView().findViewById(R.id.search_listview);
        searchList.setAdapter(this.adapter);
        adapter.refreshData();
    }

    public List<User> getUserList() {
        return userList;
    }
}
