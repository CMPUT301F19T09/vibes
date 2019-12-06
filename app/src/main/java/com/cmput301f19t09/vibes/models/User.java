package com.cmput301f19t09.vibes.models;

import android.location.Location;
import android.net.Uri;

import com.cmput301f19t09.vibes.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * User class interacts with the Firebase instance. This model contains many methods that allows
 * communicates with the database. User class allows real-time updates from the database by
 * utilizing Snapshot Listeners and is closely linked with the UserManager to prevent multiple
 * Snapshot Listeners from being produced for an individual user between fragments.
 */
public class User extends Observable implements Serializable {
    /**
     * A comparator that is used for sort a list of users by firstName+lastName
     */
    public static Comparator<User> sortByName = (user1, user2) -> (user1.getFirstName() +
            user1.getLastName())
            .compareTo(user2.getFirstName() + user2.getLastName());
    private transient static CollectionReference collectionReference;
    private transient static DocumentReference documentReference;
    private transient static FirebaseStorage storage;
    private transient static StorageReference storageReference;
    private static boolean connectionStarted;
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
    private transient FirebaseFirestore db;
    private transient Uri profileURL;
    private transient List<MoodEvent> moodEvents;
    private transient List<Map> moods;

    /**
     * Constructs the user object, sets the UID to the passed in UID, and checks if the connection
     * was started before to prevent multiple connections
     *
     * @param uid UID of the user being constructed
     */
    public User(String uid) {
        this.uid = uid;
        this.loadedData = false;
        if (!connectionStarted) { // Makes sure these definitions are called only once.
            connectionStarted = true;

            db = FirebaseFirestore.getInstance();

            collectionReference = db.collection("users");
            storage = FirebaseStorage.getInstance();
        }
    }

    /**
     * An empty constructor which does nothing and doesn't set attributes properly.
     * Used ONLY FOR TESTING.
     */
    public User() {
    }

