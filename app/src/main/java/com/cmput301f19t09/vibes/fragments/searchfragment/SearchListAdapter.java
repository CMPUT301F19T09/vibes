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

/**
 * SearchListAdapter is an ArrayAdapter used in the search fragment to display user information
 */
public class SearchListAdapter extends ArrayAdapter<String> implements Observer {
    protected List<String> data;
    private Context context;

    /**
     * Constructs the adapter
     * @param context Get's the context associated with the adapter
     */
    SearchListAdapter(Context context) {
        super(context, 0);
        this.context = context;
        this.data = new ArrayList<>();
    }

    /**
     * Constructs the view of a single item. Gets user's full name, username, and profile picture
     * @param position Position to set in the list
     * @param convertView Gets the view of the search fragment
     * @param parent Gets the parent ViewGroup
     * @return The view of the single item
     */
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

        return item;
    }

    /**
     * Refreshes the adapter with the updated list of the search results
     * @param userList The updated search result list
     */
    void refreshData(List<String> userList) {
        data.clear();
        clear();

        for (String uid : userList)
        {
            UserManager.removeUserObservers(uid);
        }

        for (String uid : userList)
        {
            UserManager.addUserObserver(uid, new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    notifyDataSetChanged();
                }
            });
        }
        data.addAll(userList);
        addAll(data);
    }

    /**
     * Clears the list on update
     * @param observable Object that can notify observers
     * @param o Objects being passed in
     */
    @Override
    public void update(Observable observable, Object o) {
        refreshData(null);
    }
}
