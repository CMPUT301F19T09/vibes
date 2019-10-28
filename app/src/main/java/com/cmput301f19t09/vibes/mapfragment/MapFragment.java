package com.cmput301f19t09.vibes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.Serializable;
import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    ArrayList<UserPoint> data;
    GoogleMap googlemap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            UserPoint mapPoint = (UserPoint)bundle.getSerializable("MapPoint");
            if(mapPoint != null){
                this.data = new ArrayList<UserPoint>();
                this.data.add(mapPoint);
            }
        }
    }

    public void showUserPoint(UserPoint point){
        if(googlemap != null){
            MarkerOptions options = new MarkerOptions();
            options.position(point.location);
            if(point.reason!=null){
                options.snippet(point.reason);
            }
            if(point.emotion != null){
                options.title(point.emotion);
            }
            switch(point.emotion) {
                case "HAPPY":
                    options.icon(bitmapDescriptorFromVector(getActivity(),R.drawable.happy));
                    break;
            }
            googlemap.addMarker(options);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container,false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
        return view;
    }

    /**
     * User structure required to display a user.
     */
    static class UserPoint implements Serializable {
        String username;
        LatLng location;
        int moodId;
        String emotion;
        String reason;
}

    /**
     * Showing a user's mood
     * @param username
     * @return
     */
    public UserPoint showMoodOf(String username){
        return getMockUser();
    }

    /**
     * Returns a mock user for dev purposes.
     * @return
     */
    public static UserPoint getMockUser(){
        UserPoint mockUserPoint = new UserPoint();
        mockUserPoint.username = "testuser";
        mockUserPoint.location = new LatLng(53.5461, 113.4938);
        mockUserPoint.moodId = 0;
        mockUserPoint.emotion = "HAPPY";
        mockUserPoint.reason = "I am pregnant";
        return mockUserPoint;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googlemap = mMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.clear(); //clear old markers

//        CameraPosition googlePlex = CameraPosition.builder()
//                .target(new LatLng(37.4219999,-122.0862462))
//                .zoom(10)
//                .bearing(0)
//                .tilt(45)
//                .build();
//
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

            if(this.data != null){
                    for (UserPoint p:this.data) {
                        this.showUserPoint(p);
                    }
        }
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(37.4219999, -122.0862462))
//                .title("Spider Man")
//                .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.happy)));
//
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(37.4629101,-122.2449094))
//                .title("Iron Man")
//                .snippet("His Talent : Plenty of money"));
//
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(37.3092293,-122.1136845))
//                .title("Captain America"));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}