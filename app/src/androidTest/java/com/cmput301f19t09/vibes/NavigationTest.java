package com.cmput301f19t09.vibes;

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
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
        onView(withId(R.id.email_field)).perform(typeText("tz@gmail.com"));
        onView(withId(R.id.password_field)).perform(typeText("111111"));
        onView(withId(R.id.password_field)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
    }

    /**
     * Check if the MainActivity is started after login.
     */
    @Test
    public void startMainActivity() throws InterruptedException {
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void startEditFragment() throws InterruptedException {
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
        onView(withId(R.id.main_add_button)).perform(click());
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void startProfileFragment() throws InterruptedException {
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
        onView(withId(R.id.main_profile_button)).perform(click());
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void startFollowingFragment() throws InterruptedException {
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
        onView(withId(R.id.main_follow_list_button)).perform(click());
        onView(withId(R.id.followingFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void startMapFragment() throws InterruptedException {
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
        onView(withId(R.id.main_view_button)).perform(click());
        onView(withId(R.id.map_fragment)).check(matches(isDisplayed()));
    }

}
