package com.cmput301f19t09.vibes.fragments.mooddetailsfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

/**
 * This fragment opens a Dialog that shows a MoodEvent. If the event belongs to the current user, then
 * it is editable and the user can delete it from the database. Clicking on the user's profile picture
 * will open the profile (ProfileFragment) of that user
 */
public class MoodDetailsDialogFragment extends DialogFragment {
    private MoodEvent event;
    private User user;
    private User eventUser;
    private boolean editable;


    /**
     * Create a new instance of MoodDetailsDialogFragment to show the given event. If editable is true,
     * then the user is able edit or delete the event
     *
     * @param event    The event to show
     * @param editable True -> The event is editable, False -> The event is not editable
     * @return A MoodDetailsDialogFragment with the given arguments
     */
    public static MoodDetailsDialogFragment newInstance(MoodEvent event, boolean editable) {
        Bundle bundle = new Bundle();
        MoodDetailsDialogFragment fragment = new MoodDetailsDialogFragment();

        bundle.putSerializable("event", event);
        bundle.putBoolean("editable", editable);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Create the Dialog and populate its fields with the MoodEvent's details
     *
     * @param savedInstanceState
     * @return A Dialog showing the details of the MoodEvent given to the class
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.mood_details, null);

        event = (MoodEvent) arguments.getSerializable("event");
        user = UserManager.getCurrentUser(); // The current user of the app
        eventUser = event.getUser();         // The user who posted the event
        editable = arguments.getBoolean("editable");

        ImageView userImage, emotionImage, reasonImage;
        TextView userUsername, userFullName, moodTime, moodDate, moodReason, emotionChip, socialChip;
        Button editButton, confirmButton;
        ImageButton deleteButton;

        userImage = view.findViewById(R.id.user_image);
        emotionImage = view.findViewById(R.id.emotion_image);
        reasonImage = view.findViewById(R.id.reason_image);
        userUsername = view.findViewById(R.id.username);
        userFullName = view.findViewById(R.id.full_name);
        moodTime = view.findViewById(R.id.mood_time);
        moodDate = view.findViewById(R.id.mood_date);
        moodReason = view.findViewById(R.id.reason);
        deleteButton = view.findViewById(R.id.delete_button);
        editButton = view.findViewById(R.id.edit_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        emotionChip = view.findViewById(R.id.emotion_chip);
        socialChip = view.findViewById(R.id.social_chip);

        // Set profile picture and crop to a circle
        Glide.with(getContext()).load(eventUser.getProfileURL()).into(userImage);
        userImage.setClipToOutline(true);
        EmotionalState state = event.getState();

        emotionImage.setImageResource(state.getImageFile());

        emotionChip.setBackgroundTintList(ColorStateList.valueOf(state.getColour()));
        String emotionName = state.getEmotion();
        emotionChip.setText(emotionName.charAt(0) + emotionName.substring(1).toLowerCase());

        int situation = event.getSocialSituation();

        // If the MoodEvent has a social situation, set the social situation view to show that situation,
        // otherwise, don't show the view
        if (situation != -1) {
            socialChip.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), android.R.color.darker_gray, null)));
            String[] socialSituations = getResources().getStringArray(R.array.situations);
            situation %= socialSituations.length;
            socialChip.setText(socialSituations[situation]);
        } else {
            socialChip.setVisibility(View.GONE);
        }

        // Set emotion picture and colour
        emotionImage.setImageResource(event.getState().getImageFile());
        emotionImage.setClipToOutline(true);

        deleteButton.setImageResource(R.drawable.ic_delete_white_24dp);

        // Set the username and user's name fields
        userUsername.setText(eventUser.getUserName());
        userFullName.setText(eventUser.getFirstName() + " " + eventUser.getLastName());

        // Calculate the time since the event was posted

        //Sets the photo to the image specified by the event's photo (type uri)
        Uri photoUri = event.getPhoto();
        if (photoUri != null) {
            Glide.with(this).load(photoUri).into(reasonImage);
            reasonImage.setClipToOutline(true);
            reasonImage.setVisibility(View.VISIBLE);
        } else {
            reasonImage.setVisibility(View.GONE);
        }

        moodReason.setText(event.getDescription());
        moodTime.setText(event.getTimeString());
        moodDate.setText(event.getDateString());

        if (editable) {
            editButton.setVisibility(View.VISIBLE);
        } else {
            // If the MoodEvent is not editable, disable the editButton, this also disables the delte
            // button
            editButton.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Dialog dialog = builder.setView(view)
                .create();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calculate the index of this event within the user's list of events
                int mood_index = eventUser.getMoodEvents().indexOf(event);

                // If the event doesn't exist in the user, return (i.e. the event has been deleted since
                // the fragment was opened
                if (mood_index == -1) {
                    return;
                }

                eventUser.deleteMood(mood_index);

                dismiss();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int mood_index = eventUser.getMoodEvents().indexOf(event);
                if (mood_index == -1) {
                    return;
                }

                ((MainActivity) getActivity()).setEditFragment(event, mood_index);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*
                    When the dialog has closed, open a ProfileFragment for the Event's user,
                     */
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface d) {
                        ProfileFragment profileFragment;

                        if (user == eventUser) {
                            // Open own profile
                            ((MainActivity) MoodDetailsDialogFragment.this.getActivity()).setProfileFragment();
                        } else {
                            // Open other user's profile
                            ((MainActivity) MoodDetailsDialogFragment.this.getActivity()).setProfileFragment(eventUser.getUid());
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.circle);

        return dialog;
    }
}
