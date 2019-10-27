package com.cmput301f19t09.vibes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;

//    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container,false);
//
//        Bundle mapViewBundle = null;
//        if (savedInstanceState != null) {
//            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
//        }
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_view);
//        mapFragment

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

//        if (mapView != null) {
//            googleMap = mapView.getMap();
//            googleMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_flag))
//                    .anchor(0.0f, 1.0f)
//                    .position(new LatLng(55.854049, 13.661331)));
//            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return view;
//            }
//            googleMap.setMyLocationEnabled(true);
//            googleMap.getUiSettings().setZoomControlsEnabled(true);
//            MapsInitializer.initialize(this.getActivity());
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(new LatLng(55.854049, 13.661331));
//            LatLngBounds bounds = builder.build();
//            int padding = 0;
//            // Updates the location and zoom of the MapView
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//            googleMap.moveCamera(cameraUpdate);
//        }

        return view;
    }

    /**
     * User structure required to display a user.
     */
    static class UserPoint{
        String username;
        double lon;
        double lat;
        LatLng location;
        int moodId;
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
    public UserPoint getMockUser(){
        UserPoint mockUserPoint = new UserPoint();
        mockUserPoint.username = "testuser";
        mockUserPoint.lon = 113.4938;
        mockUserPoint.lat = 53.5461;
        mockUserPoint.location = new LatLng(53.5461, 113.4938);
        mockUserPoint.moodId = 0;
        return mockUserPoint;
    }

    /**
     * Shows moods of multiple users.
     * @param mood
     */
    public void showMoods(ArrayList<UserPoint> mood){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("d", "Map ready");

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
//        googleMap.setMinZoomPreference(5);
//        LatLng ny = getMockUser().location;
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}