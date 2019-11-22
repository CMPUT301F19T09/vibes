package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.cmput301f19t09.vibes.MainActivity;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.fragments.profilefragment.ProfileFragment;
import com.cmput301f19t09.vibes.models.User;
import com.cmput301f19t09.vibes.models.UserManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * FollowingFragmentAdapter is an ArrayAdapter that is used for both ListView's
 * in FollowingFragment
 */
public class FollowingFragmentAdapter extends ArrayAdapter<User> {
    private ArrayList<User> userList;
    private Context context;
    private int layout;
    private FragmentActivity activity;

    /**
     * @param context : Context
     * @param userList : ArrayList<User>
     *
     * Constructs a FollowingFragmentAdapter, requires a passed layout (using setLayout())
     * and a passed FragmentActivity (using setActivity()) to be functional
     */
    public FollowingFragmentAdapter(Context context, ArrayList<User> userList){
        super(context, 0, userList);
        this.userList = userList;
        this.context = context;
    }

    /**
     * @param position : int
     * @param convertView : View
     * @param parent : ViewGroup
     * @return view
     *
     * For every item in the userList passed in the constructor, the username,
     * first name, last name, and user picture of the layout are set to the values
     * corresponding to the user. A user's profile can be opened by clicking on the
     * profile image. The list is sorted before the view is returned.
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(this.layout, parent, false);
        }

        // For user at position in list
        final User user = userList.get(position);

        if (user == null)
        {
            return view;
        }

        // Sets the fullNameText to the user's firstName + lastName
        TextView fullNameText = view.findViewById(R.id.fullName);
        String fullName = user.getFirstName() + " " + user.getLastName();
        fullNameText.setText(fullName);

        // Sets the usernameText to the user's username
        TextView usernameText = view.findViewById(R.id.username);
        String username = user.getUserName();
        usernameText.setText(username);

        // Sets the user's profile picture
        ImageView userImage = view.findViewById(R.id.profileImage);
        Glide.with(getContext()).load(user.getProfileURL()).into(userImage);
        userImage.setClipToOutline(true);

        // Sets an OnClickListener for userImage
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // A ProfileFragment that corresponds to the clicked on user
                // is created and is made the current fragment
                ProfileFragment profileFragment;
                //profileFragment = ProfileFragment.newInstance(user.getUid());
                ((MainActivity) activity).setProfileFragment(user.getUid());
            }
        });

        Button confirmButton = view.findViewById(R.id.btn_confirm);
        System.out.println(confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User currentUser = UserManager.getCurrentUser();
                currentUser.acceptRequest(user.getUid());
            }
        });

        Button deleteButton = view.findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User currentUser = UserManager.getCurrentUser();
                currentUser.removeRequest(user.getUid());
            }
        });

        Collections.sort(this.userList, User.sortByName);
        notifyDataSetChanged();

        return view;
    }

    /**
     * @param layout ; int
     *
     *
     * This function must be called before connecting the ArrayAdapter
     * to the ListView.
     *
     * Desired layout for the item in the listview.
     */
    public void setLayout(int layout){
        this.layout = layout;
    }

    /**
     * @param activity ; FragmentActivity
     *
     *
     * This function must be called before connecting the ArrayAdapter
     * to the ListView.
     *
     * Saves the current activity. Necessary for changing the fragment
     */
    public void setActivity(FragmentActivity activity){
        this.activity = activity;
    }

    /**
     *
     * @param position : int
     * @return
     *
     * Disables onItemClick
     */
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}