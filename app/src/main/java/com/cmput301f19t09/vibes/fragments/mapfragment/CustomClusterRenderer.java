package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer implements ClusterRenderer{
    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.setMinClusterSize(2);
    }

    @Override
    public void setAnimation(boolean b) {

    }

    @Override
    public void onAdd() {

    }

    @Override
    public void onRemove() {

    }

}
