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
        Pair<Integer, String> happiness = new Pair<>(R.drawable.emotion_image_happiness, "YELLOW");
        map.put("HAPPINESS", happiness);
        Pair<Integer, String> trust = new Pair<>(R.drawable.emotion_image_trust, "LIGHT_GREEN");
        map.put("TRUST", trust);
        Pair<Integer, String> fear = new Pair<>(R.drawable.emotion_image_fear, "GREEN");
        map.put("FEAR", fear);
        Pair<Integer, String> surprise = new Pair<>(R.drawable.emotion_image_surprise, "BLUE");
        map.put("SURPRISE", surprise);
        Pair<Integer, String> sadness = new Pair<>(R.drawable.emotion_image_sadness, "INDIGO");
        map.put("SADNESS", sadness);
        Pair<Integer, String> disgust = new Pair<>(R.drawable.emotion_image_disgust, "PURPLE");
        map.put("DISGUST", disgust);
        Pair<Integer, String> anger = new Pair<>(R.drawable.emotion_image_anger, "RED");
        map.put("ANGER", anger);
        Pair<Integer, String> anticipation = new Pair<>(R.drawable.emotion_image_anticipation, "ORANGE");
        map.put("ANTICIPATION", anticipation);
        Pair<Integer, String> love = new Pair<>(R.drawable.emotion_image_love, "PINK");
        map.put("LOVE", love);
    }
    private int file;
    private String colour;
    private String emotion;

    public EmotionalState(String emotion) {
        if (!map.containsKey(emotion)) throw new IllegalArgumentException(emotion+" is an invalid emotion.");
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
