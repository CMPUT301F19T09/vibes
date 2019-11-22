package com.cmput301f19t09.vibes;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SignUpActivityTest {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    final private String USER_COLLECTION = "users";

    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

//    @Before
//    public void initializeConnection() {
//        mAuth = FirebaseAuth.getInstance();
//        mStore = FirebaseFirestore.getInstance();
//    }

    /**
     * Navigate to the SignUpActivity by clicking sign up button. Check if the fragment is loaded.
     */
    @Test
    public void navigateToSignUp() {
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_up_button)).perform(click());
        onView(withId(R.id.signup_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void signUpUser() throws InterruptedException {
        navigateToSignUp();
        onView(withId(R.id.username_field)).perform(typeText("AutomatedTestUser"));
        onView(withId(R.id.email_field)).perform(typeText("automatedtestuser@example.com"));
        onView(withId(R.id.first_name_field)).perform(typeText("AutomatedTestFN"));
        onView(withId(R.id.last_name_field)).perform(typeText("AutomatedTestLN"));
        onView(withId(R.id.password_field)).perform(typeText("automatedtest"));
        onView(withId(R.id.confirm_password_field)).perform(typeText("automatedtest"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_button)).perform(click());

        Thread.sleep(5000);
        intended(hasComponent(LoginActivity.class.getName()));
    }

}
