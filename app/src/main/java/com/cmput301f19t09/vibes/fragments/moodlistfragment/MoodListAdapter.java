package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/*
    This class is responsible for displaying a list of MoodEvents, the functionality of loading events
    is handled by the subclass
 */
public abstract class MoodListAdapter extends ArrayAdapter<MoodEvent> implements Observer
{
    protected List<MoodEvent> data;
    protected User user;
    protected String filter;
    private Context context;

    /*
    Create a MoodListAdapter with the context
    @param context
        the context
     */
    public MoodListAdapter(Context context)
    {
        super(context, 0);

        this.context = context;
        this.user = UserManager.getCurrentUser();
        this.data = new ArrayList<MoodEvent>();

        initialize();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
//        Log.d("MoodListAdapter", "Creating a View for a MoodItem");
        View item = convertView;

        /*
        if the view is null, create a new one
         */
        if (item == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.mood_list_item, null);
        }

        MoodEvent event = data.get(position);

        if (event == null)
        {
            return item;
        }

        User user = event.getUser();

        ImageView userImage, emotionImage, userMask;
        TextView userUsername, userFullName, moodReason, moodTime, emotionTag;
        View emotionColour;

        userImage = item.findViewById(R.id.user_image);
        Glide.with(getContext()).load(user.getProfileURL()).into(userImage);

        userImage.setClipToOutline(true);

        emotionImage = item.findViewById(R.id.emotion_image);
        userFullName = item.findViewById(R.id.full_name);
        moodReason = item.findViewById(R.id.reason);
        moodTime = item.findViewById(R.id.mood_time);
        emotionTag = item.findViewById(R.id.emotion_chip);

        userFullName.setText(user.getFirstName() + " " + user.getLastName());

        if (event != null)
        {

            EmotionalState state = event.getState();
            moodReason.setText(event.getDescription());
            emotionImage.setImageResource(state.getImageFile());

            Duration timeSincePost = Duration.between(event.getLocalDateTime(), LocalDateTime.now());

            emotionTag.setText(state.getEmotion());
            //emotionTag.setText(state.getEmotion().charAt(0) + state.getEmotion().substring(1).toLowerCase());
            //emotionTag.setBackgroundColor(state.getColour());
            emotionTag.setBackgroundTintList(ColorStateList.valueOf(state.getColour()));

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

    /*
    Add this class as an observer to the main user and call the initializeData fucntion
     */
    private void initialize()
    {
        UserManager.addUserObserver(user.getUid(), this);
        initializeData();
    }

    public void initializeData() { }

    public abstract void refreshData();

    // When this is called, remove this object as an observer of User
    public void removeObservers()
    {
        UserManager.removeUserObserver(user.getUid(), this);
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
        refreshData();
    }
}
