package com.cmput301f19t09.vibes;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private String TAG = "Sample";

    public User(String username, String firstName, String lastName, String email) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String username) {
        this.userName = userName;
        /*
        Initialize with values from firebase
        Get moods and followers into arrayList
         */
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("users");
        collectionReference.document(this.userName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        setFirstName(documentSnapshot.getString("first"));
                        setLastName(documentSnapshot.getString("last"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data cannot be retrieved");
                    }
                });

    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // getFollowersLatest()

    // getFollowerLastest(String username)

    // addMood(MoodEvent moodEvent)

    // editMood(MoodEvent moodEvent, Integer index)

    // deleteMood(Integer index)
}
