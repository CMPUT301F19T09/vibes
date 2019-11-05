package com.cmput301f19t09.vibes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 * TODO: make the context and state a drop down.
 * TODO: handle limiting the reason
 * TODO: pull coordinates from gps
 */
public class EditFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // the fragment initialization parameters
    public static final String VIBES_MOODEVENT = "com.cmput301f19t09.vibes.MOODEVENT";
    private MoodEvent moodEvent;
    private boolean moodSet;

    private TextView dateTextView;
    private TextView timeTextView;
    private EditText editStateView;
    private EditText editContextView;
    private TextView locationTextView;
    private EditText editReasonView;
    private Button buttonSubmitView;
    private Button buttonCancelView;

    private OnFragmentInteractionListener mListener;

    // location services
    // https://demonuts.com/current-gps-location/
    // https://www.toptal.com/android/android-developers-guide-to-google-location-services-api
    // https://www.vogella.com/tutorials/AndroidLocationAPI/article.html
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 100;
    private GoogleApiClient googleApiClient;
    LocationManager locationManager;
    private Location location;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    public static EditFragment newInstance(MoodEvent moodEvent) {
        // called when we pass in a MoodEvent
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(VIBES_MOODEVENT, moodEvent);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditFragment newInstance() {
        // called when we don't pass in a MoodEvent
        EditFragment fragment = new EditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) { // we passed a MoodEvent so set fields based on it
            moodEvent = (MoodEvent) getArguments().getSerializable(VIBES_MOODEVENT);
            moodSet = true;
        } else { // we didn't pass a MoodEvent so we are creating a new one
            moodSet = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        // enable buttons
        buttonSubmitView = view.findViewById(R.id.button_submit_view);
        buttonCancelView = view.findViewById(R.id.button_cancel_view);
        buttonSubmitView.setEnabled(true);
        buttonCancelView.setEnabled(true);

        dateTextView = view.findViewById(R.id.date_text_view);
        timeTextView = view.findViewById(R.id.time_text_view);
        editStateView = view.findViewById(R.id.edit_state_view);
        editContextView = view.findViewById(R.id.edit_context_view);
        locationTextView = view.findViewById(R.id.location_text_view);
        editReasonView = view.findViewById(R.id.edit_reason_view);

        if (moodSet) {
            // populate the EditText's with the MoodEvent attributes; we are editing an existing MoodEvent

            dateTextView.setText(moodEvent.getDateString());
            timeTextView.setText(moodEvent.getTimeString());
            editStateView.setText(moodEvent.getState().getEmotion());
            editContextView.setText(moodEvent.getSocialSituation());
            locationTextView.setText(moodEvent.getLocationString());
            editReasonView.setText(moodEvent.getDescription());
        } else {
            // don't prepopulate the EditText's; we are creating a new MoodEvent

            // set the current date
            LocalDate date = LocalDate.now();
            dateTextView.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));

            // set the current time
            LocalTime time = LocalTime.now();
            timeTextView.setText(time.format(DateTimeFormatter.ISO_LOCAL_TIME));

            // TODO: fix location handling
            String tmp = "IMPLEMENT ME"
            locationTextView.setText(tmp);
//            // get the current location from GPS
//            googleApiClient = new GoogleApiClient.Builder(getActivity())
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//
//            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//            // set criteria for how to select location provider
//            Criteria criteria = new Criteria();


//                    if (location != null) {
//                        String tmp = Location.convert(
//                                location.getLatitude(), Location.FORMAT_DEGREES)
//                                + " "
//                                + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES
//                        );
//                        // set the current location
//                        locationTextView.setText(tmp);

        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
