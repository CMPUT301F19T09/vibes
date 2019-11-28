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

/*
    A fragment that shows the details of a specified MoodEvent
 */
public class MoodDetailsFragment extends Fragment
{
    /**
        Create a new instance of the Fragment for the specified event
     */
    public static MoodDetailsFragment newInstance(MoodEvent event)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);

        MoodDetailsFragment fragment = new MoodDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Bundle arguments = getArguments();

        View view = inflater.inflate(R.layout.mood_details, null);

        MoodEvent event = (MoodEvent) arguments.getSerializable("event");

        /*
        TODO: Add this check somewhere else
        if a null event was provided, don't show anything
         */
        if (event == null)
        {
            view.setVisibility(View.GONE);
            return view;
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
        emotionChip.setText(state.getEmotion());


        //Sets the photo to the image specified by the event's photo (type uri)
        Uri photoUri = event.getPhoto();
        if (photoUri != null){
            Glide.with(this).load(photoUri).into(reasonImage);
            reasonImage.setVisibility(View.VISIBLE);
        } else {
            reasonImage.setVisibility(View.GONE);
        }

        int situation = event.getSocialSituation();
        if (situation != -1)
        {
            socialChip.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), android.R.color.darker_gray, null)));
            String[] socialSituations = getResources().getStringArray(R.array.situations);
            situation %= socialSituations.length;
            socialChip.setText(socialSituations[situation]);
        }
        else
        {
            socialChip.setVisibility(View.GONE);
        }

        userImage.setClipToOutline(true);

        userUsername.setText(event_user.getUserName());
        userFullName.setText(event_user.getFirstName() + " " + event_user.getLastName());

        Glide.with(this).load(event.getPhoto()).into(reasonImage);

        //moodTime.setText(MoodTimeAdapter.timeSince(event));
        moodTime.setText(event.getTimeString());
        moodDate.setText(event.getDateString());
        moodReason.setText(event.getDescription());

        detailsContainer.setBackgroundResource(R.drawable.mood_details_drop_bg);

        return view;
    }
}
