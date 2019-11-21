package com.cmput301f19t09.vibes.fragments.searchfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchListAdapter extends ArrayAdapter<User> implements Observer {
    protected List<User> data;
    protected User user;
    private Context context;
    private List<String> observed_users;

    public SearchListAdapter(Context context) {
        super(context, 0);
        this.context = context;
        this.user = UserManager.getCurrentUser();
        this.data = new ArrayList<User>();

        initialize();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.search_list_item, parent, false);

        }

        if (!data.isEmpty() || position < data.size()) {
            User user = data.get(position);
        }

        if (user == null) {
            return item;
        }

        ImageView userProfile = item.findViewById(R.id.search_profile_picture);
        TextView userFullName = item.findViewById(R.id.search_full_name);
        TextView userUserName = item.findViewById(R.id.search_username);
        Glide.with(getContext()).load(user.getProfileURL()).into(userProfile);
        userProfile.setClipToOutline(true);

        userFullName.setText(user.getFirstName() + " " + user.getLastName());
        userUserName.setText(user.getUserName());
        return item;
    }

    public void refreshData(List<String> userList) {
        System.out.println(userList);
        data = new ArrayList<User>();

        if (userList == null) {
            return;
        }

        for (String userUID : userList) {
//            data.add(user);
            if (!observed_users.contains(userUID)) {
                User user = UserManager.getUser(userUID);
                observed_users.add(userUID);

                if (user.isLoaded()) {
                    data.add(user);
                }

                UserManager.addUserObserver(userUID, this);

//                UserManager.addUserObserver(userUID, new Observer() {
//                    @Override
//                    public void update(Observable observable, Object o) {
//                        SearchListAdapter.this.refreshData();
//                    }
//                });
            }
        }

        data.sort(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getFirstName().compareTo(user2.getFirstName());
            }
        });

        addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object o) {
        refreshData(null);
    }

    private void initialize() {
        UserManager.addUserObserver(user.getUid(), this);
        observed_users = new ArrayList<String>();
    }

    public void destroy() {
        UserManager.removeUserObserver(user.getUid(), this);
    }
}
