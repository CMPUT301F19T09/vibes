package com.cmput301f19t09.vibes.models;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class User implements Serializable {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String TAG = "Sample";
    private String picturePath;
    private List<String> followingList;

    // Objects are not serializable - will crash on switching app if not omitted from serialization
    // Ref https://stackoverflow.com/questions/14582440/how-to-exclude-field-from-class-serialization-in-runtime
    private transient FirebaseFirestore db;
    private transient CollectionReference collectionReference ;
    private transient DocumentReference documentReference;
    private transient FirebaseStorage storage;
    private transient StorageReference storageReference;
    private transient Uri profileURL;

    private transient List<Map> moodEvents;
    private static boolean connectionStarted;

    /**
     *
     */
    public interface FirebaseCallback {
        void onCallback(User user);
    }

    /**
     *
     */
    public interface UserExistListener {
        void onUserExists();
        void onUserNotExists();
    }

    /**
     * Constructor for the user class
     */
    public User(){
        if(!connectionStarted){ // Makes sure these definitions are called only once.
            connectionStarted = true;

            db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            db.setFirestoreSettings(settings);

            collectionReference = db.collection("users");
            storage = FirebaseStorage.getInstance();
        }
    }

    /**
     *
     * @param userName
     * @param firstName
     * @param lastName
     * @param email
     */
    public User(String userName, String firstName, String lastName, String email) {
        this();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.picturePath = "image/" + this.userName + ".png";

//
//        exists(new UserExistListener() {
//            @Override
//            public void onUserExists() {
//                readData(new FirebaseCallback() {
//                    @Override
//                    public void onCallback(User user) {
//                        Log.d(TAG, "User information retrieved successfully");
//                    }
//                });
//            }
//
//            @Override
//            public void onUserNotExists() {
//                Map<String, String> data = new HashMap<>();
//                data.put("first", firstName);
//                data.put("last", lastName);
//                data.put("email", email);
//                data.put("profile_picture", picturePath);
//
//                collectionReference.document(userName).set(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Uri imageUri = Uri.parse("android.resource://com.cmput301f19t09.vibes/" + R.drawable.default_profile_picture);
//                                storageReference = storage.getReference(picturePath);
//                                storageReference.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.d(TAG, "Failed to store default profile picture");
//                                    }
//                                });
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "Data failed to store in Firestore");
//                            }
//                        });
//            }
//        });
    }

    /**
     *
     * @param userName
     */
    public User(String userName) {
        this();
        this.userName = userName;
//        readData(new FirebaseCallback() {
//            @Override
//            public void onCallback(User user) {
//                Log.d(TAG, "User information retrieved successfully");
//
//            }
//        });

//        documentReference = collectionReference.document(userName);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                firstName = documentSnapshot.getString("first");
//                lastName = documentSnapshot.getString("last");
//                email = documentSnapshot.getString("email");
//                picturePath = documentSnapshot.getString("profile_picture");
//                followingList = (List<String>) documentSnapshot.get("following_list");
//                moodEvents = (List<Map>) documentSnapshot.get("moods");
//
//                Log.d(TAG, "Finished loading");
//            }
//        });
    }

    /**
     *
     * @param firebaseCallback
     */
    public void readData(FirebaseCallback firebaseCallback) {
        // Using SnapshotListener helps reduce load times and obtains from local cache
        // Ref https://firebase.google.com/docs/firestore/query-data/listen
        documentReference = collectionReference.document(userName);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                firstName = documentSnapshot.getString("first");
                lastName = documentSnapshot.getString("last");
                email = documentSnapshot.getString("email");
                picturePath = documentSnapshot.getString("profile_picture");
                followingList = (List<String>) documentSnapshot.get("following_list");
                moodEvents = (List<Map>) documentSnapshot.get("moods");

                storageReference = storage.getReference(picturePath);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileURL = uri;
                        Log.d(TAG, "Loaded profile picture URL");
                        firebaseCallback.onCallback(User.this);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Cannot retrieve profile picture download url");
                    }
                });

                Log.d(TAG, "Loaded user information");
                firebaseCallback.onCallback(User.this);
            }
        });
    }

//        collectionReference.document(this.userName).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        firstName = documentSnapshot.getString("first");
//                        lastName = documentSnapshot.getString("last");
//                        email = documentSnapshot.getString("email");
//                        picturePath = documentSnapshot.getString("profile_picture");
//                        followingList = (List<String>) documentSnapshot.get("following_list");
//                        moodEvents = (List<Map>) documentSnapshot.get("moods");
//
//                        Log.d(TAG, "Finished Loading");
//                        firebaseCallback.onCallback(User.this);
//
////                        storageReference = storage.getReference(picturePath);
////                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                            @Override
////                            public void onSuccess(Uri uri) {
////                                Log.d(TAG, "Something");
////                                profileURL = uri;
////                                firebaseCallback.onCallback(User.this);
////                            }
////                        }).addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            public void onFailure(@NonNull Exception e) {
////                                Log.d(TAG, "Cannot retrieve profile picture download url");
////                            }
////                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "User information cannot be retrieved");
//                    }
//                });

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return
     */
    public Uri getProfileURL() {
        return profileURL;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public List<String> getFollowingList() {
        return followingList;
    }

    /**
     *
     * @param userExistListener
     */
    public void exists(UserExistListener userExistListener) {
        collectionReference.document(userName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null && documentSnapshot.exists()) {
                                userExistListener.onUserExists();
                            } else {
                                userExistListener.onUserNotExists();
                            }
                        }
                    }
                });
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a List of Mood objects using the moods Maps above
     * @return
     */
    public List<Mood> getMoods() {
        List<Mood> result = new ArrayList<Mood>();
        if(this.moodEvents != null){
            for(Map mapMood : this.moodEvents){
                Log.d("MAP_MOOD", mapMood.toString());

                String emotion = (String) mapMood.get("emotion");
                String reason =(String) mapMood.get("reason");
                Number social =(Number) mapMood.get("social");
                Long timestamp = (Long) mapMood.get("timestamp");
                String username = (String) mapMood.get("username");
                GeoPoint location = (GeoPoint) mapMood.get("location");

                if(timestamp == null){
                    throw new RuntimeException("[MOOD_ERROR]: Timestamp isn't defined");
                }

                // Getting days and stuff
                Calendar cal = Calendar.getInstance(); // TODO: This part isn't working.
                cal.setTimeInMillis(timestamp); // TODO: The timestamp is something else idk why

                // Creating the Mood
                Mood newMood = new Mood(username, emotion, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), location);

                result.add(newMood);
            }
            return result;
        }else{
            // Need to do a read from db.
            throw new RuntimeException("need to update moods from db");
        }
    }
    // getFollowersLatest()

    // getFollowerLastest(String username)

    // addMood(MoodEvent moodEvent)

    // editMood(MoodEvent moodEvent, Integer index)

    // deleteMood(Integer index)
}
