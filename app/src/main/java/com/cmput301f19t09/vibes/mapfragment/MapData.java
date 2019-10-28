package com.cmput301f19t09.vibes.mapfragment;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Stores UserPoints for showing them in the map.
 * You can put in new add functions in order to convert them to UserPoints for storing.
 */
public class MapData extends ArrayList<UserPoint> implements Serializable {
    public MapData(int initialCapacity) {
        super(initialCapacity);
    }

    public MapData() {
    }

    public MapData(@NonNull Collection<? extends UserPoint> c) {
        super(c);
    }


}
