package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.LocalDateTime;
import java.util.List;

public abstract class MoodListAdapter extends ArrayAdapter<MoodListItem>
{
    private List<MoodListItem> data;
    private User user;
    private Context context;

    public MoodListAdapter(Context context, User user) {
        super(context, 0);

        this.context = context;
        this.user = user;

        initialize();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.mood_list_item, null);

        ImageView userImage, emotionImage;
        TextView userUsername, userFullName, moodReason, moodTime;

        userImage = item.findViewById(R.id.user_image);
        emotionImage = item.findViewById(R.id.emotion_image);
        userUsername = item.findViewById(R.id.username);
        userFullName = item.findViewById(R.id.full_name);
        moodReason = item.findViewById(R.id.reason);
        moodTime = item.findViewById(R.id.time);

        User user = data.get(position).user;
        MoodEvent event = data.get(position).event;

        //userImage.setImageBitmap()

        userUsername.setText(user.getUserName());
        userFullName.setText(user.getFirstName() + " " + user.getLastName());

        if (event != null)
        {
            moodReason.setText(event.getDescription());
            LocalDateTime time = LocalDateTime.of(event.getDate(), event.getTime());
        }
        else
        {

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

    protected abstract void initializeData();
}
