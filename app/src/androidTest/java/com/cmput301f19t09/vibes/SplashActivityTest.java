package com.cmput301f19t09.vibes;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

public class SplashActivityTest {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Rule
    public IntentsTestRule<SplashActivity> intentsTestRule =
            new IntentsTestRule<>(SplashActivity.class);

    @Test
    public void authenticateUserTest() throws InterruptedException {
        Thread.sleep(3000);

        if (mAuth.getCurrentUser() != null) {
            intended(hasComponent(MainActivity.class.getName()));
        } else {
            intended(hasComponent(LoginActivity.class.getName()));
        }
    }

}
