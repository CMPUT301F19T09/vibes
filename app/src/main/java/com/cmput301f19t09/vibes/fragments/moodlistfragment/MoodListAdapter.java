import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.core.util.Pair;

import com.cmput301f19t09.vibes.models.MoodEvent;
import com.cmput301f19t09.vibes.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class MoodListAdapter extends ArrayAdapter<MoodEvent>
{
    private static final Comparator<MoodEvent> COMPARE_BY_DATE;

    static
    {
        COMPARE_BY_DATE = new Comparator<MoodEvent>()
        {
            @Override
            public int compare(MoodEvent m1, MoodEvent m2)
            {
                LocalDateTime date1 = LocalDateTime.of(m1.getDate(), m1.getTime());
                LocalDateTime date2 = LocalDateTime.of(m2.getDate(), m2.getTime());

                return date1.compareTo(date2);
            }
        };
    }

    private User user;
    private List<Pair<User, MoodEvent>> data;

    public MoodListAdapter(Context context, User user)
    {
        super(context, 0);

        initializeData();
    }

    protected abstract void initializeData();

    public class OwnMoodAdapter extends MoodListAdapter
    {
        private List<MoodEvent> data;

        public OwnMoodAdapter(Context context, User user)
        {
            super(context, user);
        }

        @Override
        protected void initializeData()
        {
            data = new ArrayList<MoodEvent>();

            for (MoodEvent event : user.getMoods())
            {
                data.add(event);
            }

            data.sort(COMPARE_BY_DATE);
            notifyDataSetChanged();
        }
    }

    public class FollowedMoodAdapter extends MoodListAdapter
    {
        private static final Comparator<Pair<User, MoodEvent>> PAIR_COMPARATOR;

        static
        {
            PAIR_COMPARATOR = new Comparator<Pair<User, MoodEvent>>()
            {
                @Override
                public int compare(Pair<User, MoodEvent> p1, Pair<User, MoodEvent> p2)
                {
                    return COMPARE_BY_DATE.compare(p1.second, p2.second);
                }
            };
        }

        private List<Pair<User, MoodEvent>> data;

        public FollowedMoodAdapter(Context context, User user)
        {
            super(context, user);
        }

        @Override
        protected void initializeData()
        {

        }
    }

    private void initializeWithFollowed()
    {
        data = new ArrayList<Pair<User, MoodEvent>>();

        List<String> usernames = user.getFollowingList();

        for (String username : usernames)
        {
            User followed_user = new User(username);

            followed_user.exists(new User.UserExistListener()
            {
                @Override
                public void onUserExists()
                {
                    data.add(new Pair<>(followed_user, null));
                    loadUser(followed_user);
                }

                @Override
                public void onUserNotExists()
                {
                }
            });
        }
    }

    private void loadUser(User user)
    {
        user.readData(new User.FirebaseCallback()
        {
            @Override
            public void onCallback(User user)
            {

            }
        });
    }

    private void setEvent(User user)
    {
        for (Pair<User, MoodEvent> p : data)
        {
            if (p.first == user)
            {
                p.second = user.getMostRecentMood();
                data.sort(BY_DATE);
                break;
            }
        }

        notifyDataSetChanged();
    }
}