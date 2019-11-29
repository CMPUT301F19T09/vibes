package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.test.espresso.matcher.ViewMatchers;
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
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTests {
    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Login to the default test account.
     */
    @Before
    public void setUp() throws InterruptedException {
        try {
            Login.setUp();
        }
        catch (InterruptedException e) {
            Log.d("Test Exception", e.toString());
        }

        Thread.sleep(1000);
        onView(withId(R.id.main_profile_button)).perform(click());
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Tests that the UI elements that are supposed to be loaded when navigating
     * to a users own profile are. Notably, we check the visibility of the follow button
     * which should not appear for a users own profile.
     */
    @Test
    public void assertElements() {
        onView(withId(R.id.profile_picture)).check(matches(isDisplayed()));
        onView(withId(R.id.fullname_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.username_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.user_mood_list)).check(matches(isDisplayed()));
        // no follow button on a users own profile page
        onView(withId(R.id.follow_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        // logout button only displayed on the profile
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));
    }

    /**
     * Tests that the UI elements that are supposed to be loaded when navigating
     * to a users own profile are. Notably, we check the visibility of the follow button
     * which should not appear for a users own profile.
     */
    @Test
    public void logoutTest() {
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));
        onView(withId(R.id.logoutButton)).perform(click());

    }
}
