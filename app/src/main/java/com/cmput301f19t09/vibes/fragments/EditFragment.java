package com.cmput301f19t09.vibes;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
public class EditFragment extends Fragment {
    // the fragment initialization parameters
    public static final String VIBES_MOODEVENT = "com.cmput301f19t09.vibes.MOODEVENT";
    private MoodEvent moodEvent;
    private boolean moodSet;

    private EditText editDateView;
    private EditText editTimeView;
    private EditText editStateView;
    private EditText editContextView;
    private EditText editLocationView;
    private EditText editReasonView;

    private OnFragmentInteractionListener mListener;

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
    // TODO: Rename and change types and number of parameters
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
        }
        else { // we didn't pass a MoodEvent so we are creating a new one
            moodSet = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        editDateView = view.findViewById(R.id.edit_date_view);
        editTimeView = view.findViewById(R.id.edit_time_view);
        editStateView = view.findViewById(R.id.edit_state_view);
        editContextView = view.findViewById(R.id.edit_context_view);
        editLocationView = view.findViewById(R.id.edit_location_view);
        editReasonView = view.findViewById(R.id.edit_reason_view);

        if (moodSet) {
            // populate the EditText's with the MoodEvent attributes
        }
        else {
            // EditText's empty
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
