package com.cmput301f19t09.vibes;

import android.util.Log;

import com.cmput301f19t09.vibes.fragments.mapfragment.MapData;
import com.cmput301f19t09.vibes.fragments.mapfragment.UserPoint;
import com.cmput301f19t09.vibes.models.EmotionalState;
import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;


public class MapFragmentTests {

    public LatLng getMockLocation(){
        LatLng location = new LatLng(23.23, 23.23);
        return location;
    }


    public UserPoint getMockUserPoint(){
        UserPoint test = UserPoint.getMockUser();
        return test;
    }

    @Test
    public void testGetReason(){
        UserPoint test = getMockUserPoint();
        Assert.assertEquals(test.getReason(), "NO REASON");
    }

    @Test
    public void testGetEmotion(){
        UserPoint test = getMockUserPoint();
        Assert.assertEquals(test.getEmotion(), "HAPPY");
    }

    @Test
    public void testGetLocation(){
        UserPoint test = getMockUserPoint();
        Assert.assertTrue(test.getLat() ==53.5461);
        Assert.assertTrue(test.getLong() == 113.4938);
    }

    @Test
    public void testMapData(){
        MapData data = new MapData();
        data.add(getMockUserPoint());
        Assert.assertTrue(data.size() == 1);
    }
}
