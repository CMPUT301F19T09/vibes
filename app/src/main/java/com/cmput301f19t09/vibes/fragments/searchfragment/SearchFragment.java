package com.cmput301f19t09.vibes.fragments.searchfragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.UserManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    private SearchListAdapter adapter;
    private List<String> userList;
    private CollectionReference collectionReference;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("users");
        userList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        final EditText searchField = view.findViewById(R.id.search_edittext);
        final ListView searchList = view.findViewById(R.id.search_listview);
        ImageButton searchButton = view.findViewById(R.id.search_button);
        searchButton.setImageResource(R.drawable.ic_search_grey_36dp);

        searchButton.setOnClickListener(view1 -> Toast.makeText(getContext(), "SEARCHING", Toast.LENGTH_LONG).show());

        adapter = new SearchListAdapter(getContext());
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener((adapterView, view12, i, l) -> {
            String uid = (String) adapterView.getItemAtPosition(i);
            if (uid.equals(UserManager.getCurrentUserUID())) {
                ((MainActivity) Objects.requireNonNull(getActivity())).setProfileFragment();
            } else {
                ((MainActivity) Objects.requireNonNull(getActivity())).setProfileFragment(uid);
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (searchField.getText().length() > 0) {
                    collectionReference.orderBy("username")
                            .startAt(searchField.getText().toString())
                            .endAt(searchField.getText().toString() + "\uf8ff")
                            .get().addOnCompleteListener(task -> {
                                QuerySnapshot documentSnapshots = task.getResult();

                                if (searchField.getText().length() == 0) {
                                    return;
                                }

                                userList.clear();
                                adapter.clear();
                                assert documentSnapshots != null;
                                for (QueryDocumentSnapshot document : documentSnapshots) {
                                    userList.add(document.getId());
                                }

                                adapter.refreshData(userList);
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchField.getText().length() == 0) {
                    userList.clear();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }
}
