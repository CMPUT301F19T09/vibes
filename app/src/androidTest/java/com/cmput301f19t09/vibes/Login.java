package com.cmput301f19t09.vibes;

import androidx.test.rule.ActivityTestRule;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class Login {

    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Class method callable using Login.setUp() for intent tests to login a default test user.
     */
    public static void setUp() throws InterruptedException {
        onView(withId(R.id.email_field)).perform(typeText("?intenttest@gmail.com"));
        onView(withId(R.id.password_field)).perform(typeText("000000"));
        onView(withId(R.id.password_field)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
    }

    /**
     * Class method callable using Login.setUp() for intent tests to login a specified test user.
     *
     * @param   user
     *      The string email of the user to log in
     * @param   pass
     *      The string password of the user to log in
     */
    public static void setUp(String user, String pass) throws InterruptedException {
        onView(withId(R.id.email_field)).perform(typeText(user));
        onView(withId(R.id.password_field)).perform(typeText(pass));
        onView(withId(R.id.password_field)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
    }

    /**
     * Tests the login functionality used by setup(). If this tests fails, all other
     * intent tests are likely to fail because many rely on logging into the app.
     */
    @Test
    public void testLogin() throws InterruptedException {
        onView(withId(R.id.email_field)).perform(typeText("?intenttest@gmail.com"));
        onView(withId(R.id.password_field)).perform(typeText("000000"));
        onView(withId(R.id.password_field)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
    }


}
