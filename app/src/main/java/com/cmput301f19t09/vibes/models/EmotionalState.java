package com.cmput301f19t09.vibes.models;

import android.util.Pair;

import com.cmput301f19t09.vibes.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This enumeration associates each colour name with its rgb value.
 *
 * To get the rgb value of any colour in this enumeration, use
 * ColourEnum.COLOUR.getValue()
 */
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

    // Maps each name of each colour as a string to a pair (R.drawable, rgbColourValue)
    private static final Map<String, Pair> map = new HashMap<>();
    static {
        // For every emotion, the map is given a key (the name of the emotion as a string) and a value
        // (a pair, first element is the drawable, the second is the rgb value of the colour)
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

    /**
     * @param emotion : String
     *
     * The name of the parameter must be one of the following:
     * HAPPINESS, TRUST, FEAR, SURPRISE, SADNESS, DISGUST, ANGER, ANTICIPATION, LOVE
     *
     * Given the name of an emotion as a string, creates an emotion object
     * and sets the variables emotion, file, and colour
     */
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


    /**
     * @return map : HashMap<>
     *
     * Returns map, the static HashMap
     */
    public static Map<String, Pair> getMap(){
        return map;
    }

    /**
     * @return arrayList : ArrayList<String>
     *
     * Returns arrayList, a list of all of the colours
     */
    public static ArrayList<String> getColourList() {
        Set<String> set = map.keySet();
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(set);
        return arrayList;
    }
}