    /**
     * SnapshotListener that will allow real-time updates
     *
     * @return The listener for UserManager
     */
    ListenerRegistration getSnapshotListener() {
        documentReference = collectionReference.document(uid);

        // Using SnapshotListener helps reduce load times and obtains from local cache
        // Ref https://firebase.google.com/docs/firestore/query-data/listen
        return documentReference.addSnapshotListener((documentSnapshot, e) -> {

            if (documentSnapshot == null) {
                return;
            }

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
                profileURL = Uri.parse("android.resource://com.cmput301f19t09.vibes/"
                        + R.drawable.default_profile_picture);
                setChanged();
                notifyObservers();
            } else {
                storageReference = storage.getReference(picturePath);
                storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            profileURL = uri;
                            setChanged();
                            notifyObservers();
                        }).addOnFailureListener(e1 -> {
                    profileURL = Uri.parse("android.resource://com.cmput301f19t09.vibes/"
                            + R.drawable.default_profile_picture);
                    setChanged();
                    notifyObservers();
                });
            }
        });
    }

    /**
     * Reads the data from the database and calls back to a location once information retrieved from
     * the database due to asynchronous calls
     *
     * @param firebaseCallback The callback listener once information is retrieved
     */
    void readData(final FirebaseCallback firebaseCallback) {
        if (uid == null) {
            throw new RuntimeException("[UserClass]: Username isn't defined for readData()");
        }

        documentReference = collectionReference.document(uid);

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
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
                profileURL = Uri.parse("android.resource://com.cmput301f19t09.vibes/"
                        + R.drawable.default_profile_picture);
                firebaseCallback.onCallback(User.this);
                loadedData = true;
            } else {
                storageReference = storage.getReference(picturePath);
                storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            profileURL = uri;
                            firebaseCallback.onCallback(User.this);
                            loadedData = true;
                            setChanged();
                            notifyObservers();
                        }).addOnFailureListener(e -> {
                    profileURL = Uri.parse("android.resource://com.cmput301f19t09.vibes/"
                            + R.drawable.default_profile_picture);
                    firebaseCallback.onCallback(User.this);
                    loadedData = true;
                    setChanged();
                    notifyObservers();
                });
            }
        }).addOnFailureListener(e -> {
        });
    }

    /**
     * Parse the mapped mood events to MoodEvent objects
     *
     * @return List of MoodEvent objects
     */
    private List<MoodEvent> parseToMoodEvent() {
        List<MoodEvent> events = new ArrayList<>();
        if (moods != null) {
            for (Map moodEvent : moods) {
                String emotion = (String) moodEvent.get("emotion");
                String reason = (String) moodEvent.get("reason");
                Number social = (Number) moodEvent.get("social");
                Long timestamp = (Long) moodEvent.get("timestamp");
                String photoPath = (String) moodEvent.get("photo");

                GeoPoint locationGeoPoint = (GeoPoint) moodEvent.get("location");
                Location location;

                // Checks if there are 7 fields
                // This is the number of elements in the mood map on firebase.
                // I used it to check if a map is complete to show it on the map.
                int MAP_MOOD_SIZE = 7;
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

                if (locationGeoPoint != null) {
                    location = new Location("");
                    location.setLatitude(locationGeoPoint.getLatitude());
                    location.setLongitude(locationGeoPoint.getLongitude());
                } else {
                    location = null;
                }

                assert social != null;
                MoodEvent event = new MoodEvent(time.toLocalDate(),
                        time.toLocalTime(),
                        reason,
                        new EmotionalState(emotion),
                        social.intValue(),
                        null,
                        location,
                        this);
                if (photoPath != null && !photoPath.equals("")) {
                    storageReference = storage.getReference(photoPath);
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                event.setPhoto(uri);
                                //events.add(event);
                            })
                            .addOnFailureListener(e -> {
                                event.setPhoto(null);
                                //events.add(event);
                            });
                }// else {
                    events.add(event);
                //}
            }
        }
        return events;
    }

    /**
     * Flag used to verify user's data is loaded upon retrieving data from
     * readData or getSnapshotListener method
     *
     * @return True or false if the user's information is loaded or not respectively
     */
    public boolean isLoaded() {
        return loadedData;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public List<String> getRequestedList() {
        return requestedList;
    }

    public List<MoodEvent> getMoodEvents() {
        return moodEvents;
    }

    /**
     * Gets the most recent mood event of the user
     *
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
     * Adds a request to the user's requested_list array in the database
     *
     * @param otherUserUID The UID of the user to add to your requested_list array
     */
    public void addRequest(String otherUserUID) {
        documentReference = collectionReference.document(uid);
        documentReference.update("requested_list", FieldValue.arrayUnion(otherUserUID))
                .addOnSuccessListener(aVoid -> {
                    setChanged();
                    notifyObservers();
                }).addOnFailureListener(e -> {
        });
    }

    /**
     * Adds the user's UID to your following_list array in the database
     *
     * @param otherUserUID The UID of the user to add to your following_list array
     */
    private void addFollowing(String otherUserUID) {
        if (!followingList.contains(otherUserUID)) {
            documentReference = collectionReference.document(uid);
            documentReference.update("following_list", FieldValue.arrayUnion(otherUserUID))
                    .addOnSuccessListener(aVoid -> {
                        setChanged();
                        notifyObservers();
                    }).addOnFailureListener(e -> {
            });
        }
    }

    /**
     * Removes the user's UID from your following_list array in the database
     *
     * @param otherUserUID The UID of the user to remove from your following_list array
     */
    public void removeFollowing(String otherUserUID) {
        if (followingList.contains(otherUserUID)) {
            documentReference = collectionReference.document(uid);
            documentReference.update("following_list", FieldValue.arrayRemove(otherUserUID))
                    .addOnSuccessListener(aVoid -> {
                        setChanged();
                        notifyObservers();
                    }).addOnFailureListener(e -> {
            });
        }
    }

    /**
     * Removes the UID from your requested_list and adds to the other user's following_list in the
     * database
     *
     * @param otherUserUID The UID of the user you are accepting
     */
    public void acceptRequest(String otherUserUID) {
        removeRequest(otherUserUID);
        User otherUser = UserManager.getUser(otherUserUID);
        if (!otherUser.getFollowingList().contains(UserManager.getCurrentUserUID())) {
            otherUser.addFollowing(UserManager.getCurrentUserUID());
        }
    }

    /**
     * Removes the UID from your requested_list and does not add to the other user's following_list
     * in the database
     *
     * @param otherUserUID The UID of the user to remove request from requested_list
     */
    public void removeRequest(String otherUserUID) {
        if (requestedList.contains(otherUserUID)) {
            documentReference = collectionReference.document(uid);
            documentReference.update("requested_list", FieldValue.arrayRemove(otherUserUID))
                    .addOnSuccessListener(aVoid -> {
                        requestedList.remove(otherUserUID);
                        setChanged();
                        notifyObservers();
                    }).addOnFailureListener(e -> {
            });
        }
    }

    /**
     * Adds a mood event to the database. Puts location and photo data if exists
     *
     * @param moodEvent The mood event to add to the database
     */
    public void addMood(MoodEvent moodEvent) {
        // Checks if the MoodEvent is not null
        if (moodEvent == null) {
            throw new RuntimeException("MoodEvent not defined");
        } else {
            // Parses the MoodEvent to a map usable in the database
            Map<String, Object> mood = new HashMap<>();
            LocalDateTime time = LocalDateTime.of(moodEvent.date, moodEvent.time);
            mood.put("emotion", moodEvent.getState().getEmotion());
            mood.put("timestamp", time.toEpochSecond(ZoneOffset.UTC));
            mood.put("reason", moodEvent.getDescription());
            mood.put("social", moodEvent.getSocialSituation());
            mood.put("username", moodEvent.getUser().getUserName());

            if (moodEvent.getLocation() != null) {
                mood.put("location", new GeoPoint(moodEvent.getLocation().getLatitude(),
                        moodEvent.getLocation().getLongitude()));
            } else {
                mood.put("location", null);
            }
            if (moodEvent.getPhoto() != null) {
                // Get's the hashcode of the photo and stores in the database before updating document
                String photoPath = "reason_photos/" + moodEvent.getPhoto().hashCode() + ".jpeg";
                mood.put("photo", photoPath);
                storageReference = storage.getReference(photoPath);
                storageReference.putFile(moodEvent.getPhoto())
                        .addOnSuccessListener(taskSnapshot -> {
                            documentReference = collectionReference.document(uid);
                            documentReference.update("moods", FieldValue.arrayUnion(mood))
                                    .addOnSuccessListener(aVoid -> {
                                        setChanged();
                                        notifyObservers();
                                    }).addOnFailureListener(e -> {
                            }).addOnFailureListener(e -> {
                            });
                        });
            } else {
                mood.put("photo", null);
                documentReference = collectionReference.document(uid);
                documentReference.update("moods", FieldValue.arrayUnion(mood))
                        .addOnSuccessListener(aVoid -> {
                            setChanged();
                            notifyObservers();
                        }).addOnFailureListener(e -> {
                });
            }
        }
    }

    /**
     * Takes a MoodEvent and replace it in the database at the given index location. Checks if the
     * photo is a local photo or a Firebase photo and compare the changes before updating
     *
     * @param moodEvent The mood event to replace in the database
     * @param index     The location in the array in the database
     */
    public void editMood(MoodEvent moodEvent, Integer index) {
        if (index <= moods.size() - 1) {
            // Parses the MoodEvent to a map usable in the database
            Map<String, Object> mood = new HashMap<>();
            LocalDateTime time = LocalDateTime.of(moodEvent.date, moodEvent.time);
            mood.put("emotion", moodEvent.getState().getEmotion());
            mood.put("reason", moodEvent.getDescription());
            mood.put("social", moodEvent.getSocialSituation());
            mood.put("timestamp", moodEvent.getEpochUTC());
            mood.put("username", moodEvent.getUser().getUserName());

            if (moodEvent.getLocation() != null) {
                mood.put("location", new GeoPoint(moodEvent.getLocation().getLatitude(),
                        moodEvent.getLocation().getLongitude()));
            } else {
                mood.put("location", null);
            }

            String lastURISegment = null;

            if (moodEvent.getPhoto() != null) {
                List<String> uriSegments = moodEvent.getPhoto().getPathSegments();
                lastURISegment = uriSegments.get(uriSegments.size() - 1);
            }

            // Checks whether the photo is the same after editing
            if (moodEvent.getPhoto() != null &&
                    lastURISegment.equals(moods.get(index).get("photo"))) {
                String photoPath = (String) moods.get(index).get("photo");
                mood.put("photo", photoPath);
                moods.set(index, mood);
                documentReference = collectionReference.document(uid);
                documentReference.update("moods", moods).addOnSuccessListener(aVoid -> {
                }).addOnFailureListener(e -> {
                });
            } else if (moodEvent.getPhoto() != null) {
                // Only updates Firebase Storage if photo is changed
                String photoPath = "reason_photos/" + moodEvent.getPhoto().hashCode() + ".jpeg";
                mood.put("photo", photoPath);
                moods.set(index, mood);
                storageReference = storage.getReference(photoPath);
                storageReference.putFile(moodEvent.getPhoto())
                        .addOnSuccessListener(taskSnapshot -> {
                            documentReference = collectionReference.document(uid);
                            documentReference.update("moods", moods).addOnSuccessListener(aVoid -> {
                            }).addOnFailureListener(e -> {
                            });
                        });
            } else {
                // Does not set storage reference or photo field if photo is empty
                mood.put("photo", null);
                moods.set(index, mood);
                documentReference = collectionReference.document(uid);
                documentReference.update("moods", moods)
                        .addOnSuccessListener(aVoid -> {
                        }).addOnFailureListener(e -> {
                });
            }
        }
    }

    /**
     * Removes a mood event from the database by index number
     *
     * @param index Array index to remove MoodEvent
     */
    public void deleteMood(Integer index) {
        if (index <= moods.size() - 1) {
            moods.remove(index.intValue());
            documentReference = collectionReference.document(uid);
            documentReference.update("moods", moods)
                    .addOnSuccessListener(aVoid -> {
                        setChanged();
                        notifyObservers();
                    }).addOnFailureListener(e -> {
            });
        }
    }

    /**
     * Changes the user's profile picture from local storage and stores in Firebase by hashcode
     *
     * @param uri The uri of the selected photo to change to
     */
    public void changeProfilePicture(Uri uri) {
        if (uri != null) {
            picturePath = "image/" + uri.hashCode() + ".jpeg";
            storageReference = storage.getReference(picturePath);
            storageReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        collectionReference = db.collection("users");
                        collectionReference.document(uid).update("profile_picture",
                                picturePath)
                                .addOnSuccessListener(aVoid -> {
                                    setChanged();
                                    notifyObservers();
                                }).addOnFailureListener(e -> {
                        });
                    }).addOnFailureListener(e -> {
            });
        }
    }

    /**
     * Callback listener when data from the database is retrieved
     */
    public interface FirebaseCallback {
        void onCallback(User user);
    }
}
