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

import java.util.List;

public class MoodAdapter extends ArrayAdapter<MoodEvent>
{
    private Context context;

    private List<MoodEvent> data;

    public MoodAdapter(Context context, List<MoodEvent> dataList)
    {
        super(context, 0, dataList);

        this.data = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View moodView = convertView;

        if (moodView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            moodView = inflater.inflate(R.layout.mood_list_item, parent, false);
        }

        MoodEvent event = data.get(position);

        ImageView userImage, emotionImage;
        TextView fullName, userName, reasonText, timeText;

        return moodView;
    }
}
