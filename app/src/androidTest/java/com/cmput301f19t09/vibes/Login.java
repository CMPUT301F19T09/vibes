package com.cmput301f19t09.vibes;

import androidx.test.rule.ActivityTestRule;

import org.junit.ClassRule;
import org.junit.Rule;

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
    public ActivityTestRule<SplashActivity> rule =
            new ActivityTestRule<>(SplashActivity.class, true, true);

    /**
     * Class method callable using Login.setUp() for intent tests to login a test user.
     */
    public static void setUp() throws InterruptedException {
        onView(withId(R.id.email_field)).perform(typeText("tz@gmail.com"));
        onView(withId(R.id.password_field)).perform(typeText("111111"));
        onView(withId(R.id.password_field)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        // sleep to allow time for login to process and view to switch
        Thread.sleep(6000);
    }


}
