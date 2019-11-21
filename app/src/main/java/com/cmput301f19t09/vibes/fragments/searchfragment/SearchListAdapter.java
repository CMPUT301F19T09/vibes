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

public class SearchListAdapter extends ArrayAdapter<String> implements Observer {
    protected List<String> data;
    private Context context;

    public SearchListAdapter(Context context) {
        super(context, 0);
        this.context = context;
        this.data = new ArrayList<String>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.search_list_item, parent, false);

        }

        String userUID = data.get(position);

        if (userUID == null) {
            return item;
        }

        User user = UserManager.getUser(userUID);

        if (user.isLoaded()) {

            ImageView userProfile = item.findViewById(R.id.search_profile_picture);
            TextView userFullName = item.findViewById(R.id.search_full_name);
            TextView userUserName = item.findViewById(R.id.search_username);
            Glide.with(getContext()).load(user.getProfileURL()).into(userProfile);
            userProfile.setClipToOutline(true);

            userFullName.setText(user.getFirstName() + " " + user.getLastName());
            userUserName.setText(user.getUserName());

        }
        return item;
    }

    public void refreshData(List<String> userList) {
        clear();
        notifyDataSetChanged();

        if (userList == null) {
            return;
        }

        for (String userUID : userList) {
            data.add(userUID);
        }

        addAll(data);

//        for (String userUID : userList) {
//            User user = UserManager.getUser(userUID);
//
//                if (user.isLoaded()) {
//                    data.add(user);
//                }
//
//            user.readData(new User.FirebaseCallback() {
//                @Override
//                public void onCallback(User user) {
//                    data.add(user);
//                    addAll(data);
//                    notifyDataSetChanged();
//                }
//            });
//        }

//        data.sort(new Comparator<User>() {
//            @Override
//            public int compare(User user1, User user2) {
//                return user1.getFirstName().compareTo(user2.getFirstName());
//            }
//        });
    }

    @Override
    public void update(Observable observable, Object o) {
        refreshData(null);
    }
}
