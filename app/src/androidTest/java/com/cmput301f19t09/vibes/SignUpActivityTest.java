package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cmput301f19t09.vibes.models.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

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
        onView(withId(R.id.username_field)).perform(typeText("signupuser"));
        onView(withId(R.id.email_field)).perform(typeText("signup@example.com"));
        onView(withId(R.id.first_name_field)).perform(typeText("SignupFN"));
        onView(withId(R.id.last_name_field)).perform(typeText("SignupLN"));
        onView(withId(R.id.password_field)).perform(typeText("000000"));
        closeSoftKeyboard();
        onView(withId(R.id.confirm_password_field)).perform(typeText("000000"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_up_button)).perform(click());

        Thread.sleep(5000); // delay to let signup complete
        onView(withId(R.id.login_activity)).check(matches(isDisplayed()));

        Login.setUp("signup@example.com", "000000");
        Thread.sleep(1000); // delay to load anything in the background

//        FirebaseAuth.getInstance().deleteUser(UserManager.getCurrentUserUID());

//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d("TAG", "User account deleted.");
//                }
//            }
//        });
    }

//    @After
//    public void cleanUp() {
//        FirebaseAuth.getInstance().deleteUser(uid);

//        db.collection("cities").document("DC")
//            .delete()
//            .addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "DocumentSnapshot successfully deleted!");
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Error deleting document", e);
//            }
//        });
//    }

}
