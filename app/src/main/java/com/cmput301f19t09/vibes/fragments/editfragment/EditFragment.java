package com.cmput301f19t09.vibes.fragments.editfragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.BuildConfig;
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 * TODO: make the social situation editor a drop down with predefined values
 */
public class EditFragment extends Fragment {

    // the fragment initialization parameters
    public static final String VIBES_MOODEVENT = "com.cmput301f19t09.vibes.MOODEVENT";
    public static final String VIBES_INDEX = "com.cmput301f19t09.vibes.INDEX";
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GALLERY = 3;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 4;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private MoodEvent moodEvent;
    private boolean editing;
    private User user;
    private int moodEventListIndex;
    // date and time
    private TextView dateTextView;
    private TextView timeTextView;
    // state setting
    private ArrayList<String> emotionalStateKeys = EmotionalState.getListOfKeys();
    private EmotionalState emotionalState = null;
    private boolean validReason = true;
    // photo for reason
    private Uri photoUri;
    private ImageView photoImage;
    private ImageButton captureButton;
    private ImageButton galleryButton;
    private ImageButton clearButton;
    private EditText editReasonView;
    // location services
    private Location mLocation = null;
    private Switch locationSwitch;
    private boolean useLocation = false;
    private FusedLocationProviderClient fusedLocationClient;
    private Snackbar snackbar;
    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;


