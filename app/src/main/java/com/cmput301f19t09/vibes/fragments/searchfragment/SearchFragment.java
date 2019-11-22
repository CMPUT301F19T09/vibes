package com.cmput301f19t09.vibes.fragments.searchfragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput301f19t09.vibes.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Toast.makeText(getContext(), "SEARCHING...", Toast.LENGTH_LONG).show();
                if (searchField.getText().length() == 0) {
                    userList.clear();
                    adapter.refreshData(userList);
                }
                if (searchField.getText().length() > 0) {
                    Map<String, Object> queryData = new HashMap<>();
                    queryData.put("search", searchField.getText().toString());

                    FirebaseFunctions.getInstance().getHttpsCallable("searchUsers")
                            .call(queryData)
                            .continueWith(new Continuation<HttpsCallableResult, Object>() {
                                @Override
                                public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                    userList = (List<String>) task.getResult().getData();
                                    adapter.refreshData(userList);
                                    return null;
                                }
                            });
                }
            }
        });

        adapter = new SearchListAdapter(getContext());
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return view;
    }
}
