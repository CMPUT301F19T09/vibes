package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an abstract class responsible for managing a list of MoodEvents. The specifics of which MoodEvents
 * to load, and how to display them are delegated to the subclass
 */
public abstract class MoodListAdapter extends ArrayAdapter<MoodEvent> {
    protected List<MoodEvent> data; // The list of MoodEvents to be shown
    protected String filter;        // A specific emotion to filter, or null to show all
    private Context context;        // The application context

    /**
     * Constructor for a MoodListAdapter
     *
     * @param context The application context that the adapter is created in
     */
    public MoodListAdapter(Context context) {
        super(context, 0);

        this.context = context;
        this.data = new ArrayList<MoodEvent>();
    }

    /**
     * This inflates a View representing a given MoodEvent
     *
     * @param position
     * @param convertView
     * @param parent
     * @return A View representign the MoodEvent at data[position]
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;

        // If the View cannot be reused from another event, create a new one
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.mood_list_item, null);
        }

        MoodEvent event = data.get(position);

        if (event == null) {
            return item;
        }

        // Display the details of the User who posted the event
        User user = event.getUser();
        EmotionalState state = event.getState();

        ImageView userImage, emotionImage;
        TextView userFullName, moodReason, moodTime, emotionTag;

        userImage = item.findViewById(R.id.user_image);
        emotionImage = item.findViewById(R.id.emotion_image);
        userFullName = item.findViewById(R.id.full_name);
        moodReason = item.findViewById(R.id.reason);
        moodTime = item.findViewById(R.id.mood_time);
        emotionTag = item.findViewById(R.id.emotion_chip);

        // Clip the User's image into a circle and load the image
        userImage.setClipToOutline(true);
        Glide.with(getContext()).load(user.getProfileURL()).into(userImage);

        userFullName.setText(user.getFirstName() + " " + user.getLastName());

        moodReason.setText(event.getDescription());

        emotionImage.setImageResource(state.getImageFile());

        String emotionName = state.getEmotion();
        emotionTag.setText(emotionName.charAt(0) + emotionName.substring(1).toLowerCase());
        emotionTag.setBackgroundTintList(ColorStateList.valueOf(state.getColour()));

        moodTime.setText("~" + getRelativeTimeString(event.getLocalDateTime()));

        return item;
    }

    /**
     * Creates a String representing the time between the given LocalDateTime and now. For example:
     * "5 s", "12 h", or "100 d" for seconds, hours, or days
     *
     * @param dateTime The LocalDateTime that your are comparing against
     * @return A formatted string representing the time since dateTime
     */
    private String getRelativeTimeString(LocalDateTime dateTime) {
        // Get the time elapsed since dateTime
        Duration timeSincePost = Duration.between(dateTime, LocalDateTime.now());

        String result;

        // If dateTime is after now, return this. Due to LocalDateTime handling timezones for us, this should not happen
        if (timeSincePost.isNegative()) {
            return "***FUTURE***";
        }

        // Determine the formatting of the result
        if (timeSincePost.getSeconds() < 60) {
            result = String.format("%2d s", timeSincePost.getSeconds());
        } else if (timeSincePost.toMinutes() < 60) {
            result = String.format("%2d m", timeSincePost.toMinutes());
        } else if (timeSincePost.toHours() < 24) {
            result = String.format("%2d h", timeSincePost.toHours());
        } else if (timeSincePost.toDays() < 365) {
            result = String.format("%d d", timeSincePost.toDays());
        } else {
            result = String.format("%d y", timeSincePost.toDays() / 365);
        }

        return result;
    }

    /**
     * This can be used if the subclass needs to be aware of its parent's lifecycle
     */
    public void resume() {
    }

    /**
     * This can be used if the subclass needs to be aware of its parent's lifecycle
     */
    public void pause() {
    }

    /**
     * This is where logic for populating the data set should go
     */
    public abstract void refreshData();

    /**
     * Set the filter to a certain EmotionalState
     *
     * @param filter The key of the EmotionalState to filter for
     */
    public void setFilter(String filter) {
        this.filter = filter;
        refreshData();
    }
}
