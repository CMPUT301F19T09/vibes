package com.cmput301f19t09.vibes.models;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
    private List<Mood> result;
    private List<MoodEvent> moodEvents;

    // Objects are not serializable - will crash on switching app if not omitted from serialization
    // Ref https://stackoverflow.com/questions/14582440/how-to-exclude-field-from-class-serialization-in-runtime
    private transient static FirebaseFirestore db;
    private transient static CollectionReference collectionReference ;
    private transient static DocumentReference documentReference;
    private transient static FirebaseStorage storage;
    private transient static StorageReference storageReference;
    private transient Uri profileURL;

    private transient List<Map> moods;
    private static boolean connectionStarted;

    // This is the number of elements in the mood map on firebase.
    // I used it to check if a map is complete to show it on the map.
    private final int MAP_MOOD_SIZE = 7;
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
    }

    /**
     *
     * @param userName
     */
    public User(String userName) {
        this();
        this.userName = userName;

        if(userName == null){
            throw new RuntimeException("[UserClass]: Username isn't defined for readData()");
        }

        // Using SnapshotListener helps reduce load times and obtains from local cache
        // Ref https://firebase.google.com/docs/firestore/query-data/listen
//        documentReference = collectionReference.document(userName);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                firstName = documentSnapshot.getString("first");
//                lastName = documentSnapshot.getString("last");
//                email = documentSnapshot.getString("email");
//                picturePath = documentSnapshot.getString("profile_picture");
//                followingList = (List<String>) documentSnapshot.get("following_list");
//                moods = (List<Map>) documentSnapshot.get("moods");
//
//                List<Map> moods = (List<Map>) documentSnapshot.get("moods");
//
//                moodEvents = parseToMoodEvent();
//
//                storageReference = storage.getReference(picturePath);
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        profileURL = uri;
//                        Log.d(TAG, "Loaded profile picture URL");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Cannot retrieve profile picture download url");
//                    }
//                });
//
//                Log.d(TAG, "Loaded user information");
//            }
//        });
    }

    /**
     *
     * @param firebaseCallback
     */
    public void readData(FirebaseCallback firebaseCallback) {
        if(userName == null){
            throw new RuntimeException("[UserClass]: Username isn't defined for readData()");
        }

        // Using SnapshotListener helps reduce load times and obtains from local cache
        // Ref https://firebase.google.com/docs/firestore/query-data/listen
        documentReference = collectionReference.document(userName);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                firstName = documentSnapshot.getString("first");
//                lastName = documentSnapshot.getString("last");
//                email = documentSnapshot.getString("email");
//                picturePath = documentSnapshot.getString("profile_picture");
//                followingList = (List<String>) documentSnapshot.get("following_list");
//                moods = (List<Map>) documentSnapshot.get("moods");
//
//                List<Map> moods = (List<Map>) documentSnapshot.get("moods");
//
//                moodEvents = parseToMoodEvent();
//
//                storageReference = storage.getReference(picturePath);
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        profileURL = uri;
//                        Log.d(TAG, "Loaded profile picture URL");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Cannot retrieve profile picture download url");
//                    }
//                });
//
//                Log.d(TAG, "Loaded user information");
//                firebaseCallback.onCallback(User.this);
//            }
//        });
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firstName = documentSnapshot.getString("first");
                lastName = documentSnapshot.getString("last");
                email = documentSnapshot.getString("email");
                picturePath = documentSnapshot.getString("profile_picture");
                followingList = (List<String>) documentSnapshot.get("following_list");
                moods = (List<Map>) documentSnapshot.get("moods");

                List<Map> moods = (List<Map>) documentSnapshot.get("moods");

                moodEvents = parseToMoodEvent();

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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("INFO", "Cannot load info");
            }
        });
    }

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
        result = new ArrayList<Mood>();
        if(this.moods != null){
            for(Map mapMood : this.moods){
//                Log.d("MAP_MOOD", mapMood.toString());

                // Getting things out of the mood that is in the Map form.
                String emotion = (String) mapMood.get("emotion");
                String reason =(String) mapMood.get("reason");
                Number social =(Number) mapMood.get("social");
                Long timestamp = (Long) mapMood.get("timestamp");
                String username = (String) mapMood.get("username");
                GeoPoint location = (GeoPoint) mapMood.get("location");

                if(mapMood.size() != MAP_MOOD_SIZE){ // The mood class isn't complete. Then skip it.
                    Log.d("INFO", "Mood isn't complete yet");
                    continue;
                }

                // Checking if timestamp is defined.
                if(timestamp == null){
                    throw new RuntimeException("[MOOD_ERROR]: Timestamp isn't defined");
                }

                // Getting the time elements.
                // However, it doesn't return the correct values when creating the new mood.
                Calendar cal = Calendar.getInstance(); // TODO: This part isn't working.
                cal.setTimeInMillis(timestamp); // TODO: The timestamp is something else idk why

                // Creating the Mood
                Mood newMood = new Mood(username, emotion, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), location);

                result.add(newMood);
            }
            return result;
        }else{
            // Need to do a read from db.
            throw new RuntimeException("Need to update moods from db");
        }
    }

    /**
     *
     * @return
     */
    public List<MoodEvent> parseToMoodEvent() {
        if (moods != null) {
            for (Map moodEvent : moods) {
                String emotion = (String) moodEvent.get("emotion");
                String reason =(String) moodEvent.get("reason");
                Number social =(Number) moodEvent.get("social");
                Long timestamp = (Long) moodEvent.get("timestamp");
                String username = (String) moodEvent.get("username");
                GeoPoint location = (GeoPoint) moodEvent.get("location");

                if (moodEvent.size() != MAP_MOOD_SIZE) {
                    Log.d("INFO", "Mood isn't complete yet");
                    continue;
                }

                if (timestamp == null) {
                    throw new RuntimeException("[MOOD_ERROR]: Timestamp isn't defined");
                }

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timestamp);

//                MoodEvent newMood = new MoodEvent(date, time, description, state, social_situation, locaation);
//                moodEvents.add(newMood);
            }
        } else {
            throw new RuntimeException("Need to update moods from DB");
        }
        return null;
    }

    /**
     *
     * @return
     */
    public MoodEvent getMostRecentMoodEvent() {
        MoodEvent moodEvent;
        if (moodEvents != null) {
            moodEvent = moodEvents.get(moodEvents.size() - 1);
            return moodEvent;
        } else {
            Log.d("INFO", "No mood events");
            return null;
        }
    }

    public void addMood(MoodEvent moodEvent) {
        if (moodEvent == null) {
            throw new RuntimeException("Mood not defined");
        } else {
            Map<String, Object> mood = new HashMap<String, Object>();
            mood.put("emotion", "SAD");
            mood.put("location", new GeoPoint(53.23, -115.44));
            mood.put("photo", null);
            mood.put("reason", "Something else");
            mood.put("social", 1);
            mood.put("timestamp", 1124245623);
            mood.put("username", "testuser");

            documentReference = collectionReference.document(userName);
            documentReference.update("moods", FieldValue.arrayUnion(mood)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("INFO", "Moods list updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("INFO", "Cannot add mood to list");
                }
            });
        }
    }

    // editMood(MoodEvent moodEvent, Integer index)

    public void deleteMood(Integer index) {
        if (index > moods.size() - 1) {
            moods.remove(index.intValue());
            documentReference = collectionReference.document(userName);
            documentReference.update("moods", moods).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("INFO", "Removed successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("INFO", "Could not remove from array");
                }
            });
        }
    }
}
