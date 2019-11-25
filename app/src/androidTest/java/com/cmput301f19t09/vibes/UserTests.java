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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class UserTests {
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
            Login.setUp("?userhelper@example.com", "000000");
        }
        catch (InterruptedException e) {
            Log.d("Test Exception", e.toString());
        }
    }

    @Test
    public void testAddRemoveMoodEvent() {
        // check that MainActivity is displayed for navigation
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));

        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // we grant device location permissions by default so dont need to interact with that interface
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // create a new MoodEvent - 1
        // select HAPPINESS
        onData(anything()).inAdapterView(withId(R.id.state_grid_view)).atPosition(0).perform(click());
        onView(withId(R.id.button_submit_view)).perform(click());

        // repeat to create two more MoodEvents - 2
        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // we grant device location permissions by default so dont need to interact with that interface
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // create a new MoodEvent
        // select ANGER
        onData(anything()).inAdapterView(withId(R.id.state_grid_view)).atPosition(8).perform(click());
        onView(withId(R.id.edit_reason_view)).perform(typeText("This is 3"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        // 3
        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // we grant device location permissions by default so dont need to interact with that interface
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // create a new MoodEvent
        // select ANTICIPATION
        onData(anything()).inAdapterView(withId(R.id.state_grid_view)).atPosition(4).perform(click());
        onView(withId(R.id.edit_reason_view)).perform(typeText("thisistwentycharacte"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        // confirm we are back with MoodListFragment displayed
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        // select each MoodEvent and delete them
        for (int i = 0; i < 3; i ++) {
            onData(anything()).inAdapterView(withId(R.id.ml_listview)).atPosition(0).perform(click());
            onView(withId(R.id.mood_details_root)).check(matches(isDisplayed()));
            onView(withId(R.id.delete_button)).perform(click());
        }

        // confirm we are back with MoodListFragment displayed
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));
    }
}
