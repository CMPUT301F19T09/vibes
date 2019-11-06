package com.cmput301f19t09.vibes.fragments.moodlistfragment;

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
import com.cmput301f19t09.vibes.models.MoodItem;
import com.cmput301f19t09.vibes.models.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class MoodListAdapter extends ArrayAdapter<MoodItem>
{
    protected List<MoodItem> data;
    protected User user;
    private Context context;

    public MoodListAdapter(Context context, User user)
    {
        //super(context, R.layout.mood_list_item);
        super(context, 0);

        this.context = context;
        this.user = user;
        this.data = new ArrayList<MoodItem>();

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

        ImageView userImage, emotionImage;
        TextView userUsername, userFullName, moodReason, moodTime;

        userImage = item.findViewById(R.id.user_image);
        Glide.with(getContext()).load(user.getProfileURL()).into(userImage);
        emotionImage = item.findViewById(R.id.emotion_image);
        userUsername = item.findViewById(R.id.username);
        userFullName = item.findViewById(R.id.full_name);
        moodReason = item.findViewById(R.id.reason);
        moodTime = item.findViewById(R.id.mood_time);

        User user = data.get(position).user;
        MoodEvent event = data.get(position).event;

        //userImage.setImageBitmap()

        userUsername.setText(user.getUserName());
        userFullName.setText(user.getFirstName() + " " + user.getLastName());

        if (event != null)
        {

            moodReason.setText(event.getDescription());
            //emotionImage.setImageBitmap();

            Duration timeSincePost = data.get(position).timeSinceEvent();
            String timeString = "~";

            if (timeSincePost.getSeconds() < 60)
            {
                timeString += timeSincePost.getSeconds() + " seconds ago";
            }
            else if (timeSincePost.toMinutes() < 60)
            {
                timeString += timeSincePost.toMinutes() + " minutes ago";
            }
            else if (timeSincePost.toHours() < 24)
            {
                timeString += timeSincePost.toHours() + " hours ago";
            }
            else if (timeSincePost.toDays() < 365)
            {
                timeString += timeSincePost.toDays() + " days ago";
            }
            else
            {
                timeString += ( timeSincePost.toDays() / 365 ) + "years ago";
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
        user.exists(new User.UserExistListener()
        {
            @Override
            public void onUserExists()
            {
                user.readData(new User.FirebaseCallback()
                {
                    @Override
                    public void onCallback(User user)
                    {
                        initializeData();
                    }
                });
            }

            @Override
            public void onUserNotExists()
            {
                throw new RuntimeException("Attempt to create MoodList with invalid user");
            }
        });
    }

    protected void addMoodItem(MoodItem item)
    {
        data.add(item);
        add(item);
    }

    protected abstract void initializeData();
}
