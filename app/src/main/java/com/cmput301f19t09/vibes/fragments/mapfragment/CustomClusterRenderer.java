package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * This class helps rendering the emoticon image on
 * the map fragment. It controls the cluster views
 * as well as the views of the single items.
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<MoodEvent> {

    private Context context;
    private GoogleMap gmap;

    /**
     * Constructor for CustomClusterRenderer
     * @param context
     * @param map
     * @param clusterManager
     */
    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.gmap = map;
        this.setMinClusterSize(1);
    }

    /**
     * Before the cluster item is rendered, it updates
     * the marker to show the emoticon with its specific
     * defined in EmotionalState.
     * @param event
     * @param markerOptions
     */
    @Override
    protected void onBeforeClusterItemRendered(MoodEvent event, MarkerOptions markerOptions) {
        Integer emoticon = (Integer) EmotionalState.getMap().get(event.getState().getEmotion()).first;
        Integer color = (Integer)  EmotionalState.getMap().get(event.getState().getEmotion()).second;
        markerOptions.icon(bitmapDescriptorFromVector(this.context, emoticon, color));
    }

    /**
     * This is used to convert the drawable object into its bitmap descriptor.
     * It is used for showing the image of the icon.
     * @param context
     * @param vectorResId
     * @return
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId, Integer color) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//        vectorDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        // Set the bounds of the emoticon to be drawn on the canvas
        vectorDrawable.setBounds(20, 20, 100, 100);

        // An empty bitmap
        Bitmap finalBitmap = Bitmap.createBitmap(120,120, Bitmap.Config.RGBA_F16);
        Canvas canvas = new Canvas(finalBitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
//        canvas.drawPaint(paint);
        canvas.drawCircle(60,60,60, paint);
//        canvas.drawBitmap(bitmap,50, 50, null);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(finalBitmap);
    }

}
