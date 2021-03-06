package com.cmput301f19t09.vibes;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cmput301f19t09.vibes.models.UserManager;
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

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTests {

    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Navigate to the SignUpActivity by clicking sign up button. Check if the fragment is loaded.
     */
    @Test
    public void navigateToSignUp() {
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_up_button)).perform(click());
        onView(withId(R.id.signup_activity)).check(matches(isDisplayed()));
    }

    /**
     * Test the sign up functionality by creating a test user and attempting to login after
     * the sign up has completed. Deletes the user after the test so that it can be reran.
     */
    @Test
    public void signUpUser() throws InterruptedException {
        navigateToSignUp();
        onView(withId(R.id.username_field)).perform(typeText("?signupuser"));
        closeSoftKeyboard();
        onView(withId(R.id.email_field)).perform(typeText("?signup@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.first_name_field)).perform(typeText("?SignupFN"));
        closeSoftKeyboard();
        onView(withId(R.id.last_name_field)).perform(typeText("?SignupLN"));
        closeSoftKeyboard();
        onView(withId(R.id.password_field)).perform(typeText("000000"));
        closeSoftKeyboard();
        onView(withId(R.id.confirm_password_field)).perform(typeText("000000"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_button)).perform(click());

        Thread.sleep(5000); // delay to let sign up complete
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()));

        Login.setUp("?signup@example.com", "000000");
        Thread.sleep(1000); // delay to load anything in the background

        /**
         * Use firebase function to delete the newly created user so that we can rerun the test.
         * The function doesnt always succeed so call the function a few times. The function
         * performs an asynchronous call so we cant garuntee it succeeds. Also, due to the
         * asynch., the collection and image may take some time to remove.
         */

        for (int i = 0; i < 5; i++ ) {
            DeleteUser.deleteUser(UserManager.getCurrentUserUID());
            Thread.sleep(1000); // delay to give some time between function calls
        }
    }

}
