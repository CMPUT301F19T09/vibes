package com.cmput301f19t09.vibes;

import com.cmput301f19t09.vibes.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
    User mockUser1, mockUser2;

    @BeforeEach
    private void mockUsers() {
        mockUser1 = Mockito.mock(User.class);
        mockUser2 = Mockito.mock(User.class);
//        Mockito.when(mockFirestore.someMethodCallYouWantToMock()).thenReturn(something)

        mockUser1.setFirstName("abc");
        mockUser1.setLastName("def");
        mockUser2.setFirstName("hij");
        mockUser2.setLastName("klm");
    }

    @Test
    void testCompareTo() {
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
