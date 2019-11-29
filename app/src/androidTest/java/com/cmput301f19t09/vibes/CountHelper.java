package com.cmput301f19t09.vibes;

import android.view.View;
import android.widget.ListView;

import androidx.annotation.IdRes;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * This class provides static ListView count helper method which can be used to verify
 * the number of elements in a ListView.
 * Reference: https://stackoverflow.com/questions/30361068/assert-proper-number-of-items-in-list-with-espresso
 */
public class CountHelper {

    private static int count;

    /**
     * Static method which implements a TypeSafeMatcher which requires a non-null value of
     * ListView type and returns the count. Call this method by passing in a ListView id
     * to get the total number of elements in the ListView, not just displayed.
     */
    public static int getCountFromListUsingTypeSafeMatcher(@IdRes int listViewId) {
        count = 0;

        Matcher matcher = new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                count = ((ListView) item).getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        };

        onView(withId(listViewId)).check(matches(matcher));

        int result = count;
        count = 0;
        return result;
    }

}