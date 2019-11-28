package com.cmput301f19t09.vibes.fragments.mapfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.mooddetailsfragment.MoodDetailsDialogFragment;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MapFragment extends Fragment implements OnMapReadyCallback, Observer,
        ClusterManager.OnClusterClickListener<MoodEvent>,
        ClusterManager.OnClusterInfoWindowClickListener<MoodEvent>,
        ClusterManager.OnClusterItemClickListener<MoodEvent>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MoodEvent> {

    GoogleMap googlemap;
    private ClusterManager<MoodEvent> mClusterManager;
    boolean firstPointPut = false;
    Context context;
    private MapFilter mapFilter;

    /**
     * This is used to filter out the moods being showed;
     */
    public enum Filter{
        SHOW_MINE,
        SHOW_EVERYONE
    }

    Filter filter;
    String emotionSelected;
    private List<String> observedUsers;

    /**
     * The map fragment shows the locations of the moods.
     * It can show an interactive MoodEvent, helping the mood to be able to get edited or viewed
     * by the user.
     */
    public MapFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        observedUsers = new ArrayList<>();
    }

    public static MapFragment newInstance(Context context){
        return new MapFragment(context);
    }

    /**
     * Checks for the bundle.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Displays the MoodEvent in the map.
     * @param event The MoodEvent that you're trying to show.
     */
    public void showMoodEvent(MoodEvent event){
        if(googlemap != null){
            // Adding the event to the cluster manager
            if (event.getLocation() != null) {
                mClusterManager.addItem(event);
                mClusterManager.cluster();
                // If first marker, move the camera to the marker
                if (!firstPointPut) {
                    firstPointPut = true;
                    googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), 4));
                }
            }
        }
    }

    public void setEmotionSelected(String emotion){
        this.emotionSelected = emotion;
    }

    /**
     * Making a callback function for when the map object is ready.
     * As the map is read,
     * The onMapReady function is called to go throught the given MoodEvent.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SupportMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(this);
        if (mapFilter == null)
        {
            mapFilter = MapFilter.getInstance(Filter.SHOW_MINE);
            getChildFragmentManager().beginTransaction().add(R.id.filter_root, mapFilter, "mapFilter").commit();
        }
        UserManager.addUserObserver(UserManager.getCurrentUserUID(), this);
        return view;
    }

    /**
     * This is a callback function. It is called when the map is ready.
     * @param mMap
     */
    @Override
    public void onMapReady(GoogleMap mMap) {
        googlemap = mMap;

//        Code For changing the structure of the
//        Info window. Commented out to be used later.
//        googlemap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                View v = getLayoutInflater().inflate(R.layout.info_window, null);
//
//                // Getting the position from the marker
//                TextView tvLatitude= (TextView) findViewById(R.id.tvLatitude);
//                tvLatitude.setText("Latitude ");
//                // Returning the view containing InfoWindow contents
//                return v;
//                return null;
//            }
//        });

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear();

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(getActivity(), googlemap);
        ClusterRenderer clusterRenderer = new CustomClusterRenderer(getActivity(), googlemap, mClusterManager);
        mClusterManager.setRenderer(clusterRenderer);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googlemap.setOnCameraIdleListener(mClusterManager);
        googlemap.setOnMarkerClickListener(mClusterManager);
        googlemap.setOnInfoWindowClickListener(mClusterManager);

        switchFilter(Filter.SHOW_MINE, null);
    }

    public void switchFilter(Filter filter, @Nullable String emotion) {
        if (googlemap == null) {
            Log.e("SWITCH FILTER", "NO GOOGLEMAP DEFINED");
            return;
        }

        this.filter = filter;
        User user = UserManager.getCurrentUser();
        clusterCleanUp();
        if (filter == Filter.SHOW_MINE) {
            Log.d("TEST/Map", "Showing own events");
            removeObservers();
            showUserEvents(emotion);
        } else if (filter == Filter.SHOW_EVERYONE) {
            Log.d("TEST/Map", "Showing everyone's events");
            for (String id : user.getFollowingList())
            {
                Log.d("TEST/Map", "Adding " + id);
                observedUsers.add(id);
                User followed_user = UserManager.getUser(id);
                if (followed_user.isLoaded()) {
                    if (followed_user.getMostRecentMoodEvent() != null) {
                        showEvent(followed_user.getMostRecentMoodEvent());
                    }
                }
                followed_user.addObserver(new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        showFollowedEvents();
                    }
                });
            }
        }else{
            throw new RuntimeException("Map filter is not recognized.");
        }
    }

    private void showFollowedEvents()
    {
        for (String id : observedUsers)
        {
            User user = UserManager.getUser(id);
            if (user.isLoaded() && user.getMostRecentMoodEvent() != null)
            {
                showEvent(user.getMostRecentMoodEvent());
            }
        }
    }

    private void showUserEvents(@Nullable String emotion)
    {
        for (MoodEvent event : UserManager.getCurrentUser().getMoodEvents()) {
            if(emotion != null){
                if(event.getState().getEmotion() == emotion){
                    showEvent(event);
                }
            }else{
                showEvent(event);
            }
        }
    }

    private void showEvent(MoodEvent event)
    {
        showMoodEvent(event);
    }

    private void removeObservers()
    {
        for (String id : observedUsers)
        {
            UserManager.getUser(id).deleteObservers();
        }
    }

    @Override
    public void onPause() {
        removeObservers();
        UserManager.getCurrentUser().deleteObserver(this);
        super.onPause();
    }

    public void clusterCleanUp(){
        mClusterManager.clearItems();
        mClusterManager.cluster();
        Log.d("TEST/Map", "Cleared the cluster items");
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d("TEST/Map", "Current User Update");
        clusterCleanUp();
        if (filter == Filter.SHOW_MINE)
        {
            showUserEvents(null);
        }
        else
        {
            for (String id : ((User)o).getFollowingList())
            {
                if (!observedUsers.contains(id))
                {
                    observedUsers.add(id);
                    User followed_user = UserManager.getUser(id);
                    if (followed_user.isLoaded()) {
                        if (followed_user.getMostRecentMoodEvent() != null) {
                            showEvent(followed_user.getMostRecentMoodEvent());
                        }
                    }
                    followed_user.addObserver(new Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            showFollowedEvents();
                        }
                    });
                }
            }
            showFollowedEvents();
        }
    }

    /**
     * Shows up a dialog when there are multiple moods in the same location in a cluster.
     * @param events
     */
    public void showDialogForMultipleEvents(Collection<MoodEvent> events){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Multiple moods in same location:");

        ArrayList listEvents = new ArrayList(events);

        final MoodsDialogAdapter customAdapter = new MoodsDialogAdapter(context, listEvents);

        builderSingle.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(customAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MoodEvent eventSelected = customAdapter.getItem(which);
                ((MainActivity)getActivity()).openDialogFragment(MoodDetailsDialogFragment.newInstance(eventSelected, filter == Filter.SHOW_MINE));
            }
        });

        builderSingle.show().getListView().setBackgroundResource(R.color.moodListBackground);
    }

    /**
     * Triggered when you click on a cluster that has multiple moods in it.
     * @param events
     * @return
     */
    @Override
    public boolean onClusterClick(Cluster<MoodEvent> events) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : events.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();
        Log.d("LatLngBounds", bounds.toString());

        if(bounds.northeast.equals(bounds.southwest)){
            Log.d("DEV", "This cluster has multiple in the same point.");
            showDialogForMultipleEvents(events.getItems());
            return true;
        }
        // Animate camera to the bounds
        try {
            googlemap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MoodEvent> events) {
        Log.d("MAP", "cluster info is clicked.");
    }

    @Override
    public boolean onClusterItemClick(MoodEvent event) {

        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MoodEvent event) {
        ((MainActivity)getActivity()).openDialogFragment(MoodDetailsDialogFragment.newInstance(event, filter == Filter.SHOW_MINE));
        Log.d("MAP", "clusterPoint info is clicked.");
    }


}