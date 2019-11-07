package com.cmput301f19t09.vibes.fragments.mooddetailsfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.cmput301f19t09.vibes.fragments.EditFragment;
import com.cmput301f19t09.vibes.models.MoodItem;

import java.time.Duration;

public class MoodDetailsDialogFragment extends DialogFragment
{
    private MoodItem moodItem;

    public static MoodDetailsDialogFragment newInstance(MoodItem moodItem, boolean editable)
    {
        Bundle bundle = new Bundle();
        MoodDetailsDialogFragment fragment = new MoodDetailsDialogFragment();

        bundle.putSerializable("item", moodItem);
        bundle.putBoolean("editable", editable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        Bundle arguments = getArguments();

        moodItem = (MoodItem) arguments.getSerializable("item");
        boolean editable = arguments.getBoolean("editable");

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

        Glide.with(getContext()).load(moodItem.user.getProfileURL()).into(userImage);
        //Glide.with(getContext()).load(moodItem.event.getReasonImage()).into(reasonImage);
        //Glide.with(getContext()).load(moodItem.event.getEmotion().getImage()).into(emotionImage);

        userUsername.setText(moodItem.user.getUserName());
        userFullName.setText(moodItem.user.getFirstName() + " " + moodItem.user.getLastName());

        Duration timeSincePost = moodItem.timeSinceEvent();

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
        moodReason.setText(moodItem.event.getDescription());

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
                moodItem.user.deleteMood(moodItem.getIndex());

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
                        ((MainActivity) getActivity()).setMainFragment(EditFragment.newInstance(moodItem.event));
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

        return dialog;
    }
}
