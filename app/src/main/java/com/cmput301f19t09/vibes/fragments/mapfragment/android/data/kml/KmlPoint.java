package com.cmput301f19t09.vibes.fragments.mapfragment.android.data.kml;

import com.google.android.gms.maps.model.LatLng;
import com.cmput301f19t09.vibes.fragments.mapfragment.android.data.Point;

/**
 * Represents a KML Point. Contains a single coordinate.
 */
public class KmlPoint extends Point {
    private final Double mAltitude;

    /**
     * Creates a new KmlPoint
     *
     * @param coordinates coordinates of the KmlPoint
     */
    public KmlPoint(LatLng coordinates) {
        this(coordinates, null);
    }

    /**
     * Creates a new KmlPoint
     *
     * @param coordinates coordinates of the KmlPoint
     * @param altitude    altitude of the KmlPoint
     */
    public KmlPoint(LatLng coordinates, Double altitude) {
        super(coordinates);

        this.mAltitude = altitude;
    }

    public Double getAltitude() {
        return mAltitude;
    }
}
