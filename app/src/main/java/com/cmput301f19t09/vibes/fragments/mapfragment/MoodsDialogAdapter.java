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
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MoodsDialogAdapter extends ArrayAdapter<MoodEvent> {
    private ArrayList<MoodEvent> eventList;
    private Context context;

    public MoodsDialogAdapter(Context context, ArrayList<MoodEvent> events){
        super(context, 0, events);
        this.eventList = events;
        this.context = context;
    }

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

        emotionImage = item.findViewById(R.id.emotion_image);
        userFullName = item.findViewById(R.id.full_name);
        moodReason = item.findViewById(R.id.reason);
        moodTime = item.findViewById(R.id.mood_time);

        userFullName.setText(user.getFirstName() + " " + user.getLastName());

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
//        // Sets the fullNameText to the user's firstName + lastName
//        TextView fullNameText = view.findViewById(R.id.fullName);
//        String fullName = user.getFirstName() + " " + user.getLastName();
//        fullNameText.setText(fullName);
//
//        // Sets the usernameText to the user's username
//        TextView usernameText = view.findViewById(R.id.username);
//        String username = user.getUserName();
//        usernameText.setText(username);
//
//        // Sets the user's profile picture
//        ImageView userImage = view.findViewById(R.id.profileImage);
//        Glide.with(getContext()).load(user.getProfileURL()).into(userImage);
//        userImage.setClipToOutline(true);

        // Sets an OnClickListener for userImage
//        userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // A ProfileFragment that corresponds to the clicked on user
//                // is created and is made the current fragment
//                ProfileFragment profileFragment;
//                profileFragment = ProfileFragment.newInstance(user.getUid());
//                ((MainActivity) activity).setMainFragment(profileFragment);
//            }
//        });

//        return view;
    }
}
