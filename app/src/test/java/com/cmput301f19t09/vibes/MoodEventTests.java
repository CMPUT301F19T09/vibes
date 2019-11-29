package com.cmput301f19t09.vibes;

import com.cmput301f19t09.vibes.models.MoodEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class MoodEventTests {

    MoodEvent m1, m2;

    /**
     * Tests that the comparator works as expected for mood events when one mood event
     * has a date time before the other.
     */
    @Test
    void testCompareTo() throws InterruptedException {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        m1 = new MoodEvent(ld, lt, "earlier time", null, 1, null,null, null);

        Thread.sleep(1000);
        LocalDate ld2 = LocalDate.now();
        LocalTime lt2 = LocalTime.now();
        m2 = new MoodEvent(ld2, lt2, "later time", null, 2, null, null, null);

        // compareTo returns -1 if m1's LocalDateTime is before m2's
        assertEquals(-1, m1.compareTo(m2));
        // compareTo returns 1 if m2's LocalDateTime is after m1's
        assertEquals(1, m2.compareTo(m1));
    }

    /**
     * Tests that the comparator works as expected for mood events they have equal
     * date times.
     */
    @Test
    void testCompareToEqual() throws InterruptedException {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        m1 = new MoodEvent(ld, lt, "equal time", null, 1, null,null, null);
        m2 = new MoodEvent(ld, lt, "same time", null, 2, null, null, null);

        // compareTo returns 0 if m1's LocalDateTime is equal to m2's
        assertEquals(0, m1.compareTo(m2));
        // compareTo returns 0 if m2's LocalDateTime is equal to m1's
        assertEquals(0, m2.compareTo(m1));
    }
}
