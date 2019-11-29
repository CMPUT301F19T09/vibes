package com.cmput301f19t09.vibes;

import com.cmput301f19t09.vibes.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTests {
    User mockUser1, mockUser2, mockUser3, mockUser4, mockUser5, mockUser6, mockUser7, mockUser8;

    @BeforeEach
    public void mockUsers() {
        mockUser1 = new User();
        mockUser2 = new User();
        mockUser3 = new User();
        mockUser4 = new User();
        mockUser5 = new User();
        mockUser6 = new User();
        mockUser7 = new User();
        mockUser8 = new User();

        mockUser1.setFirstName("abc");
        mockUser1.setLastName("def");

        mockUser2.setFirstName("hij");
        mockUser2.setLastName("klm");

        mockUser3.setFirstName("hij");
        mockUser3.setLastName("klm");

        mockUser4.setFirstName("Abc");
        mockUser4.setLastName("def");

        mockUser5.setFirstName("01");
        mockUser5.setLastName("def");

        mockUser6.setFirstName("");
        mockUser6.setLastName("");

        mockUser7.setFirstName("Tim");
        mockUser7.setLastName("Mason");

        mockUser8.setFirstName("Timothy");
        mockUser8.setLastName("Mason");
    }

    /**
     * Tests User custom comparator for when the name which will come later in the list
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

    /**
     * Tests User custom comparator for when the name which will come first in the list
     * after a sort is placed into the list first before sorting. Removes the elements
     * from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareNoSort() {
        ArrayList<User> userList = new ArrayList<>();
        // put mockUser1 first; when sorted they shouldnt be switched
        userList.add(mockUser1);
        userList.add(mockUser2);
        Collections.sort(userList, User.sortByName);

        // after sorting mockUser1 should appear earlier in the list
        assertEquals(mockUser1, userList.remove(0));
        assertEquals(mockUser2, userList.remove(0));
    }

    /**
     * Tests User custom comparator for when two users have identical names. Removes the elements
     * from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareEqual() {
        ArrayList<User> userList = new ArrayList<>();
        // users have equal names put them in arbitrary order but match whatever we put first
        // will be first in the array after the sort
        userList.add(mockUser2);
        userList.add(mockUser3);
        Collections.sort(userList, User.sortByName);

        // after sorting mockUser2 should appear earlier in the list because sort wont
        // switch equal objects
        assertEquals(mockUser2, userList.remove(0));
        assertEquals(mockUser3, userList.remove(0));
    }

    /**
     * Tests User custom comparator for when the two names have the same characters in their
     * first and last names except one's first name has the uppercase letter. Removes the elements
     * from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareUppercase() {
        ArrayList<User> userList = new ArrayList<>();
        // put mockUser1 first because when sorted it should come later in the list
        userList.add(mockUser1);
        userList.add(mockUser4);
        Collections.sort(userList, User.sortByName);

        // after sorting mockUser4 should appear earlier in the list
        assertEquals(mockUser4, userList.remove(0));
        assertEquals(mockUser1, userList.remove(0));
    }

    /**
     * Tests User custom comparator for when a name contains numbers and the other does not.
     * Removes the elements from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareNumbers() {
        ArrayList<User> userList = new ArrayList<>();
        // put mockUser1 first because when sorted it should come later in the list
        userList.add(mockUser1);
        userList.add(mockUser5);
        Collections.sort(userList, User.sortByName);

        // after sorting mockUser5 should appear earlier in the list
        assertEquals(mockUser5, userList.remove(0));
        assertEquals(mockUser1, userList.remove(0));
    }

    /**
     * Tests User custom comparator for when a name is an empty string and the other is non-empty.
     * Removes the elements from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareEmpty() {
        ArrayList<User> userList = new ArrayList<>();
        // put mockUser1 first because when sorted it should come later in the list
        userList.add(mockUser1);
        userList.add(mockUser6);
        Collections.sort(userList, User.sortByName);

        // after sorting mockUser6 should appear earlier in the list because it is empty
        // it has a value of 0 effectively
        assertEquals(mockUser6, userList.remove(0));
        assertEquals(mockUser1, userList.remove(0));
    }

    /**
     * Tests User custom comparator for when a name is an empty string and the other is non-empty.
     * Removes the elements from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareExtendedName() {
        ArrayList<User> userList = new ArrayList<>();
        // put mockUser8 first because when sorted it should come later in the list
        userList.add(mockUser8);
        userList.add(mockUser7);
        Collections.sort(userList, User.sortByName);

        // after sorting mockUser7 should appear earlier in the list because it
        // has the same name as mockUser8 but with a few less characters
        // Tim Mason vs Timothy Mason
        assertEquals(mockUser7, userList.remove(0));
        assertEquals(mockUser8, userList.remove(0));
    }

    /**
     * Tests User custom comparator for when a name is an empty string and the other is non-empty.
     * Removes the elements from the array one at a time checking that it is the expected user.
     */
    @Test
    public void testCompareFullList() {
        ArrayList<User> userList = new ArrayList<>();
        // put users in the list in an arbitrary order
        userList.add(mockUser2);
        userList.add(mockUser6);
        userList.add(mockUser4);
        userList.add(mockUser5);
        userList.add(mockUser8);
        userList.add(mockUser3);
        userList.add(mockUser1);
        userList.add(mockUser7);
        Collections.sort(userList, User.sortByName);

        // after sorting the collection should follow the rules checked by the other tests
        assertEquals(mockUser6, userList.remove(0));
        assertEquals(mockUser5, userList.remove(0));
        assertEquals(mockUser4, userList.remove(0));
        assertEquals(mockUser7, userList.remove(0));
        assertEquals(mockUser8, userList.remove(0));
        assertEquals(mockUser1, userList.remove(0));
        assertEquals(mockUser2, userList.remove(0));
        assertEquals(mockUser3, userList.remove(0));
    }
}