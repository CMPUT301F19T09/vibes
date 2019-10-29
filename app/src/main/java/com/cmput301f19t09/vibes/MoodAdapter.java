package com.cmput301f19t09.vibes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class MoodAdapter extends ArrayAdapter<Mood> {
    private ArrayList<Mood> moods;
    private Context context;

    public MoodAdapter(Context context, ArrayList<Mood> moods){
        super(context, 0, moods);
        this.moods = moods;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.mood_list, parent, false);
        }

        Mood mood = moods.get(position);

        TextView nameText = view.findViewById(R.id.name);
        TextView dateText = view.findViewById(R.id.date);

        String name = mood.getName();
        nameText.setText(name);
        String date = String.format("%04d", mood.getYear()) +
                ":" + String.format("%02d", mood.getMonth()) +
                ":" + String.format("%02d", mood.getDay()) +
                ":" + String.format("%02d", mood.getHour()) +
                ":" + String.format("%02d", mood.getMinute());
        dateText.setText(date);

        ImageView imageView = view.findViewById(R.id.emotion_image);
        imageView.setImageResource(mood.getImageFile());

        return view;
    }
}
