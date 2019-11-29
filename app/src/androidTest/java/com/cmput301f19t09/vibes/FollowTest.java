package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

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
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FollowTest {
    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Two accounts have been set up for this test. Followee is requesting to follow Follower
     * and Follower is following Followee. Tests that the correct number of users appear in the list.
     */
    @Test
    public void testFollow() throws InterruptedException {
        try {
            Login.setUp("?follower@hotmail.com", "000000");
        }
        catch (InterruptedException e) {
            Log.d("Test Exception", e.toString());
        }

        // check that MainActivity is displayed for navigation and that MoodListFragment is displayed
        Thread.sleep(100);
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        onView(withId(R.id.main_follow_list_button)).perform(click());

        Thread.sleep(100);
        onView(withId(R.id.followingFragment)).check(matches(isDisplayed()));

        // followee is requesting to follow us (follower)
        Thread.sleep(200);
        int request = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.requested_list);
        assertEquals(1, request);

        // we follow followee
        Thread.sleep(200);
        int following = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.following_list);
        assertEquals(1, request);
    }
}
