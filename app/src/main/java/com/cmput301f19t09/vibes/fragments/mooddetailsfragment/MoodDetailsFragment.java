package com.cmput301f19t09.vibes.fragments.mooddetailsfragment;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.Duration;
import java.time.LocalDateTime;

import io.opencensus.resource.Resource;

/**
 * This fragment shows the details of a MoodEvent. It is similar to MoodDetailsDialogFragment, however
 * MoodEvents are not editable/deletable and it is not a dialog
 */
public class MoodDetailsFragment extends Fragment
{
    /**
     * Create a new instance of the Fragment for the specified event
     */
    public static MoodDetailsFragment newInstance(MoodEvent event)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);

        MoodDetailsFragment fragment = new MoodDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Inflate the view for this Fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.mood_details, null);
        return view;
    }

    /**
     * Once the view is created, populate the fields with the details of the given MoodEventj
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        MoodEvent event = (MoodEvent) arguments.getSerializable("event");
        setMoodEvent(event);
    }

    /**
     * Set the fields of the view to show the details of the given MoodEvent. If the event is null, then
     * set the visibility of the View to GONE, so that it is not shown
     * @param event
     */
    public void setMoodEvent(MoodEvent event)
    {
        View view = getView();

        // Save the event (in case it is different than initially provided)
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);

        setArguments(bundle);

        if (view == null)
        {
            return;
        }

        if (event == null)
        {
            view.setVisibility(View.GONE);
            return;
        }

        User event_user = event.getUser();

        ImageView userImage, emotionImage, reasonImage;
        TextView userUsername, userFullName, moodTime, moodDate, moodReason, emotionChip, socialChip;
        Button editButton, confirmButton;
        ImageButton deleteButton;
        ConstraintLayout detailsContainer;

        userImage = view.findViewById(R.id.user_image);

        emotionImage =  view.findViewById(R.id.emotion_image);
        reasonImage = view.findViewById(R.id.reason_image);
        userUsername =  view.findViewById(R.id.username);
        userFullName = view.findViewById(R.id.full_name);
        moodTime = view.findViewById(R.id.mood_time);
        moodDate = view.findViewById(R.id.mood_date);
        moodReason = view.findViewById(R.id.reason);
        editButton = view.findViewById(R.id.edit_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        deleteButton = view.findViewById(R.id.delete_button);
        detailsContainer = view.findViewById(R.id.mood_details_root);
        emotionChip = view.findViewById(R.id.emotion_chip);
        socialChip = view.findViewById(R.id.social_chip);

        editButton.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        Glide.with(getContext()).load(event_user.getProfileURL()).into(userImage);

        EmotionalState state = event.getState();

        emotionImage.setImageResource(state.getImageFile());

        emotionChip.setBackgroundTintList(ColorStateList.valueOf(state.getColour()));

        String emotionName = state.getEmotion();
        emotionChip.setText(emotionName.charAt(0) + emotionName.substring(1).toLowerCase());


        //Sets the photo to the image specified by the event's photo (type uri)
        Uri photoUri = event.getPhoto();
        if (photoUri != null){
            Glide.with(this).load(photoUri).into(reasonImage);
            reasonImage.setVisibility(View.VISIBLE);
        } else {
            reasonImage.setVisibility(View.GONE);
        }

        // If the MoodEvent has a social situation, set the social situation view to show that situation,
        // otherwise, don't show the view
        int situation = event.getSocialSituation();
        if (situation >= 0)
        {
            socialChip.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), android.R.color.darker_gray, null)));
            String[] socialSituations = getResources().getStringArray(R.array.situations);

            // Since mood events were created before SocialSituations were constrained, this accounts for any that
            // might be set to incorrect values
            situation %= socialSituations.length;
            socialChip.setText(socialSituations[situation]);
        }
        else
        {
            socialChip.setVisibility(View.GONE);
        }

        userImage.setClipToOutline(true);
        reasonImage.setClipToOutline(true);

        userUsername.setText(event_user.getUserName());
        userFullName.setText(event_user.getFirstName() + " " + event_user.getLastName());

        Glide.with(getContext()).load(event.getPhoto()).into(reasonImage);

        moodTime.setText(event.getTimeString());
        moodDate.setText(event.getDateString());
        moodReason.setText(event.getDescription());

        detailsContainer.setBackgroundResource(R.drawable.mood_details_drop_bg);
    }
}
