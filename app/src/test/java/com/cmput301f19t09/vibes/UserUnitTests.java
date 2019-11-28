package com.cmput301f19t09.vibes;

import com.cmput301f19t09.vibes.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTests {
    User mockUser1, mockUser2;

    @BeforeEach
    public void mockUsers() {
        mockUser1 = new User();
        mockUser2 = new User();

        mockUser1.setFirstName("abc");
        mockUser1.setLastName("def");
        mockUser2.setFirstName("hij");
        mockUser2.setLastName("klm");
    }

    /**
     * Tests user custom comparator for when the name which will come later in the list
     * after a sort is placed into the list first before sorting. Removes the elements
     * from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareSorts() {
        ArrayList<User> userList = new ArrayList<>();
        // put mockUser2 first because when sorted it should come later in the list
        userList.add(mockUser2);
        userList.add(mockUser1);
        Collections.sort(userList, User.sortByName);

        // after sorting mockUser1 should appear earlier in the list
        assertEquals(mockUser1, userList.remove(0));
        assertEquals(mockUser2, userList.remove(0));
    }
    
}