package com.cmput301f19t09.vibes;

import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    static String db_users[] = {
            "9qkppgObLBgPhrF19bRqhncTQjm1",
            "EDA2JYgwh1a9AW40DnyYaQ6BpuC3",
            "I2ctOawtIQcWBwpEL1I1yr3EbVb2",
            "hXc1EVwXLkeDHQG0yPXhyw8ABrH2",
            "o81OG6esaghei2iulWulb3dzPky1"
    };

    @Test
    void testRegisterUser() {
        UserManager.registerUser(db_users[0]);
        User user1 = UserManager.getUser(db_users[0]);

        assertTrue(user1 != null);
        assertEquals(db_users[0], user1.getUid());

        UserManager.registerUser(db_users[1]);
        User user2 = UserManager.getUser(db_users[1]);

        assertTrue(user2 != null);
        assertEquals(db_users[1], user2.getUid());

        User user1_ref = UserManager.getUser(db_users[1]);

        assertTrue(user1_ref != null);
        assertEquals(user1, user1_ref);
    }

    @Test
    void testUnregisterUser() {
        UserManager.registerUser(db_users[0]);
        User user = UserManager.getUser(db_users[0]);

        assertTrue(user != null);

        UserManager.unregisterUser(db_users[0]);

        user = UserManager.getUser(db_users[0]);

        assertTrue(user == null);
    }

    @Test
    void testGetUser() {
        UserManager.registerUser(db_users[0]);
        User user = UserManager.getUser(db_users[0]);
        assertTrue(user != null);
    }
}