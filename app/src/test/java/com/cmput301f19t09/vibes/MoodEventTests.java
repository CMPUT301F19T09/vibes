package com.cmput301f19t09.vibes;

import com.cmput301f19t09.vibes.models.MoodEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class MoodEventTests {

    MoodEvent m1, m2;

    @BeforeEach
    private void mockMoodEvents() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        m1 = new MoodEvent(ld, lt, "", null, 0, null,null, null);

        ld = LocalDate.now();
        lt = LocalTime.now();
        m2 = new MoodEvent(ld, lt, "", null, 1, null, null, null);
    }

    /**
     * Tests that the comparator works as expected for mood events when one mood event
     * has a date time before the other.
     */
    @Test
    void testCompareTo() {
        // compareTo returns -1 if m1's LocalDateTime is before m2's
        assertEquals(-1, m1.compareTo(m2));
        // compareTo returns 1 if m2's LocalDateTime is after m1's
        assertEquals(1, m2.compareTo(m1));
    }
}
