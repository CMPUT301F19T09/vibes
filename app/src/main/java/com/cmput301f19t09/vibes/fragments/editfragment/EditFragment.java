package com.cmput301f19t09.vibes.fragments.editfragment;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.cmput301f19t09.vibes.models.UserManager;

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
    private Location location;
    private Switch locationSwitch;
    private boolean useLocation;

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

        if (getArguments() != null) { // a MoodEvent was passed so set fields based on it
            moodEvent = (MoodEvent) getArguments().getSerializable(VIBES_MOODEVENT);
            moodEventListIndex = (int) getArguments().getInt(VIBES_INDEX);
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

        location = new Location(""); // empty provider when not pulling from a location service
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
            // TODO: only set location if switch is set
            location.setLatitude(53.5461);
            location.setLongitude(-113.4938);

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

        buttonSubmitView.setOnClickListener(view1 -> {
            moodEvent.setState(emotionalState);

            // set optional fields
            if (!editSituationView.getText().toString().isEmpty()) {
                moodEvent.setSocialSituation(Integer.parseInt(editSituationView.getText().toString()));
            }

            if (useLocation) {
                moodEvent.setLocation(location);
            }
            else {
                moodEvent.setLocation(null);
            }

            if (editReasonView.getText().toString().isEmpty()){
                moodEvent.setDescription("");
            }
            else {
                moodEvent.setDescription(editReasonView.getText().toString());
            }

            if (!moodSet) {
                // was adding a new mood
                user.addMood(moodEvent);
            }
            else {
                // was editing
                user.editMood(moodEvent, moodEventListIndex);
            }
            MainActivity main = (MainActivity) getActivity();
            MoodListFragment moodList = MoodListFragment.newInstance(MoodListFragment.OWN_MOODS);
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

}
