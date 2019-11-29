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

/**
 * Fragment to implement searching capabilities. Searching can only be done by order sensitive,
 * case sensitive, and username. Search automatically pulls from the database on text changes to
 * provide quick searches
 */
public class SearchFragment extends Fragment {
    private SearchListAdapter adapter;
    private List<String> userList;
    private CollectionReference collectionReference;

    /**
     * Creates a instance of the SearchFragment
     * @return The new SearchFragment
     */
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    /**
     * Connects with the database when created
     * @param savedInstanceState The saved instance of the MainActivity
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("users");
        userList = new ArrayList<>();
    }

    /**
     * Gets views and set adapter when creating the search fragment
     * @param inflater Makes the view of the fragment from the XML layout file
     * @param container Parent container to store the fragment in
     * @param savedInstanceState Saved instance state of the MainActivity
     * @return he created ProfileFragment view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        final EditText searchField = view.findViewById(R.id.search_edittext);
        final ListView searchList = view.findViewById(R.id.search_listview);
        ImageButton searchButton = view.findViewById(R.id.search_button);
        searchButton.setImageResource(R.drawable.ic_search_grey_36dp);

        adapter = new SearchListAdapter(getContext());
        searchList.setAdapter(adapter);

        // Goes to profile of the selected user
        searchList.setOnItemClickListener((adapterView, view12, i, l) -> {
            String uid = (String) adapterView.getItemAtPosition(i);
            if (uid.equals(UserManager.getCurrentUserUID())) {
                ((MainActivity) Objects.requireNonNull(getActivity())).setProfileFragment();
            } else {
                ((MainActivity) Objects.requireNonNull(getActivity())).setProfileFragment(uid);
            }
        });

        // Updates the adapter when text changes to use new results
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Searching by username, ordered, and case-sensitive
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
                // checks if search field is empty before clearing so empty field won't show anything
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
