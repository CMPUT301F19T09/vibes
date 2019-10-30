package com.cmput301f19t09.vibes;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("users");
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private String TAG = "Sample";
    private Uri profileURL;
    private String picturePath;
    private Boolean userNameExists;
    private List<String> followingList;

    public interface FirebaseCallback {
        void onCallback(User user);
    }

    public void readData(FirebaseCallback firebaseCallback) {
        collectionReference.document(this.userName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "User information retrieved");
                        firstName = documentSnapshot.getString("first");
                        lastName = documentSnapshot.getString("last");
                        email = documentSnapshot.getString("email");
                        picturePath = documentSnapshot.getString("profile_picture");
                        followingList = (List<String>) documentSnapshot.get("following_list");
                        System.out.println(followingList);

                        storage = FirebaseStorage.getInstance();
                        storageReference = storage.getReference(picturePath);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "Profile picture download url retrieved");
                                profileURL = uri;
                                firebaseCallback.onCallback(User.this);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Cannot retrieve profile picture download url");
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "User information cannot be retrieved");
                    }
                });
    }

    public User(String userName, String firstName, String lastName, String email) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        checkUserName(userName);
    }

    public User(String userName) {
        this.userName = userName;
        /*
        Initialize with values from Firebase
        Get moods and followers into arrayList
         */
//        collectionReference.document(this.userName).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Log.d(TAG, "User information retrieved");
//                        firstName = documentSnapshot.getString("first");
//                        lastName = documentSnapshot.getString("last");
//                        email = documentSnapshot.getString("email");
//                        picturePath = documentSnapshot.getString("profile_picture");
//                        followingList = (List<String>) documentSnapshot.get("following_list");
//                        System.out.println(followingList);
//
//                        storage = FirebaseStorage.getInstance();
//                        storageReference = storage.getReference(picturePath);
//                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Log.d(TAG, "Profile picture download url retrieved");
//                                profileURL = uri;
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Cannot retrieve profile picture download url");
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "User information cannot be retrieved");
//                    }
//                });

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public Uri getProfileURL() {
        return profileURL;
    }

    public String getEmail() {
        return email;
    }

    public Boolean checkUserName(String userName) {
        collectionReference.document(userName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                userNameExists = true;
                            } else {
                                userNameExists = false;
                            }
                        } else {
                            Log.d(TAG, "Failed to retrieve username");
                        }
                    }
                });

        return userNameExists;
    }

//    Map<String, String> data = new HashMap<>();
//        data.put("first", this.firstName);
//        data.put("last", this.lastName);
//        data.put("email", this.email);
//
//        collectionReference.document(this.userName).set(data)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//        @Override
//        public void onSuccess(Void aVoid) {
//            Log.d(TAG, "Data stored in Firestore successfully");
//        }
//    })
//            .addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception e) {
//            Log.d(TAG, "Data failed to store in Firestore");
//        }
//    });

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // getFollowersLatest()

    // getFollowerLastest(String username)

    // addMood(MoodEvent moodEvent)

    // editMood(MoodEvent moodEvent, Integer index)

    // deleteMood(Integer index)
}
