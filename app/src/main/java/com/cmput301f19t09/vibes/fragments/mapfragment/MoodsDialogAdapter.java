package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.content.Context;

import com.cmput301f19t09.vibes.fragments.moodlistfragment.MoodListAdapter;
import com.cmput301f19t09.vibes.models.MoodEvent;

import java.util.ArrayList;

/**
 * This class is used as an adapter for the list dialog
 * in the clusters in the map screen. When there are overlapping
 * MoodEvents, this adapter is used to render them in a new dialog.
 */
public class MoodsDialogAdapter extends MoodListAdapter {
    /**
     * Create a MoodsDialogAdapter
     *
     * @param context
     * @param events
     */
    public MoodsDialogAdapter(Context context, ArrayList<MoodEvent> events) {
        super(context);

        events.sort((MoodEvent a, MoodEvent b) -> {
            return b.compareTo(a);
        });

        this.data = events;
        this.addAll(events);
    }

    @Override
    public void refreshData() {

    }
}
