package com.cmput301f19t09.vibes;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SignUpActivityTest {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    final private String USER_COLLECTION = "users";

    @Rule
    public IntentsTestRule<SignUpActivity> intentsTestRule =
            new IntentsTestRule<>(SignUpActivity.class);

    @Before
    public void initializeConnection() {
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
    }

    @Test
    public void signUpUser() throws InterruptedException {
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
