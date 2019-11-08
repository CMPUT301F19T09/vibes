package com.cmput301f19t09.vibes.fragments.editfragment;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cmput301f19t09.vibes.models.EmotionalState;

import java.util.ArrayList;
import java.util.Map;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> stateKeys;
    private Map<String, Pair> stateMap;

    public ImageAdapter(Context c) {
        mContext = c;
        stateKeys = EmotionalState.getListOfKeys();
        stateMap = EmotionalState.getMap();
    }

    public int getCount() {
        return stateKeys.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else {
            imageView = (ImageView) convertView;
        }
        Pair state = stateMap.get(stateKeys.get(position));
        imageView.setImageResource((Integer) state.first);
        imageView.setColorFilter((Integer) state.second);
        return imageView;
    }
}
