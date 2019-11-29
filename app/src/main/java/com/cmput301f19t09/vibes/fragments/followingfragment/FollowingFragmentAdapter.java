package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * FollowingFragmentAdapter is an ArrayAdapter that is used for both ListView's
 * in FollowingFragment - following ListView and requested Listview
 */
public class FollowingFragmentAdapter extends ArrayAdapter<String> {
    private List<String> userList;
    private int viewMode;

    /**
     * Constructs a FollowingFragmentAdapter that displays either the following ListView or the
     * request ListView in the FollowingFragment based on the mode passed in.
     *
     * @param context : Get's the context to associate with the adapter
     * @param mode : Gets the mode to display - either "following" or "request"
     */
    FollowingFragmentAdapter(Context context, String mode){
        super(context, 0);
        this.userList = new ArrayList<>();

        if (mode.equals("following")) {
            viewMode = R.layout.following_list;
        } else {
            viewMode = R.layout.requested_list;
        }
    }

    /**
     * Constructs the view of a single item. Get's the user of the item and displays the user's
     * full name, username, and profile picture. Displays the accept and deny request button only
     * if the layout is the requested ListView
     *
     * @param position : int
     * @param convertView : View
     * @param parent : ViewGroup
     * @return view
     *
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(viewMode, parent, false);
        }

        final TextView fullNameText = view.findViewById(R.id.fullName);
        final TextView usernameText = view.findViewById(R.id.username);
        final ImageView userImage = view.findViewById(R.id.profileImage);

        String userUID = userList.get(position);

        if (userUID == null) {
            return view;
        }

        final User user = UserManager.getUser(userUID);

        // Verifies if the user is loaded before loading the user's information
        if (user.isLoaded()) {
            Glide.with(getContext()).load(user.getProfileURL()).into(userImage);
            userImage.setClipToOutline(true);

            fullNameText.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            usernameText.setText(user.getUserName());
        } else {
            // Creates an observer if for the user before loading the user's information
            user.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    Glide.with(getContext()).load(user.getProfileURL()).into(userImage);
                    userImage.setClipToOutline(true);

                    fullNameText.setText(String.format("%s %s", user.getFirstName(),
                            user.getLastName()));
                    usernameText.setText(user.getUserName());

                    user.deleteObserver(this);
                }
            });
        }

        view.setOnClickListener(v -> goToProfile(userUID));

        // Button listeners only if on the requested ListView layout
        if (viewMode == R.layout.requested_list) {
            Button confirmButton = view.findViewById(R.id.btn_confirm);
            confirmButton.setOnClickListener(v -> {
                User currentUser = UserManager.getCurrentUser();
                if (currentUser != null) {
                    currentUser.acceptRequest(user.getUid());
                }
            });

            Button deleteButton = view.findViewById(R.id.btn_delete);
            deleteButton.setOnClickListener(v -> {
                User currentUser = UserManager.getCurrentUser();
                if (currentUser != null) {
                    currentUser.removeRequest(user.getUid());
                }
            });
        }

        return view;
    }

    /**
     * Refreshes the data
     * @param uidList
     */
    void refreshData(List<String> uidList) {

        userList.clear();
        clear();

        for (String id : userList) {
            UserManager.removeUserObservers(id);
        }

        if (uidList == null) {
            return;
        }

        for (String id : uidList) {
            UserManager.addUserObserver(id, new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    notifyDataSetChanged();
                }
            });
        }

        userList.addAll(uidList);
        addAll(uidList);
    }

    /**
     *
     * @param uid
     */
    private void goToProfile(String uid) {
        User user = UserManager.getUser(uid);
        if (user.isLoaded()) {
            ((MainActivity) getContext()).setProfileFragment(user.getUid());
        }
    }
}