    // buttons
    private Button buttonSubmitView;
    private Button buttonCancelView;

    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters when we are
     * editing an existing MoodEvent.
     *
     * @param moodEvent The MoodEvent we want to edit.
     * @param index     The location in the users List of moods map that moodEvent occurrs.
     * @return A new instance of fragment EditFragment.
     */
    public static EditFragment newInstance(MoodEvent moodEvent, int index) {
        // called when we pass in a MoodEvent and its index in the moodEventList
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(VIBES_MOODEVENT, moodEvent);
        args.putInt(VIBES_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment when we are adding a new MoodEvent.
     *
     * @return A new instance of fragment EditFragment.
     */
    public static EditFragment newInstance() {
        // called when we don't pass in a MoodEvent
        return new EditFragment();
    }

    /**
     * Check if arguments were set on fragment initialization. If no arguments were
     * set then we did not pass in a MoodEvent and its index meaning we were not
     * editing an existing MoodEvent but rather, were creating a new one. Sets the
     * global editing variable to indicate in onCreateView whether we are editing or
     * adding. Saves the passed in MoodEvent and its index if we are editing for calling
     * User.editMood() on submit button press.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) { // a MoodEvent was passed so set fields based on it
            moodEvent = (MoodEvent) getArguments().getSerializable(VIBES_MOODEVENT);
            moodEventListIndex = getArguments().getInt(VIBES_INDEX);
            editing = true;
        } else { // we didn't pass a MoodEvent so we are creating a new one
            editing = false;

            // only set up location api when we need it (when creating a new mood event)
            // create an instance of the Fused Location Provider Client
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mSettingsClient = LocationServices.getSettingsClient(getContext());

            // Kick off the process of building the LocationCallback, LocationRequest, and
            // LocationSettingsRequest objects.
            createLocationCallback();
            createLocationRequest();
            buildLocationSettingsRequest();
        }
    }

    /**
     * Graphical initialization of the fragment. Handles the cases for when
     * we are editing or adding a MoodEvent separately. When we are editing
     * we need to initialize the fields from the existing MoodEvent's attributes
     * and 'select' the appropriate EmotionalState gridView. When adding a new mood
     * we initialize a new empty/null MoodEvent.
     * <p>
     * Input from the fields is pulled. Enforces input requirements.
     * <p>
     * Returns the View at the end.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        // get user in current session
        user = UserManager.getCurrentUser();

        // enable buttons
        buttonSubmitView = view.findViewById(R.id.button_submit_view);
        buttonCancelView = view.findViewById(R.id.button_cancel_view);
        buttonSubmitView.setEnabled(false);
        buttonCancelView.setEnabled(true);


        TextView titleTextView = view.findViewById(R.id.title_textview);
        ChipGroup stateChipGroup = view.findViewById(R.id.emotion_chip_group);
        ImageView emotionImageView = view.findViewById(R.id.emotion_image);

        emotionImageView.setVisibility(GONE);

        int bgColor = ResourcesCompat.getColor(getResources(), android.R.color.darker_gray, null);

        for (String key : emotionalStateKeys) {
            Chip emotionChip = (Chip) inflater.inflate(R.layout.edit_chip, null);
            stateChipGroup.addView(emotionChip);

            emotionChip.setCheckable(true);
            emotionChip.setClickable(true);

            EmotionalState state = new EmotionalState(key);

            emotionChip.setText(key.charAt(0) + key.substring(1).toLowerCase());
            emotionChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        emotionalState = state;

                        emotionChip.setChipBackgroundColor(ColorStateList.valueOf(state.getColour()));
                        emotionImageView.setImageResource(state.getImageFile());
                        Log.d("TEST/Chips", state.getEmotion() + " is checked");
                    } else {
                        emotionChip.setChipBackgroundColor(ColorStateList.valueOf(bgColor));
                    }
                }
            });
            emotionChip.setTag(key);
        }

        stateChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                if (i == -1) {
                    emotionalState = null;
                    emotionImageView.setVisibility(GONE);
                    buttonSubmitView.setEnabled(false);
                } else {
                    emotionImageView.setVisibility(VISIBLE);
                }

                if (validReason && emotionalState != null) {
                    buttonSubmitView.setEnabled(true);
                }
            }
        });

        stateChipGroup.setClickable(true);
        stateChipGroup.setEnabled(true);

        ChipGroup socialChipGroup = view.findViewById(R.id.social_chip_group);

        for (String situation : getResources().getStringArray(R.array.situations)) {
            Chip socialChip = (Chip) inflater.inflate(R.layout.edit_chip, null);

            socialChip.setClickable(true);
            socialChip.setCheckable(true);

            socialChip.setText(situation);

            socialChipGroup.addView(socialChip);
            socialChip.setTag(situation);
        }

        dateTextView = view.findViewById(R.id.date_text_view);
        timeTextView = view.findViewById(R.id.time_text_view);
        editReasonView = view.findViewById(R.id.edit_reason_view);

        photoImage = view.findViewById(R.id.photo_image);
        photoImage.setVisibility(GONE);
        captureButton = view.findViewById(R.id.capture_button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the user asks to capture a photo using the camera, we check if the user has permission
                // and the user is using the correct build version, if so the camera is opened.
                // ref https://devofandroid.blogspot.com/2018/09/take-picture-with-camera-android-studio.html
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, CAMERA_PERMISSIONS_REQUEST_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });
        galleryButton = view.findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // A new intent is created where the user can view all of the photos in
                // the image directory/
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");

                if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
                }
            }
        });
        clearButton = view.findViewById(R.id.clear_photo_button);
        clearButton.setVisibility(GONE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            // The image is cleared in both the mood object and the displayed view.
            @Override
            public void onClick(View v) {
                photoUri = null;

                photoImage.setVisibility(GONE);
                clearButton.setVisibility(GONE);
                setPhotoImage(null, photoImage);
            }
        });

        locationSwitch = view.findViewById(R.id.location_switch);

        if (editing) {
            // populate the EditText's with the MoodEvent attributes; we are editing an existing MoodEvent

            titleTextView.setText(R.string.edit_mood_title);

            dateTextView.setText(moodEvent.getDateString());
            timeTextView.setText(moodEvent.getTimeString());
            int socialSituation = moodEvent.getSocialSituation();
            if (moodEvent.getSocialSituation() != -1) { // social situation was specified
                String[] situations = getResources().getStringArray(R.array.situations);
                // Make sure the situation index is wrapped (since many moods were uploaded before this was constrained)
                socialSituation %= situations.length;
                ((Chip) socialChipGroup.findViewWithTag(situations[socialSituation])).setChecked(true);
            }

            editReasonView.setText(moodEvent.getDescription());

            emotionalState = moodEvent.getState();
            ((Chip) stateChipGroup.findViewWithTag(emotionalState.getEmotion())).setChecked(true);

            // We set the photo displayed to the photo associated with this mood.
            photoUri = moodEvent.getPhoto();

            if (photoUri != null) {
                setPhotoImage(photoUri, photoImage);
            } else {
                setPhotoImage(null, photoImage);
            }

            // set the use location slider based on whether the mood event has a location or not
            if (moodEvent.getLocation() != null) {
                // set the slider to ON
                useLocation = true;
                locationSwitch.setChecked(true);
            } else {
                locationSwitch.setChecked(false);
            }
            // turn off slider interaction as locations should not be editable
            locationSwitch.setEnabled(false);

            // all required fields are completed already
            buttonSubmitView.setEnabled(true);
        } else {
            // don't prepopulate the EditText's; we are creating a new MoodEvent
            // set moodEvent to be an empty new MoodEvent object for the current user
            titleTextView.setText(R.string.new_mood_title);
            moodEvent = new MoodEvent(null, null, null, null, -1, null, null, user);

            // set the current date
            LocalDate date = LocalDate.now();
            dateTextView.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            // can update immediately because cant be edited
            moodEvent.setDate(dateTextView.getText().toString());

            // set the current time
            LocalTime time = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            timeTextView.setText(time.format(timeFormatter));
            // can update immediately because cant be edited
            moodEvent.setTime(time);
        }

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useLocation = b; // b indicates whether switch is "ON" = 1 or "OFF" = 0
                if (b) {
                    if (!checkPermissions()) { // permissions were denied
                        requestPermissionFragment(); // prompt user for permission
                    } else {
                        startLocationUpdates();
                    }
                }
            }
        });

        editReasonView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // auto-generated stub; do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                String[] splitStr = str.trim().split("\\s+"); // split the string at whitespace

                // reason must be 3 words or less and must have all required fields set
                if (splitStr.length <= 3) {
                    validReason = true;
                    if (emotionalState != null) {
                        buttonSubmitView.setEnabled(true);
                    }
                } else { // disable the button
                    validReason = false;
                    buttonSubmitView.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // auto-generated stub; do nothing
            }
        });

        // clicked the submit button
        // update the MoodEvent appropriately and return from the fragment
        buttonSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                moodEvent.setState(emotionalState);

                Chip selectedSocial = view.findViewById(socialChipGroup.getCheckedChipId());
                if (selectedSocial != null) {
                    String socialText = selectedSocial.getText().toString();
                    String[] situations = getResources().getStringArray(R.array.situations);

                    boolean found_situation = false;

                    for (int i = 0; i < situations.length; i++) {
                        if (situations[i].equals(socialText)) {
                            found_situation = true;
                            moodEvent.setSocialSituation(i);
                            break;
                        }
                    }

                    if (!found_situation) {
                        Log.d("TEST/SocialChips", "no situation selected");
                        moodEvent.setSocialSituation(-1);
                    }
                }

                // set location
                if (useLocation) { // user specified to use location
                    if (!editing) { // if we are adding a mood
                        // then pull location data from GPS
                        moodEvent.setLocation(mLocation);

                        // log
                        if (mLocation != null) {
                            String logLocation = Location.convert(
                                    mLocation.getLatitude(), Location.FORMAT_DEGREES)
                                    + " "
                                    + Location.convert(mLocation.getLongitude(), Location.FORMAT_DEGREES
                            );
                            Log.d("LOCATION_SET", logLocation);
                        } else {
                            Log.d("LOCATION_DEVICE", "device location is null");
                        }

                    } // otherwise we are editing a mood and it is already set so dont touch it
                } else { // user does not want to use location
                    moodEvent.setLocation(null);
                    Log.d("LOCATION_SET", "location set to null");
                }


                if (editReasonView.getText().toString().isEmpty()) {
                    moodEvent.setDescription("");
                } else {
                    moodEvent.setDescription(editReasonView.getText().toString());
                }

                if (photoUri != null) {
                    moodEvent.setPhoto(photoUri);
                } else {
                    moodEvent.setPhoto(null);
                }

                if (!editing) {
                    // was adding a new mood
                    stopLocationUpdates();
                    user.addMood(moodEvent);
                } else {
                    // was editing
                    user.editMood(moodEvent, moodEventListIndex);
                }
                MainActivity main = (MainActivity) EditFragment.this.getActivity();
                main.onBackPressed();
            }
        });

        // cancel button listener
        buttonCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /* Remove the snackbar which was being displayed on the fragment so that
         * if the user goes back and the fragment is still being displayed there will
         * be a not attached to fragment issue.
         */
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        if (snackbar != null) {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener);
        snackbar.show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Used the following links for grabbing location from GPS but code was modified for our use:
     * https://developer.android.com/training/location/receive-location-updates
     * https://github.com/android/location-samples/tree/master/LocationUpdates
     *
     * Copyright 2019 Google LLC
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

    /**
     * If your app doesn't already have the permission it needs, the app must call one of the requestPermissionFragment()
     * methods to request the appropriate permissions. Your app passes the permissions it wants and an integer
     * request code that you specify to identify this permission request. This method functions asynchronously.
     * It returns right away, and after the user responds to the prompt, the system calls the app's callback
     * method with the results, passing the same request code that the app passed to requestPermissionFragment().
     */
    private void requestPermissionFragment() {
        /**
         * shouldShowRequestPermissionRationale(), returns true if the user has previously denied the request,
         * and returns false if a user has denied a permission and selected the Don't ask again option in the
         * permission request dialog, or if a device policy prohibits the permission.
         */
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            useLocation = false;
            locationSwitch.setChecked(false);
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            requestPermissions(
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            useLocation = false;
            locationSwitch.setChecked(false);
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    useLocation = false;
                    locationSwitch.setChecked(false);
                    Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                } else {
                    // Permission denied.
                    useLocation = false;
                    locationSwitch.setChecked(false);

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    showSnackbar(R.string.permission_denied_explanation,
                            R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
                break;
            //ref https://devofandroid.blogspot.com/2018/09/take-picture-with-camera-android-studio.html
            case CAMERA_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openCamera();
                } else {
                    Log.d("Permission denied:", "The permission to the camera was denied.");
                }
        }

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
//                Log.d("LOCATION", "updated");
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    mLocation = location;
                }
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * If the requestCode is REQUEST_CHECK_SETTINGS, wecheck whether the user provided
     * the required location settings and handle the cases appropriately.
     * <p>
     * If the requestCode is REQUEST_IMAGE_GALLERY or REQUEST_IMAGE_CAPTURE, we update the
     * photo associated with the mood and displayed in this fragment.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
//                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        useLocation = false;
                        locationSwitch.setChecked(false);
                        break;
                }
                break;
            // Upon selecting a photo from the gallery, the photo associated with the MoodEvent
            // object and the mood displayed in the EditFragment will be changed.
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    photoUri = data.getData();
                    photoImage.setVisibility(VISIBLE);
                    clearButton.setVisibility(VISIBLE);
                    setPhotoImage(photoUri, photoImage);
                }
                break;
            // Upon capturing a photo with the camera, the photo associated with the MoodEvent
            // object and the mood displayed in the EditFragment will be changed.
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK && data != null) {
                    photoImage.setVisibility(VISIBLE);
                    clearButton.setVisibility(VISIBLE);
                    setPhotoImage(photoUri, photoImage);
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        Log.d("SL", "location updates started");
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        useLocation = true;
                        locationSwitch.setChecked(true);

                        //noinspection MissingPermission
                        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                                useLocation = false;
                                locationSwitch.setChecked(false);
                        }
                    }
                });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Starts a new intent to capture a photo using the camera.
     * <p>
     * If photo is captured successfully, it is handled under the
     * method onActivityResult() under the RESULT_IMAGE_CAPTURE case.
     * <p>
     * The class variable photoUri will be overwritten and contain the
     * path to the photo captured.
     * <p>
     * https://devofandroid.blogspot.com/2018/09/take-picture-with-camera-android-studio.html
     */
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * @param uri
     * @param imageView Sets the image displayed in imageView to the photo indicated by uri.
     *                  If uri is null, the photo is set to the default photo.
     */
    private void setPhotoImage(Uri uri, ImageView imageView) {
        if (uri != null) {
            Glide.with(this).load(uri).into(imageView);
            imageView.setVisibility(VISIBLE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
