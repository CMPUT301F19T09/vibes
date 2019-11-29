package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.test.espresso.matcher.ViewMatchers;
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
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class EditFragmentTests {
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
    public void setUp() throws InterruptedException {
        try {
            Login.setUp();
        }
        catch (InterruptedException e) {
            Log.d("Test Exception", e.toString());
        }

        Thread.sleep(1000);
        onView(withId(R.id.main_add_button)).perform(click());
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Test whether all UI elements are on screen in the edit fragment when we
     * are adding a new mood event.
     */
    @Test
    public void assertElementsAdd() {
        // check that all the expected fields are displayed
        onView(withId(R.id.title_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_emotion_title)).check(matches(isDisplayed()));
        onView(withId(R.id.emotion_chip_group)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_reason_title)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_reason_view)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_social_title)).check(matches(isDisplayed()));
        onView(withId(R.id.social_chip_group)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_photo_title)).check(matches(isDisplayed()));
        onView(withId(R.id.capture_button)).check(matches(isDisplayed()));
        onView(withId(R.id.gallery_button)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_location_title)).check(matches(isDisplayed()));
        onView(withId(R.id.location_switch)).check(matches(isDisplayed()));
        onView(withId(R.id.date_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.time_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.button_cancel_view)).check(matches(isDisplayed()));
        onView(withId(R.id.button_submit_view)).check(matches(isDisplayed()));
    }

    /**
     * Test whether image loading behaviour is working as intended for when a user
     * selects a particular emotion chip. The image should load when selected once and
     * should disappear again on a second click.
     */
    @Test
    public void toggleEmotion() {
        // no emotion image visible at start
        onView(withId(R.id.emotion_image)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        // select an emotion
        onView(withTagValue(is("SURPRISE"))).perform(click());
        // an emotion image should be visible
        onView(withId(R.id.emotion_image)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        // select the same emotion again to unselect it
        onView(withTagValue(is("SURPRISE"))).perform(click());
        onView(withId(R.id.emotion_image)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /**
     * Test whether the location switch toggling behaviour is working as intended. The switch
     * should start off and should allow the user to toggle at will (if location permissions are given;
     * which this test gives automatically) afterward.
     */
    @Test
    public void toggleLocation() {
        // location switch should be visible and toggled off
        onView(withId(R.id.location_switch)).check(matches(isDisplayed()));
        onView(withId(R.id.location_switch)).check(matches(isNotChecked()));
        // select the location switch
        onView(withId(R.id.location_switch)).perform(click());
        // it should be toggled on
        onView(withId(R.id.location_switch)).check(matches(isChecked()));
        // re toggle it
        onView(withId(R.id.location_switch)).perform(click());
        // it should again be toggled off
        onView(withId(R.id.location_switch)).check(matches(isNotChecked()));
    }
}
