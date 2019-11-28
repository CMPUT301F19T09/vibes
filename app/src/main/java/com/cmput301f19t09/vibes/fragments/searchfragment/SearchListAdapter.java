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
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchListAdapter extends ArrayAdapter<String> implements Observer {
    protected List<String> data;
    private Context context;

    SearchListAdapter(Context context) {
        super(context, 0);
        this.context = context;
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.search_list_item, parent, false);

        }

        final ImageView userProfile = item.findViewById(R.id.search_profile_picture);
        final TextView userFullName = item.findViewById(R.id.search_full_name);
        final TextView userUserName = item.findViewById(R.id.search_username);

        String userUID = data.get(position);

        if (userUID == null) {
            return item;
        }

        final User user = UserManager.getUser(userUID);

        if (user.isLoaded()) {
            Glide.with(getContext()).load(user.getProfileURL()).into(userProfile);
            userProfile.setClipToOutline(true);

            userFullName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            userUserName.setText(user.getUserName());

        }

        UserManager.addUserObserver(userUID, new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                Glide.with(getContext()).load(user.getProfileURL()).into(userProfile);
                userProfile.setClipToOutline(true);

                userFullName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                userUserName.setText(user.getUserName());
            }
        });
        return item;
    }

    void refreshData(List<String> userList) {
        data.clear();
        clear();
        notifyDataSetChanged();

        if (userList == null) {
            return;
        }

        data.addAll(userList);
        addAll(data);
    }

    @Override
    public void update(Observable observable, Object o) {
        refreshData(null);
    }
}
