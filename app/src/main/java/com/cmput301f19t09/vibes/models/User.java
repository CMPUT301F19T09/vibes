package com.cmput301f19t09.vibes.models;

import android.location.Location;
import android.net.Uri;

import com.cmput301f19t09.vibes.R;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class User extends Observable implements Serializable {
    private String uid;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String picturePath;
    private List<String> followingList;
    private List<String> requestedList;

    private boolean loadedData;

    // Objects are not serializable - will crash on switching app if not omitted from serialization
    // Ref https://stackoverflow.com/questions/14582440/how-to-exclude-field-from-class-serialization-in-runtime
    private transient static FirebaseFirestore db;
    private transient static CollectionReference collectionReference ;
    private transient static DocumentReference documentReference;
    private transient static FirebaseStorage storage;
    private transient static StorageReference storageReference;
    private transient Uri profileURL;

    private transient List<MoodEvent> moodEvents;
    private transient List<Map> moods;

    private static boolean connectionStarted;

    // This is the number of elements in the mood map on firebase.
    // I used it to check if a map is complete to show it on the map.
    final private int MAP_MOOD_SIZE = 7;

    /**
     * Callback listener when data from the database is retrieved
     */
    public interface FirebaseCallback {
        void onCallback(User user);
    }

    /**
     * Callback listener when checking database if user exists
     */
    public interface UserExistListener {
        void onUserExists();
        void onUserNotExists();
    }

    /**
     * Constructs the user object, sets the UID to the passed in UID, and checks if the connection
     * was started before to prevent multiple connections
     * @param uid UID of the user being constructed
     */
    public User(String uid) {
        this.uid = uid;
        this.loadedData = false;
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
     * SnapshotListener that will allow real-time updates
     * @return The listener for UserManager
     */
    public ListenerRegistration getSnapshotListener() {
        documentReference = collectionReference.document(uid);

        // Using SnapshotListener helps reduce load times and obtains from local cache
        // Ref https://firebase.google.com/docs/firestore/query-data/listen
        return documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                userName = documentSnapshot.getString("username");
                firstName = documentSnapshot.getString("first");
                lastName = documentSnapshot.getString("last");
                email = documentSnapshot.getString("email");
                picturePath = documentSnapshot.getString("profile_picture");
                followingList = (List<String>) documentSnapshot.get("following_list");
                requestedList = (List<String>) documentSnapshot.get("requested_list");
                moods = (List<Map>) documentSnapshot.get("moods");
                loadedData = true;

                // Parses the retrieved data to MoodEvent object
                moodEvents = parseToMoodEvent();

                // Gets profile picture from FireBase Storage if not null
                if (picturePath == null) {
                    profileURL = Uri.parse("android.resource://com.cmput301f19t09.vibes/" + R.drawable.default_profile_picture);
                    setChanged();
                    notifyObservers();
                } else {
                    storageReference = storage.getReference(picturePath);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileURL = uri;
                            setChanged();
                            notifyObservers();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
        });
    }

    /**
     * Reads the data from the database and calls back to a location once information retrieved from
     * the database due to asynchronous calls
     * @param firebaseCallback The callback listener once information is retrieved
     */
    public void readData(FirebaseCallback firebaseCallback) {
        if(uid == null) {
            throw new RuntimeException("[UserClass]: Username isn't defined for readData()");
        }

        documentReference = collectionReference.document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userName = documentSnapshot.getString("username");
                firstName = documentSnapshot.getString("first");
                lastName = documentSnapshot.getString("last");
                email = documentSnapshot.getString("email");
                picturePath = documentSnapshot.getString("profile_picture");
                followingList = (List<String>) documentSnapshot.get("following_list");
                requestedList = (List<String>) documentSnapshot.get("requested_list");
                moods = (List<Map>) documentSnapshot.get("moods");

                // Parses the retrieved data to MoodEvent object
                moodEvents = parseToMoodEvent();

                // Gets profile picture from FireBase Storage if not null
                if (picturePath == null) {
                    profileURL = Uri.parse("android.resource://com.cmput301f19t09.vibes/" + R.drawable.default_profile_picture);
                    firebaseCallback.onCallback(User.this);
                    loadedData = true;
                } else {
                    storageReference = storage.getReference(picturePath);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileURL = uri;
                            firebaseCallback.onCallback(User.this);
                            loadedData = true;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public boolean isLoaded()
    {
        return loadedData;
    }

    public String getUid()
    {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() { ;
        return userName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public Uri getProfileURL() {
        return profileURL;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public List<String> getRequestedList() {
        return requestedList;
    }

    /**
     * Checks whether or mot the user already exists by checking UIDs
     * @param userExistListener A Listener to call back when user exists or not
     */
    public void exists(UserExistListener userExistListener) {
        collectionReference.document(uid).get()
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

    /**
     * Parse the mapped mood events to MoodEvent objects
     * @return List of MoodEvent objects
     */
    public List<MoodEvent> parseToMoodEvent() {
        List<MoodEvent> events = new ArrayList<MoodEvent>();
        if (moods != null) {
            for (Map moodEvent : moods) {
                String emotion = (String) moodEvent.get("emotion");
                String reason = (String) moodEvent.get("reason");
                Number social = (Number) moodEvent.get("social");
                Long timestamp = (Long) moodEvent.get("timestamp");
                String username = (String) moodEvent.get("username");
                GeoPoint locationGeoPoint = (GeoPoint) moodEvent.get("location");

                // Checks if there are 7 fields
                if (moodEvent.size() != MAP_MOOD_SIZE) {
                    continue;
                }

                // Requires a timestamp
                if (timestamp == null) {
                    throw new RuntimeException("[MOOD_ERROR]: Timestamp isn't defined");
                }

                // Convert to local time
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timestamp);

                LocalDateTime time = LocalDateTime.ofEpochSecond(
                        timestamp,
                        0,
                        ZoneOffset.UTC
                );

                // Not implemented yet (hardcoded)
                Location location = new Location("");
                location.setLatitude(locationGeoPoint.getLatitude());
                location.setLongitude(locationGeoPoint.getLongitude());

                MoodEvent event = new MoodEvent(time.toLocalDate(),
                        time.toLocalTime(),
                        reason,
                        new EmotionalState(emotion),
                        social.intValue(),
                        location,
                        this);
                events.add(event);
            }
        }
        return events;
    }

    public List<MoodEvent> getMoodEvents() {
        return moodEvents;
    }

    /**
     * Gets the most recent mood event of the user
     * @return The most recent MoodEvent object
     */
    public MoodEvent getMostRecentMoodEvent() {
        MoodEvent moodEvent;
        // Checks if there are mood events in the list of MoodEvents
        if (moodEvents != null && moodEvents.size() != 0) {
            moodEvent = moodEvents.get(0);

            for (MoodEvent event : moodEvents) {
                if (moodEvent.compareTo(event) <= 0) {
                    moodEvent = event;
                }
            }

            return moodEvent;
        } else {
            return null;
        }
    }

    /**
     * Adds a mood event to the database
     * @param moodEvent The mood event to add to the database
     */
    public void addMood(MoodEvent moodEvent) {
        // Checks if the MoodEvent is not null
        if (moodEvent == null) {
            throw new RuntimeException("MoodEvent not defined");
        } else {
            // Parses the MoodEvent to a map usable in the database
            Map<String, Object> mood = new HashMap<String, Object>();
            LocalDateTime time = LocalDateTime.of(moodEvent.date, moodEvent.time);
            mood.put("emotion", moodEvent.getState().getEmotion());
            mood.put("location", new GeoPoint(53.23, -115.44)); // Location currently not implemented yet (hardcoded)
            mood.put("photo", null); // Photo not implemented yet
            mood.put("timestamp", time.toEpochSecond(ZoneOffset.UTC));
            mood.put("reason", moodEvent.getDescription());
            mood.put("social", moodEvent.getSocialSituation());
            mood.put("username", moodEvent.getUser().getUserName());

            documentReference = collectionReference.document(uid);
            documentReference.update("moods", FieldValue.arrayUnion(mood)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    /**
     * Takes a MoodEvent and replace it in the database at the given index location
     * @param moodEvent The mood event to replace in the database
     * @param index The location in the array in the database
     */
    public void editMood(MoodEvent moodEvent, Integer index) {
        if (index > moods.size() - 1) {
            return;
        } else {
            // Parses the MoodEvent to a map usable in the database
            Map<String, Object> mood = new HashMap<String, Object>();
            LocalDateTime time = LocalDateTime.of(moodEvent.date, moodEvent.time);
            mood.put("emotion", moodEvent.getState().getEmotion());
            mood.put("location", new GeoPoint(53.23, -115.44)); // Location currently not implemented yet (hardcoded)
            mood.put("photo", null); // Photo not implemented yet
            mood.put("reason", moodEvent.getDescription());
            mood.put("social", moodEvent.getSocialSituation());
            mood.put("timestamp", moodEvent.getEpochUTC());
            mood.put("username", moodEvent.getUser().getUserName());

            moods.set(index.intValue(), mood);
            documentReference = collectionReference.document(uid);
            documentReference.update("moods", moods).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    /**
     * Removes a mood event from the database by index number
     * @param index Array index to remove MoodEvent
     */
    public void deleteMood(Integer index) {
        if (index > moods.size() - 1) {
            return;
        } else {
            moods.remove(index.intValue());
            documentReference = collectionReference.document(uid);
            documentReference.update("moods", moods).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }
}
