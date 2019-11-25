package com.cmput301f19t09.vibes;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.google.firebase.firestore.GeoPoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserHelperTests {
    private List<Map> moods;

    @BeforeEach
    public void mockMoods() {
        // moods is a list of maps
        // the maps are how MoodEvents are stored in firebase
        // initialize some mock maps

        Map moodMap1 = new HashMap<String, Object>();
        moodMap1.put("emotion", "HAPPINESS");
        // latitude -90 +90 longitude -180 +180
        moodMap1.put("location", new GeoPoint(43.131, -123.4121);
        moodMap1.put("photo", null);
        moodMap1.put("reason", null);
        moodMap1.put("social", 0);
        moodMap1.put("timestamp", 0); // Wednesday, December 31, 1969 5:00:00 PM GMT-07:00
        moodMap1.put("username", "vibesusertest");

        Map moodMap2 = new HashMap<String, Object>();
        moodMap2.put("emotion", "FEAR");
        moodMap2.put("location", new GeoPoint(-90.0000, -180.0000);
        moodMap2.put("photo", null);
        moodMap2.put("reason", "because");
        moodMap2.put("social", 1);
        moodMap2.put("timestamp", 1574668072); // Monday, November 25, 2019 12:47:52 AM GMT-07:00
        moodMap2.put("username", "VIBESUSERTEST");

        Map moodMap3 = new HashMap<String, Object>();
        moodMap3.put("emotion", "ANTICIPATION");
        moodMap3.put("location", new GeoPoint(+90.0000, +180.0000);
        moodMap3.put("photo", null);
        moodMap3.put("reason", "YES WE CAN");
        moodMap3.put("social", 3);
        moodMap3.put("timestamp", 1000000); // Monday, January 12, 1970 6:46:40 AM GMT-07:00
        moodMap3.put("username", "1010");

        moods.add(moodMap1);
        moods.add(moodMap2);
        moods.add(moodMap3);
    }

    @Test
    void testParseToMoodEvent() {
        List<MoodEvent> events = new ArrayList<MoodEvent>();

    }
}
