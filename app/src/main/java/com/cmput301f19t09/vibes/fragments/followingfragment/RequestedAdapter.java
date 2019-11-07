package com.cmput301f19t09.vibes.fragments.followingfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.cmput301f19t09.vibes.R;
import com.cmput301f19t09.vibes.models.User;

import java.util.ArrayList;

public class RequestedAdapter extends ArrayAdapter<User> {
    private ArrayList<User> userList;
    private Context context;

    public RequestedAdapter(Context context, ArrayList<User> userList){
        super(context, 0, userList);
        this.userList = userList;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.requested_list, parent, false);
        }

        User user = userList.get(position);

        TextView fullNameText = view.findViewById(R.id.fullName);
        String fullName = user.getFirstName() + " " + user.getLastName();
        fullNameText.setText(fullName);

        TextView usernameText = view.findViewById(R.id.username);
        String username = user.getUserName();
        usernameText.setText(username);

        return view;
    }
}