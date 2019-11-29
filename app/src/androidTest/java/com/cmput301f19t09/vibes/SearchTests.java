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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SearchTests {
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
        onView(withId(R.id.main_search_button)).perform(click());
        onView(withId(R.id.search_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Test whether all UI elements are on screen in the search fragment.
     */
    @Test
    public void assertElements() {
        onView(withId(R.id.search_edittext)).check(matches(isDisplayed()));
        onView(withId(R.id.main_search_button)).check(matches(isDisplayed()));
    }

    /**
     * Test whether elements are returned when searching test. The search is by username
     * and is case sensitive. Note: this test will fail if the test user accounts are deleted.
     * Test user account fields are prefixes by a '?'.
     */
    @Test
    public void searchName() throws InterruptedException {
        Thread.sleep(500);
        int start = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.search_listview);
        // whe the fragment starts their should be no elements in the list view
        assertEquals(0, start);

        // search for test users are they should be stable in the
        onView(withId(R.id.search_edittext)).perform(typeText("?"));
        closeSoftKeyboard();

        Thread.sleep(500);
        int end = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.search_listview);

        assertTrue(end > start);
    }

    /**
     * Test that no elements are displayed on the search page by default and that no
     * elements are displayed as a result of interacting with the textview but
     * not entering any text.
     */
    @Test
    public void assertNoTyping() throws InterruptedException {
        Thread.sleep(500);
        int start = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.search_listview);
        // whe the fragment starts their should be no elements in the list view
        assertEquals(0, start);

        // click on the textview but dont type anything
        onView(withId(R.id.search_edittext)).perform(click());
        closeSoftKeyboard();

        Thread.sleep(500);
        int end = CountHelper.getCountFromListUsingTypeSafeMatcher(R.id.search_listview);

        assertTrue(end == start);
    }
}
