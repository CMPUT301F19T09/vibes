package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

public class CustomClusterRenderer extends DefaultClusterRenderer<MoodEvent> {

    private Context context;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.setMinClusterSize(2);
    }

    @Override
    protected void onBeforeClusterItemRendered(MoodEvent event, MarkerOptions markerOptions) {
        Integer emoticon = (Integer) EmotionalState.getMap().get(event.getState().getEmotion()).first;
        Integer color = (Integer)  EmotionalState.getMap().get(event.getState().getEmotion()).second;
        markerOptions.icon(bitmapDescriptorFromVector(this.context, emoticon, color));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId, Integer color) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        vectorDrawable.setBounds(0, 0, 100, 100);

        Bitmap bitmap2 = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap = (Bitmap.createScaledBitmap(bitmap2, 100, 100, true));

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
