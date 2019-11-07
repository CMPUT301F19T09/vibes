package com.cmput301f19t09.vibes.models;

import android.util.Pair;

import com.cmput301f19t09.vibes.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EmotionalState implements Serializable {

    private static final Map<String, Pair> map = new HashMap<>();
    static {
        //Ref: https://stackoverflow.com/questions/8229473/hashmap-one-key-multiple-values#8229518
        Pair<Integer, String> happy = new Pair<>(R.drawable.happy_emotion, "RED");
        map.put("HAPPY", happy);
        Pair<Integer, String> sad = new Pair<>(R.drawable.sad_emotion, "BLUE");
        map.put("SAD", sad);
    }
    private int file;
    private String colour;
    private String emotion;

    public EmotionalState(String emotion) {
        if (!map.containsKey(emotion)) throw new IllegalArgumentException("invalid emotion");
        this.emotion = emotion;
        Pair pair = map.get(this.emotion);
        this.file = (int) pair.first;
        this.colour = (String) pair.second;
    }

    public String getEmotion(){
        return emotion;
    }

    public int getImageFile() {
        return this.file;
    }

    public String getColour() {
        return this.colour;
    }

    public static Map<String, Pair> getMap(){
        return map;
    }
}
