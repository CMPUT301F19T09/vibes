package com.cmput301f19t09.vibes.fragments.editfragment;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListFragment;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.MoodEvent;

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
public class EditFragment extends Fragment implements AdapterView.OnItemClickListener {

    // the fragment initialization parameters
    public static final String VIBES_MOODEVENT = "com.cmput301f19t09.vibes.MOODEVENT";
    private MoodEvent moodEvent;
    private boolean moodSet;
    public static final String VIBES_USER = "com.cmput301f19t09.vibes.USER";
    private User user;

    private TextView dateTextView;
    private TextView timeTextView;
    private GridView stateGridView;
    private TextView stateTextView;
    private ArrayList<String> stateKeys = EmotionalState.getListOfKeys();
    private EmotionalState emotionalState = null;
    private EditText editSituationView;
    private EditText editReasonView;
    private Button buttonSubmitView;
    private Button buttonCancelView;

    private OnFragmentInteractionListener mListener;

    // location services
    private Location location;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @param
     * @return A new instance of fragment EditFragment.
     */
    public static EditFragment newInstance(MoodEvent moodEvent, User user) {
        // called when we pass in a MoodEvent
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(VIBES_MOODEVENT, moodEvent);
        args.putSerializable(VIBES_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditFragment newInstance(User user) {
        // called when we don't pass in a MoodEvent
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(VIBES_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(VIBES_USER);
            moodEvent = (MoodEvent) getArguments().getSerializable(VIBES_MOODEVENT);

            if (moodEvent != null) { // a MoodEvent was passed so set fields based on it
                moodSet = true;
            }
            else { // we didn't pass a MoodEvent so we are creating a new one
                moodSet = false;
            }

        }
        else {
            Log.d("info", "the bundle args was null");
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

        location = new Location(""); // empty provider when not pulling from a location service

        if (moodSet) {
            // populate the EditText's with the MoodEvent attributes; we are editing an existing MoodEvent

            dateTextView.setText(moodEvent.getDateString());
            timeTextView.setText(moodEvent.getTimeString());
            editSituationView.setText(moodEvent.getSocialSituation());
            editReasonView.setText(moodEvent.getDescription());
        }
        else {
            // don't prepopulate the EditText's; we are creating a new MoodEvent
            // set moodEvent to be an empty new MoodEvent object
            moodEvent = new MoodEvent(null, null, null, null, 0, null, null);

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
            location.setLatitude(53.5461);
            location.setLongitude(-113.4938);
        }

        buttonSubmitView.setOnClickListener(view1 -> {
            moodEvent.setState(emotionalState);
            if (!editSituationView.getText().toString().isEmpty()) {
                moodEvent.setSocialSituation(Integer.parseInt(editSituationView.getText().toString()));
            }
            moodEvent.setLocation(location);
            moodEvent.setDescription(editReasonView.getText().toString());

            if (!moodSet) {
                // was adding a new mood
                user.addMood(moodEvent);
            }
            else {
                // was editing
                // TODO: replace the moodEvent which was clicked on by using its index
                user.addMood(moodEvent);
            }
            MainActivity main = (MainActivity) getActivity();
            MoodListFragment moodList = MoodListFragment.newInstance(user, 0);
            main.setMainFragment(moodList);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.state_grid_view) {
            emotionalState = new EmotionalState(stateKeys.get(i));
            // update the state text view
            stateTextView.setText(emotionalState.getEmotion());
            checkRequiredFields();
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

    private void checkRequiredFields() {
        // check if date and time have been set valid and no other field (except comment) is empty
        if (emotionalState != null) {

            // then enable button
            buttonSubmitView.setEnabled(true);
        }
        else {
            // disable button
            buttonSubmitView.setEnabled(false);
        }
    }

}
