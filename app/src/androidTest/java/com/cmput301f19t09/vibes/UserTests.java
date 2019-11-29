package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
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
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

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
     * Must be signed in before calling. Populates the mood list with 3 mood events.
     */
    public static void createMoodList() throws InterruptedException {
        try {
            Thread.sleep(100);
            onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
            onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            Log.d("TESTERROR", e.toString());
            return;
        }

        Thread.sleep(200);
        int start = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.ml_listview);
        Log.d("MOODLISTSETUPCOUNT", "start: " + start);

        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // create a new MoodEvent - 1
        // select LOVE
        onView(withTagValue(is("LOVE"))).perform(click());
        onView(withId(R.id.button_submit_view)).perform(click());
        Thread.sleep(100);

        // repeat to create two more MoodEvents - 2
        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // create a new MoodEvent
        // select SURPRISE
        onView(withTagValue(is("SURPRISE"))).perform(click());
        onView(withId(R.id.edit_reason_view)).perform(typeText("2nd test moodevent"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        Thread.sleep(100);
        // 3
        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // create a new MoodEvent
        // select TRUST
        onView(withTagValue(is("TRUST"))).perform(click());
        onView(withId(R.id.edit_reason_view)).perform(typeText("thisistwentycharacte"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        // confirm we are back with MoodListFragment displayed
        Thread.sleep(100);
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        // confirm we are back with MoodListFragment displayed
        Thread.sleep(100);
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        // confirm that the number of mood events in the list is the same as when we started the test
        Thread.sleep(200);
        int end = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.ml_listview);
        Log.d("MOODLISTCOUNT", "start: " + end);
        assertEquals(start + 3, end);
    }

    /**
     * Must be signed in before calling. Deletes the specified number of mood events from MoodList.
     *
     * @param   num
     *      number of mood events to delete
     */
    public static void deleteMoods(int num) throws InterruptedException {
        try {
            Thread.sleep(100);
            onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
            onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            Log.d("TESTERROR", e.toString());
            return;
        }

        // select each MoodEvent and delete them
        for (int i = 0; i < num; i ++) {
            onData(anything()).inAdapterView(withId(R.id.ml_listview)).atPosition(0).perform(click());
            onView(withId(R.id.mood_details_root)).check(matches(isDisplayed()));
            onView(withId(R.id.delete_button)).perform(click());
            Thread.sleep(200);
        }
    }

    /**
     * Login to ?userhelper test account.
     */
    @Before
    public void setUp() throws InterruptedException {
        try {
            Login.setUp("?userhelper@example.com", "000000");
        }
        catch (InterruptedException e) {
            Log.d("Test Exception", e.toString());
        }

        // check that MainActivity is displayed for navigation and that MoodListFragment is displayed
        Thread.sleep(100);
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Creates three new mood events and verifies that they are created properly. Deletes the newly
     * created moods and checks that they were deleted properly.
     */
    @Test
    public void testAddRemoveMoodEvent() throws InterruptedException {
        Thread.sleep(200);
        int start = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.ml_listview);
        Log.d("MOODLISTCOUNT", "start: " + start);

        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // we grant device location permissions by default so dont need to interact with that interface
        Thread.sleep(200);
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // create a new MoodEvent - 1
        // select HAPPINESS
        onView(withTagValue(is("HAPPINESS"))).perform(click());
        onView(withId(R.id.button_submit_view)).perform(click());

        // check that a mood was added to the mood list
        Thread.sleep(200);
        int intermediate = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.ml_listview);
        Log.d("MOODLISTCOUNT", "start: " + intermediate);
        assertEquals(start + 1, intermediate);

        // repeat to create two more MoodEvents - 2
        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // we grant device location permissions by default so dont need to interact with that interface
        Thread.sleep(200);
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // create a new MoodEvent
        // select ANGER
        onView(withTagValue(is("ANGER"))).perform(click());
        onView(withId(R.id.edit_reason_view)).perform(typeText("This is 3"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        // check that another mood was added to the mood list
        Thread.sleep(200);
        intermediate = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.ml_listview);
        Log.d("MOODLISTCOUNT", "start: " + intermediate);
        assertEquals(start + 2, intermediate);

        // 3
        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // we grant device location permissions by default so dont need to interact with that interface
        Thread.sleep(200);
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // create a new MoodEvent
        // select ANTICIPATION
        onView(withTagValue(is("ANTICIPATION"))).perform(click());
        onView(withId(R.id.edit_reason_view)).perform(typeText("thisistwentycharacte"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        // check that another mood was added to the mood list
        Thread.sleep(200);
        intermediate = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.ml_listview);
        Log.d("MOODLISTCOUNT", "start: " + intermediate);
        assertEquals(start + 3, intermediate);

        // confirm we are back with MoodListFragment displayed
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        // select each MoodEvent and delete them
        for (int i = 0; i < 3; i ++) {
            onData(anything()).inAdapterView(withId(R.id.ml_listview)).atPosition(0).perform(click());
            onView(withId(R.id.mood_details_root)).check(matches(isDisplayed()));
            onView(withId(R.id.delete_button)).perform(click());
            Thread.sleep(200);
        }

        // confirm we are back with MoodListFragment displayed
        Thread.sleep(200);
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        // confirm that the number of mood events in the list is the same as when we started the test
        Thread.sleep(200);
        int end = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.ml_listview);
        Log.d("MOODLISTCOUNT", "start: " + end);
        assertEquals(start, end);
    }

    /**
     * Creates three new mood events and checks whether the latest mood is the last created.
     * Confirms that the details of the mood event match what was provided when created - therefore
     * confirms functionality of User.addMood. Tests the layout of MoodDetailsFragment.
     */
    @Test
    public void testGetMostRecent() throws InterruptedException {
        createMoodList();
        Thread.sleep(100);
        // confirm we are back with MoodListFragment displayed
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        // click on the latest item in the list
        Thread.sleep(100);
        onData(anything()).inAdapterView(withId(R.id.ml_listview)).atPosition(0).perform(click());

        Thread.sleep(100);
        onView(withId(R.id.mood_details_root)).check(matches(isDisplayed()));

        // the item display should match the last one we added so it should be the TRUST mood event
        onView(withId(R.id.user_image)).check(matches(isDisplayed()));
        onView(withId(R.id.emotion_image)).check(matches(isDisplayed()));
        onView(withId(R.id.full_name)).check(matches(withText("?user ?helper")));
        onView(withId(R.id.username)).check(matches(withText("?userhelper")));
        onView(withId(R.id.reason)).check(matches(withText("thisistwentycharacte")));

        // check remaining details fragment layout elements
        onView(withId(R.id.delete_button)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_button)).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_button)).check(matches(isDisplayed()));

        // return to ListView and delete the created events
        onView(withId(R.id.confirm_button)).perform(click());
        Thread.sleep(100);
        deleteMoods(3);
    }

    /**
     * Tests creating a mood and editing the newly created mood. Verifies that the when
     * we are editing the mood, that the EditText and TextView's are set correctly with
     * the details that we set when creating the mood event. Edits the mood event details and
     * again attempts to re-edit and check if the details are set correctly. Tests User.editMood
     * and EditFragment.
     */
    @Test
    public void testEditMood() throws InterruptedException {
        // navigate to EditFragment
        onView(withId(R.id.main_add_button)).perform(click());
        // we grant device location permissions by default so dont need to interact with that interface
        Thread.sleep(100);
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // create a new MoodEvent
        // select ANGER
        onView(withTagValue(is("ANGER"))).perform(click());
//        onView(withId(R.id.edit_situation_view)).perform(typeText("0.0"));
        closeSoftKeyboard();
        onView(withId(R.id.edit_reason_view)).perform(typeText("three words desc"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        // click on the newly created mood in the list
        Thread.sleep(100);
        onData(anything()).inAdapterView(withId(R.id.ml_listview)).atPosition(0).perform(click());

        Thread.sleep(100);
        onView(withId(R.id.mood_details_root)).check(matches(isDisplayed()));
        // click the edit button
        onView(withId(R.id.edit_button)).perform(click());

        Thread.sleep(100);
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // verify that the fields are populated correctly
        onView(withId(R.id.edit_reason_view)).check(matches(withText("three words desc")));
        Thread.sleep(100);
        onView(withTagValue(is("LOVE"))).perform(click());






//        onView(withId(R.id.social_chip_group)).check(matches(withText("0.0")));
//        onView(withId(R.id.state_text_view)).check(matchesChecks());

        // select new mood; HAPPINESS
        Thread.sleep(100);
//        onData(anything()).inAdapterView(withId(R.id.state_grid_view)).atPosition(5).perform(click());
//        onView(withId(R.id.edit_situation_view)).perform(typeText("1.0"));
        closeSoftKeyboard();
        onView(withId(R.id.edit_reason_view)).perform(typeText("a new desc"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit_view)).perform(click());

        // check that MainActivity is displayed for navigation and that MoodListFragment is displayed
        Thread.sleep(100);
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.mood_list_fragment)).check(matches(isDisplayed()));

        // click on the newly edited mood
        Thread.sleep(100);
        onData(anything()).inAdapterView(withId(R.id.ml_listview)).atPosition(0).perform(click());

        Thread.sleep(100);
        onView(withId(R.id.mood_details_root)).check(matches(isDisplayed()));
        // click the edit button
        onView(withId(R.id.edit_button)).perform(click());

        Thread.sleep(100);
        onView(withId(R.id.edit_fragment)).check(matches(isDisplayed()));

        // verify that the fields are populated correctly again
        onView(withId(R.id.edit_reason_view)).check(matches(withText("a new desc")));
//        onView(withId(R.id.edit_situation_view)).check(matches(withText("1.0")));
//        onView(withId(R.id.state_text_view)).check(matches(withText("HAPPINESS")));
        closeSoftKeyboard();
        onView(withId(R.id.button_cancel_view)).perform(click());

        Thread.sleep(100);
        deleteMoods(1);
    }
}
