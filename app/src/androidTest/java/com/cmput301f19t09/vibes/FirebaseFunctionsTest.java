package com.cmput301f19t09.vibes;

import com.google.firebase.functions.FirebaseFunctions;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FirebaseFunctionsTest
{
    FirebaseFunctions fbFunctions;

    private final String graysonUID = "I2ctOawtIQcWBwpEL1I1yr3EbVb2";

    @Before
    void initFunctions()
    {
        fbFunctions = FirebaseFunctions.getInstance();
    }

    @Test
    void testDeleteUser()
    {
        Map<String, Object> data = new HashMap<>();

        data.put("uid", graysonUID);

        fbFunctions.getHttpsCallable("deleteUser").call(data);
    }
}
