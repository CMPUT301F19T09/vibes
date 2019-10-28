package com.cmput301f19t09.vibes;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private String TAG = "Sample";
    private Uri profileURL;
    private String picturePath;

    public User(String username, String firstName, String lastName, String email) {
        this.userName = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String username) {
        this.userName = username;
        /*
        Initialize with values from Firebase
        Get moods and followers into arrayList
         */
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("users");
        collectionReference.document(this.userName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "Data retrieved");
                        firstName = documentSnapshot.getString("first");
                        lastName = documentSnapshot.getString("last");
                        picturePath = documentSnapshot.getString("profile_picture");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data cannot be retrieved");
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference(picturePath);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileURL = uri;
                        System.out.println(profileURL);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Cannot retrieve URL");
                    }
                });
            }
        }, 3000);
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

    public Uri getProfileURL() {
        return this.profileURL;
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
