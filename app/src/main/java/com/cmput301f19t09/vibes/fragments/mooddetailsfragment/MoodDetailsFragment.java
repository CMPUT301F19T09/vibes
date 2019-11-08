package com.cmput301f19t09.vibes.fragments.mooddetailsfragment;

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
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class MoodDetailsFragment extends Fragment
{
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

        if (event == null)
        {
            view.setVisibility(View.GONE);
            return view;
        }

        User event_user = event.getUser();

        ImageView userImage, emotionImage, reasonImage;
        TextView userUsername, userFullName, moodTime, moodReason;
        Button editButton, confirmButton;
        ImageButton deleteButton;
        ConstraintLayout detailsContainer;

        userImage = view.findViewById(R.id.user_image);

        Log.d("TEST", "User image is " + ((userImage == null) ? "" : "not ") + "null");

        emotionImage =  view.findViewById(R.id.emotion_image);
        reasonImage = view.findViewById(R.id.reason_image);
        userUsername =  view.findViewById(R.id.username);
        userFullName = view.findViewById(R.id.full_name);
        moodTime = view.findViewById(R.id.mood_time);
        moodReason = view.findViewById(R.id.reason);
        editButton = view.findViewById(R.id.edit_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        deleteButton = view.findViewById(R.id.delete_button);
        detailsContainer = view.findViewById(R.id.mood_details_root);

        editButton.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        Glide.with(getContext()).load(event_user.getProfileURL()).into(userImage);
        emotionImage.setImageResource(event.getState().getImageFile());
        emotionImage.setColorFilter(event.getState().getColour());

        userImage.setClipToOutline(true);
        emotionImage.setClipToOutline(true);

        userUsername.setText(event_user.getUserName());
        userFullName.setText(event_user.getFirstName() + " " + event_user.getLastName());

        LocalDateTime timeOfPost = LocalDateTime.of(event.getDate(), event.getTime());
        Duration timeSincePost = Duration.between(timeOfPost, LocalDateTime.now());

        String timeString = "~";

        if (timeSincePost.getSeconds() < 60)
        {
            timeString += timeSincePost.getSeconds() + " s";
        }
        else if (timeSincePost.toMinutes() < 60)
        {
            timeString += timeSincePost.toMinutes() + " m";
        }
        else if (timeSincePost.toHours() < 24)
        {
            timeString += timeSincePost.toHours() + " h";
        }
        else if (timeSincePost.toDays() < 365)
        {
            timeString += timeSincePost.toDays() + " d";
        }
        else
        {
            timeString += ( timeSincePost.toDays() / 365 ) + " y";
        }

        moodTime.setText(timeString);
        moodReason.setText(event.getDescription());

        detailsContainer.setBackgroundResource(R.drawable.mood_details_drop_bg);

        return view;
    }
}
