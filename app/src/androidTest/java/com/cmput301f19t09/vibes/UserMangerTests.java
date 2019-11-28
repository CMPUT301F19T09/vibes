package com.cmput301f19t09.vibes;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class UserMangerTests {
    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Placeholder.
     */
    @Before
    public void setUp() {

    }

    /**
     * Tests whether the user manager getUser correctly returns null when getCurrentUser
     * is called for a user the DNE ie. is not logged in. Must be run before logging in because
     * Espresso doesnt restart a fresh instance between tests thus UID will remain set.
     */
    @Test
    public void testGetUserDNE() throws InterruptedException {
        Thread.sleep(500);
        // get the current user which should not be logged in
        assertNull(UserManager.getCurrentUserUID());

        // confirm that the method returns null when the user isnt logged in
        assertNull(UserManager.getCurrentUser());
    }

    /**
     * Tests whether the user manager getUser correctly returns the user object
     * for a user that is registered in the db.
     */
    @Test
    public void testGetUserExists() throws InterruptedException {
        // login to the default user
        try {
            Login.setUp();
        }
        catch (InterruptedException e) {
            Log.d("Test Exception", e.toString());
        }

        Thread.sleep(500);
        // get the current user which should be logged in
        User user = UserManager.getCurrentUser();

        // confirm that the user has the correct account information
        assertEquals("?intent", user.getFirstName());
        assertEquals("?tester", user.getLastName());
        assertEquals("?intenttestuser", user.getUserName());
        assertEquals("?intenttest@gmail.com", user.getEmail());
        assertEquals("image/?intenttestuser.png", user.getPicturePath());
    }

}
