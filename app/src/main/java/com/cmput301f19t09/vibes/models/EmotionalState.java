package com.cmput301f19t09.vibes.models;

import android.util.Pair;

import com.cmput301f19t09.vibes.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

enum ColourEnum {
    YELLOW (0xffffff00),
    LIGHT_GREEN (0xffccff99),
    GREEN (0xff009900),
    BLUE (0xff0000ff),
    INDIGO (0xff4b0082),
    PURPLE (0xff800080),
    RED (0xffff0000),
    ORANGE (0xffffa500),
    PINK (0xffffc0ce);

    private int value;

    ColourEnum(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}

public class EmotionalState implements Serializable {

    private static final Map<String, Pair> map = new HashMap<>();
    static {
        //Ref: https://stackoverflow.com/questions/8229473/hashmap-one-key-multiple-values#8229518

        Pair<Integer, Integer> happiness = new Pair<>(R.drawable.emotion_image_happiness, ColourEnum.YELLOW.getValue());
        map.put("HAPPINESS", happiness);
        Pair<Integer, Integer> trust = new Pair<>(R.drawable.emotion_image_trust, ColourEnum.LIGHT_GREEN.getValue());
        map.put("TRUST", trust);
        Pair<Integer, Integer> fear = new Pair<>(R.drawable.emotion_image_fear, ColourEnum.GREEN.getValue());
        map.put("FEAR", fear);
        Pair<Integer, Integer> surprise = new Pair<>(R.drawable.emotion_image_surprise, ColourEnum.BLUE.getValue());
        map.put("SURPRISE", surprise);
        Pair<Integer, Integer> sadness = new Pair<>(R.drawable.emotion_image_sadness, ColourEnum.INDIGO.getValue());
        map.put("SADNESS", sadness);
        Pair<Integer, Integer> disgust = new Pair<>(R.drawable.emotion_image_disgust, ColourEnum.PURPLE.getValue());
        map.put("DISGUST", disgust);
        Pair<Integer, Integer> anger = new Pair<>(R.drawable.emotion_image_anger, ColourEnum.RED.getValue());
        map.put("ANGER", anger);
        Pair<Integer, Integer> anticipation = new Pair<>(R.drawable.emotion_image_anticipation, ColourEnum.ORANGE.getValue());
        map.put("ANTICIPATION", anticipation);
        Pair<Integer, Integer> love = new Pair<>(R.drawable.emotion_image_love, ColourEnum.PINK.getValue());
        map.put("LOVE", love);
    }
    private int file;
    private int colour;
    private String emotion;

    public EmotionalState(String emotion) {
        if (!map.containsKey(emotion)) throw new IllegalArgumentException(emotion+" is an invalid emotion.");
        this.emotion = emotion;
        Pair pair = map.get(this.emotion);
        this.file = (int) pair.first;
        this.colour = (int) pair.second;
    }

    public String getEmotion(){
        return emotion;
    }

    public int getImageFile() {
        return this.file;
    }

    public int getColour() {
        return this.colour;
    }

    public static Map<String, Pair> getMap(){
        return map;
    }

    public static ArrayList<String> getListOfKeys() {
        return new ArrayList<>(map.keySet());
    }
}
