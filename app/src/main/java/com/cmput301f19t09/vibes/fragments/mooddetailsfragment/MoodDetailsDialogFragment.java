package com.cmput301f19t09.vibes.fragments.mooddetailsfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.fragments.editfragment.EditFragment;

import java.time.Duration;
import java.time.LocalDateTime;

public class MoodDetailsDialogFragment extends DialogFragment
{
    private MoodEvent event;
    private User user;
    private User eventUser;
    private boolean editable;

    public static MoodDetailsDialogFragment newInstance(MoodEvent event, User user, boolean editable)
    {
        Bundle bundle = new Bundle();
        MoodDetailsDialogFragment fragment = new MoodDetailsDialogFragment();

        bundle.putSerializable("event", event);
        bundle.putSerializable("user", user);
        bundle.putBoolean("editable", editable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        Bundle arguments = getArguments();

        event = (MoodEvent) arguments.getSerializable("event");
        user = (User) arguments.getSerializable("user");
        eventUser = event.getUser();
        editable = arguments.getBoolean("editable");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.mood_details, null);

        ImageView userImage, emotionImage, reasonImage;
        TextView userUsername, userFullName, moodTime, moodReason;
        Button editButton, confirmButton;
        ImageButton deleteButton;

        userImage = view.findViewById(R.id.user_image);
        emotionImage =  view.findViewById(R.id.emotion_image);
        reasonImage = view.findViewById(R.id.reason_image);
        userUsername =  view.findViewById(R.id.username);
        userFullName = view.findViewById(R.id.full_name);
        moodTime = view.findViewById(R.id.mood_time);
        moodReason = view.findViewById(R.id.reason);
        deleteButton = view.findViewById(R.id.delete_button);
        editButton = view.findViewById(R.id.edit_button);
        confirmButton = view.findViewById(R.id.confirm_button);

        Glide.with(getContext()).load(eventUser.getProfileURL()).into(userImage);
        emotionImage.setImageResource(event.getState().getImageFile());
        emotionImage.setColorFilter(event.getState().getColour());

        userImage.setClipToOutline(true);
        emotionImage.setClipToOutline(true);
        deleteButton.setImageResource(R.drawable.ic_delete_white_24dp);

        userUsername.setText(eventUser.getUserName());
        userFullName.setText(eventUser.getFirstName() + " " + eventUser.getLastName());

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

        if (editable)
        {
            editButton.setVisibility(View.VISIBLE);
        }
        else
        {
            editButton.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Dialog dialog = builder.setView(view)
                .create();

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //moodItem.user.deleteMood(moodItem.getIndex());
                int mood_index = eventUser.getMoodEvents().indexOf(event);
                Log.d("TEST", "Trying to remove event from user " + eventUser.getUserName());
                Log.d("TEST", "Trying to remove mood with index " + mood_index);
                eventUser.deleteMood(mood_index);

                dismiss();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialog)
                    {
                        ((MainActivity) getActivity()).setMainFragment(EditFragment.newInstance(event,eventUser));
                    }
                });

                dialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        userImage.setOnClickListener((View v) ->
                {
                    dialog.setOnDismissListener((DialogInterface d) ->
                    {
                        ProfileFragment profileFragment;

                        if (user == eventUser)
                        {
                            profileFragment = ProfileFragment.newInstance(user);
                        }
                        else
                        {
                            profileFragment = ProfileFragment.newInstance(user, eventUser);
                        }
                        ((MainActivity) getActivity()).setMainFragment(profileFragment);
                    });

                    dialog.dismiss();
                });

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.circle);

        return dialog;
    }
}
