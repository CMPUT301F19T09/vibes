package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class is used as an adapter for the list dialog
 * in the clusters in the map screen. When there are overlapping
 * MoodEvents, this adapter is used to render them in a new dialog.
 */
public class MoodsDialogAdapter extends ArrayAdapter<MoodEvent> {
    private ArrayList<MoodEvent> eventList;
    private Context context;

    /**
     * Create a MoodsDialogAdapter
     * @param context
     * @param events
     */
    public MoodsDialogAdapter(Context context, ArrayList<MoodEvent> events){
        super(context, 0, events);
        this.eventList = events;
        this.context = context;
    }

    /**
     * This function is used to render the rows.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;

        if (item == null){
            item = LayoutInflater.from(context).inflate(R.layout.mood_list_item, parent, false);
        }

        // For user at position in list
        final MoodEvent event = eventList.get(position);

        if (event == null)
        {
            return item;
        }

        User user = event.getUser();

        ImageView userImage, emotionImage, userMask;
        TextView userUsername, userFullName, moodReason, moodTime;
        View emotionColour;

        userImage = item.findViewById(R.id.user_image);
        Glide.with(getContext()).load(user.getProfileURL()).into(userImage);

        userImage.setClipToOutline(true);

        // Setting the layout fields with the corresponding
        // variables in the class.
        emotionImage = item.findViewById(R.id.emotion_image);
        userFullName = item.findViewById(R.id.full_name);
        moodReason = item.findViewById(R.id.reason);
        moodTime = item.findViewById(R.id.mood_time);

        userFullName.setText(user.getFirstName() + " " + user.getLastName());

        // Setting the fields with their corresponding values
        if (event != null)
        {

            moodReason.setText(event.getDescription());
            emotionImage.setImageResource(event.getState().getImageFile());
            emotionImage.setClipToOutline(true);
            Log.d("TEST", String.format("Setting emotion colour to %x", event.getState().getColour()));
            emotionImage.setColorFilter(event.getState().getColour());

            Duration timeSincePost = Duration.between(event.getLocalDateTime(), LocalDateTime.now());

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

        }
        else
        {
            moodReason.setText("");
            moodTime.setText("");
        }

        return item;
    }
}
