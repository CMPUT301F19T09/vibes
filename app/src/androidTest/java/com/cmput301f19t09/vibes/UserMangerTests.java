package com.cmput301f19t09.vibes;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

@RunWith(AndroidJUnit4.class)
public class UserMangerTests {
    // automate disabling device animations which is required by espresso
    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);
}
