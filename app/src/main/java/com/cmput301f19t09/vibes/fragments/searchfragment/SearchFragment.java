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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    private SearchListAdapter adapter;
    private List<String> userList;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "SEARCHING", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new SearchListAdapter(getContext());
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

                if (searchField.getText().length() > 0) {
                    collectionReference.orderBy("username")
                            .startAt(searchField.getText().toString())
                            .endAt(searchField.getText().toString() + "\uf8ff")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot documentSnapshots = task.getResult();

                            if (searchField.getText().length() == 0) {
                                return;
                            }
                            userList.clear();
                            adapter.clear();
                            for (QueryDocumentSnapshot document : documentSnapshots) {
                                userList.add(document.getId());
                            }

                            adapter.refreshData(userList);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return view;
    }
}
