package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

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

@RunWith(AndroidJUnit4.class)
public class NavigationTest {

    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

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

    @Test
    public void startEditFragment() {
        onView(withId(R.id.main_add_button)).perform(click());
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void startProfileFragment() {
        onView(withId(R.id.main_profile_button)).perform(click());
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void startFollowingFragment() {
        onView(withId(R.id.main_follow_list_button)).perform(click());
        onView(withId(R.id.followingFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void startMapFragment() {
        onView(withId(R.id.main_view_button)).perform(click());
        onView(withId(R.id.map_fragment)).check(matches(isDisplayed()));
    }

}
