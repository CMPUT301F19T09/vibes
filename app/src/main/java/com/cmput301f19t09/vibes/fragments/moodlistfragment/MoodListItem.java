package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.LocalDateTime;
import java.util.Comparator;

public class MoodListItem
{
    public static final Comparator<MoodListItem> date_comparator;

    static
    {
        date_comparator = new Comparator<MoodListItem>()
        {
            @Override
            public int compare(MoodListItem item1, MoodListItem item2)
            {
                LocalDateTime time1 = LocalDateTime.of(item1.event.getDate(), item1.event.getTime());
                LocalDateTime time2 = LocalDateTime.of(item2.event.getDate(), item2.event.getTime());

                return time1.compareTo(time2);
            }
        };
    }

    public User user;
    public MoodEvent event;

    public MoodListItem(User user, MoodEvent event)
    {
        this.user = user;
        this.event = event;
    }
}
