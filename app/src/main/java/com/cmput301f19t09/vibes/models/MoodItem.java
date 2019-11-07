package com.cmput301f19t09.vibes.models;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;

/*
    A class to wrap MoodEvent, User, and that MoodEvent's index in User.
    Useful for link
 */
public class MoodItem implements Serializable
{
    public static final Comparator<MoodItem> date_comparator;

    static
    {
        date_comparator = new Comparator<MoodItem>()
        {
            @Override
            public int compare(MoodItem item1, MoodItem item2)
            {
                if (item1.event == null || item2.event == null)
                {
                    return 0;
                }

                LocalDateTime time1 = LocalDateTime.of(item1.event.getDate(), item1.event.getTime());
                LocalDateTime time2 = LocalDateTime.of(item2.event.getDate(), item2.event.getTime());

                return -time1.compareTo(time2);
            }
        };
    }

    public User user;
    public MoodEvent event;
    private final int index;

    public MoodItem(User user, MoodEvent event)
    {
        //this.user = user;
        //this.event = event;
        //this.index = -1;
        this(user, event, -1);
    }

    public MoodItem(User user, MoodEvent event, int index)
    {
        this.user = user;
        this.event = event;
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public Duration timeSinceEvent()
    {
        if (event == null)
        {
            return null;
        }


        LocalDateTime time = LocalDateTime.of(event.getDate(), event.getTime());
        Duration timeSincePost = Duration.between(time, LocalDateTime.now());

        return timeSincePost;
    }
}
