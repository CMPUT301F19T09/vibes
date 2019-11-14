package com.cmput301f19t09.vibes.fragments.editfragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f19t09.vibes.BuildConfig;
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.User;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.MoodEvent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 * TODO: make the social situation editor a drop down with predefined values
 * TODO: implement pulling location from GPS
 */
public class EditFragment extends Fragment implements AdapterView.OnItemClickListener {

    // the fragment initialization parameters
    public static final String VIBES_MOODEVENT = "com.cmput301f19t09.vibes.MOODEVENT";
    private MoodEvent moodEvent;
    private boolean moodSet;
    private User user;
    public static final String VIBES_INDEX = "com.cmput301f19t09.vibes.INDEX";
    private int moodEventListIndex;

    // date and time
    private TextView dateTextView;
    private TextView timeTextView;

    // state setting
    private GridView stateGridView;
    private TextView stateTextView;
    private ArrayList<String> stateKeys = EmotionalState.getListOfKeys();
    private EmotionalState emotionalState = null;

    private EditText editSituationView;
    private EditText editReasonView;

    // location services
    private Location mLocation = null;
    private Switch locationSwitch;
    private boolean useLocation;
    private FusedLocationProviderClient fusedLocationClient;

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
     * @param   moodEvent
     *      The MoodEvent we want to edit.
     * @param   index
     *      The location in the users List of moods map that moodEvent occurrs.
     *
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
     * global moodSet variable to indicate in onCreateView whether we are editing or
     * adding. Saves the passed in MoodEvent and its index if we are editing for calling
     * User.editMood() on submit button press.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getContext());

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        if (getArguments() != null) { // a MoodEvent was passed so set fields based on it
            moodEvent = (MoodEvent) getArguments().getSerializable(VIBES_MOODEVENT);
            moodEventListIndex = getArguments().getInt(VIBES_INDEX);
            moodSet = true;
        }
        else { // we didn't pass a MoodEvent so we are creating a new one
            moodSet = false;
        }
    }

    /**
     * Graphical initialization of the fragment. Handles the cases for when
     * we are editing or adding a MoodEvent separately. When we are editing
     * we need to initialize the fields from the existing MoodEvent's attributes
     * and 'select' the appropriate EmotionalState gridView. When adding a new mood
     * we initialize a new empty/null MoodEvent.
     *
     * Input from the fields is pulled. Enforces input requirements.
     *
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


        stateGridView = view.findViewById(R.id.state_grid_view);
        stateGridView.setAdapter(new ImageAdapter(getActivity()));
        stateGridView.setOnItemClickListener(this);
        stateTextView = view.findViewById(R.id.state_text_view);

        dateTextView = view.findViewById(R.id.date_text_view);
        timeTextView = view.findViewById(R.id.time_text_view);
        editSituationView = view.findViewById(R.id.edit_situation_view);
        editReasonView = view.findViewById(R.id.edit_reason_view);

        locationSwitch = view.findViewById(R.id.location_switch);

        if (moodSet) {
            // populate the EditText's with the MoodEvent attributes; we are editing an existing MoodEvent

            dateTextView.setText(moodEvent.getDateString());
            timeTextView.setText(moodEvent.getTimeString());
            if (moodEvent.getSocialSituation() != -1) { // social situation was specified
                String situationString = Integer.toString(moodEvent.getSocialSituation());
                editSituationView.setText(situationString);
            }
            editReasonView.setText(moodEvent.getDescription());
            emotionalState = moodEvent.getState();
            stateTextView.setText(moodEvent.getState().getEmotion());

            // all required fields are completed already
            buttonSubmitView.setEnabled(true);
        }
        else {
            // don't prepopulate the EditText's; we are creating a new MoodEvent
            // set moodEvent to be an empty new MoodEvent object for the current user
            moodEvent = new MoodEvent(null, null, null, null, -1, null, user);

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
            moodEvent.setTime(timeTextView.getText().toString());

            // TODO: fix location handling
            if (!checkPermissions()) { // permissions were denied
                requestPermissions(); // prompt user for permission
            }

//            location.setLatitude(53.5461);
//            location.setLongitude(-113.4938);

        }

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useLocation = b; // b indicates whether switch is "ON" = 1 or "OFF" = 0
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
                if (splitStr.length <= 3 && emotionalState != null) {
                    buttonSubmitView.setEnabled(true);
                }
                else { // disable the button
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
                stopLocationUpdates();

                moodEvent.setState(emotionalState);

                // set optional fields
                if (!editSituationView.getText().toString().isEmpty()) {
                    moodEvent.setSocialSituation(Integer.parseInt(editSituationView.getText().toString()));
                }

                if (useLocation) {
                    moodEvent.setLocation(mLocation);
                } else {
                    moodEvent.setLocation(null);
                }
                if (mLocation != null) {
                    String logLocation = Location.convert(
                            mLocation.getLatitude(), Location.FORMAT_DEGREES)
                            + " "
                            + Location.convert(mLocation.getLongitude(), Location.FORMAT_DEGREES
                    );
                    Log.d("LOCATION", logLocation);
                } else {
                    Log.d("LOCATION", "location is null");
                }

                if (editReasonView.getText().toString().isEmpty()) {
                    moodEvent.setDescription("");
                } else {
                    moodEvent.setDescription(editReasonView.getText().toString());
                }

                if (!moodSet) {
                    // was adding a new mood
                    user.addMood(moodEvent);
                } else {
                    // was editing
                    user.editMood(moodEvent, moodEventListIndex);
                }
                MainActivity main = (MainActivity) EditFragment.this.getActivity();
                MoodListFragment moodList = MoodListFragment.newInstance(MoodListFragment.OWN_MOODS);
                main.setMainFragment(moodList);
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

    /**
     * The onItemClick listener for the EmotionalState selector gridview. Callback invoked
     * when an item in the AdapterView has been clicked.
     *
     * @param   adapterView
     *      An AdapterView that was clicked on in the fragment.
     * @param   view
     *      The view within the AdapterView that was clicked.
     * @param   i
     *      The position of the view in the adapter.
     * @param   l
     *      The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.state_grid_view) {
            emotionalState = new EmotionalState(stateKeys.get(i));
            // update the state text view
            stateTextView.setText(emotionalState.getEmotion());
            // a mood has been selected so all required fields have been set; allow submitting MoodEvent
            buttonSubmitView.setEnabled(true);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
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
     * If your app doesn't already have the permission it needs, the app must call one of the requestPermissions()
     * methods to request the appropriate permissions. Your app passes the permissions it wants and an integer
     * request code that you specify to identify this permission request. This method functions asynchronously.
     * It returns right away, and after the user responds to the prompt, the system calls the app's callback
     * method with the results, passing the same request code that the app passed to requestPermissions().
     */
    private void requestPermissions() {
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
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
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
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startLocationUpdates();
            } else {
                // Permission denied.

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
                Log.d("LOCATION", "updated");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        locationSwitch.setEnabled(false); // make switch unclickable
                        break;
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

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
                                locationSwitch.setEnabled(false); // make switch unclickable
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


}
