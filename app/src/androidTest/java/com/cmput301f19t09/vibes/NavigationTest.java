package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests the basic navigation within the app to the different Activities and Fragments.
 */
@RunWith(AndroidJUnit4.class)
public class NavigationTest {

    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Grant the test device location permissions.
     */
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Login to the default test account.
     */
    @Before
    public void setUp() {
        try {
            Login.setUp();
        }
        catch (InterruptedException e) {
            Log.d("Test Exception", e.toString());
        }
    }

    /**
     * Check if the MainActivity is started after login.
     */
    @Test
    public void startMainActivity() {
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
    }

    /**
     * Navigate to the EditFragment by clicking add button. Check if the fragment is loaded.
     */
    @Test
    public void startEditFragment() {
        onView(withId(R.id.main_add_button)).perform(click());
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Navigate to the ProfileFragment by clicking profile button. Check if the fragment is loaded.
     */
    @Test
    public void startProfileFragment() {
        onView(withId(R.id.main_profile_button)).perform(click());
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Navigate to the FollowingFragment by clicking follow list button. Check if the fragment is loaded.
     */
    @Test
    public void startFollowingFragment() {
        onView(withId(R.id.main_follow_list_button)).perform(click());
        onView(withId(R.id.followingFragment)).check(matches(isDisplayed()));
    }

    /**
     * Navigate to the MapFragment by clicking main view button. Check if the fragment is loaded.
     */
    @Test
    public void startMapFragment() {
        onView(withId(R.id.main_view_button)).perform(click());
        onView(withId(R.id.map_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Navigate to the MapFragment and then back to MoodListFragment. Check if we end on the
     * MoodListFragment.
     */
    @Test
    public void mapFragmentToListFragment() {
        // navigate to map
        startMapFragment();
        // navigate to mood list from map
        onView(withId(R.id.main_view_button)).perform(click());
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Navigate to the SearchFragment by clicking search button. Check if the fragment is loaded.
     */
    @Test
    public void startSearchFragment() {
        onView(withId(R.id.main_search_button)).perform(click());
        onView(withId(R.id.search_fragment)).check(matches(isDisplayed()));
    }

}
