package com.cmput301f19t09.vibes.fragments.mapfragment;

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
import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListAdapter;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;

/**
 * This class is used as an adapter for the list dialog
 * in the clusters in the map screen. When there are overlapping
 * MoodEvents, this adapter is used to render them in a new dialog.
 */
public class MoodsDialogAdapter extends MoodListAdapter {
    /**
     * Create a MoodsDialogAdapter
     * @param context
     * @param events
     */
    public MoodsDialogAdapter(Context context, ArrayList<MoodEvent> events){
        super(context);

        events.sort((MoodEvent a, MoodEvent b) -> { return b.compareTo(a); });

        this.data = events;
        this.addAll(events);
    }

    @Override
    public void refreshData()
    {

    }
}
