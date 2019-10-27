package com.example.followinglist;

import android.media.Image;

import java.util.ArrayList;

public class EmotionData {
    private  ArrayList<Image> images;

    public EmotionData(){
    }

    public Image getImage(int emotion){
        return images.get(emotion);
    }
}
