package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observer;

/*
    This class is responsible for displ
 */
public abstract class MoodListAdapter extends ArrayAdapter<MoodEvent> implements Observer
{
    protected List<MoodEvent> data;
    protected User user;
    private Context context;

    public MoodListAdapter(Context context, User user)
    {
        super(context, 0);

        this.context = context;
        this.user = user;
        this.data = new ArrayList<MoodEvent>();

        initialize();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Log.d("MoodListAdapter", "Creating a View for a MoodItem");
        View item = convertView;

        if (item == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.mood_list_item, null);
        }

        //User user = data.get(position).user;
        //MoodEvent event = data.get(position).event;

        MoodEvent event = data.get(position);

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

        emotionImage = item.findViewById(R.id.emotion_image);
        userFullName = item.findViewById(R.id.full_name);
        moodReason = item.findViewById(R.id.reason);
        moodTime = item.findViewById(R.id.mood_time);
        //emotionColour = item.findViewById(R.id.emotion_colour);

        userFullName.setText(user.getFirstName() + " " + user.getLastName());

        if (event != null)
        {

            moodReason.setText(event.getDescription());
            emotionImage.setImageResource(event.getState().getImageFile());
            emotionImage.setClipToOutline(true);
            Log.d("TEST", String.format("Setting emotion colour to %x", event.getState().getColour()));
            emotionImage.setColorFilter(event.getState().getColour());
            //emotionColour.setBackgroundResource(R.drawable.circle);
            //emotionColour.setBackgroundColor(Color.parseColor(event.getState().getColour()));

            //Duration timeSincePost = data.get(position).timeSinceEvent();

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

        }
        else
        {
            moodReason.setText("");
            moodTime.setText("");
        }

        return item;
    }

    private void initialize()
    {
        UserManager.addUserObserver(user.getUserName(), this);
        initializeData();
    }

    public void initializeData() { }

    public abstract void refreshData();

    public void destroy()
    {
        UserManager.removeUserObserver(user.getUserName(), this);
    }
}
