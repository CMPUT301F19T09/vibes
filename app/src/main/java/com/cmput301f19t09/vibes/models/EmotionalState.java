package com.cmput301f19t09.vibes.models;

import android.graphics.Color;
import android.util.Pair;

import com.cmput301f19t09.vibes.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration associates each colour name with its rgb value.
 *
 * To get the rgb value of any colour in this enumeration, use
 * ColourEnum.COLOUR.value
 */
enum ColourEnum {
    // Colours and their values.

    YELLOW (0xffffcd05),
    LIGHT_GREEN (0xff24ff53),
    GREEN (0xff009900),
    BLUE (0xff29b4ff),
    INDIGO (0xff4b0082),
    PURPLE (0xff800080),
    RED (0xffff0000),
    ORANGE (0xffffa500),
    PINK (0xffffc0ce);

    public final int value;

    /**
     * @param value : int
     *
     * Sets value to the value passed in
     */
    ColourEnum(int value) {
        this.value = value;
    }

    /**
     * @return value : int
     *
     * Returns the value of the ColourEnum
     */
    /*public int value{
        return this.value;
    }*/
}

public class EmotionalState implements Serializable {

    // Maps each name of each colour as a string to a pair (R.drawable, rgbColourValue)
    private static final Map<String, Pair> map = new HashMap<>();
    static {
        // For every emotion, the map is given a key (the name of the emotion as a string) and a value
        // (a pair, first element is the drawable, the second is the rgb value of the colour)
        Pair<Integer, Integer> happiness = new Pair<>(R.drawable.ic_happy, ColourEnum.YELLOW.value);
        map.put("HAPPINESS", happiness);
        Pair<Integer, Integer> trust = new Pair<>(R.drawable.ic_iconfinder_man_wink_2411818, ColourEnum.LIGHT_GREEN.value);
        map.put("TRUST", trust);
        Pair<Integer, Integer> fear = new Pair<>(R.drawable.ic_iconfinder_man_atonished_2411845, ColourEnum.GREEN.value);
        map.put("FEAR", fear);
        Pair<Integer, Integer> surprise = new Pair<>(R.drawable.ic_iconfinder_man_surprised_2411823, ColourEnum.BLUE.value);
        map.put("SURPRISE", surprise);
        Pair<Integer, Integer> sadness = new Pair<>(R.drawable.ic_iconfinder_man_sad_2411830, ColourEnum.INDIGO.value);
        map.put("SADNESS", sadness);
        Pair<Integer, Integer> disgust = new Pair<>(R.drawable.ic_iconfinder_man_sick_2411828, ColourEnum.PURPLE.value);
        map.put("DISGUST", disgust);
        Pair<Integer, Integer> anger = new Pair<>(R.drawable.ic_iconfinder_man_angry_2411816, ColourEnum.RED.value);
        map.put("ANGER", anger);
        Pair<Integer, Integer> anticipation = new Pair<>(R.drawable.ic_iconfinder_man_neutral_face_2411832, ColourEnum.ORANGE.value);
        map.put("ANTICIPATION", anticipation);
        Pair<Integer, Integer> love = new Pair<>(R.drawable.ic_iconfinder_man_in_love_2411836, ColourEnum.PINK.value);
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

    /**
     * @return emotion : String
     *
     * Returns the name of the emotion as a String
     */
    public String getEmotion(){
        return emotion;
    }

    /**
     * @return file : int
     *
     * Returns the layout file of the image of the emotion.
     */
    public int getImageFile() {
        return this.file;
    }

    /**
     * @return colour : int
     *
     * Returns the rgb value of the colour corresponding to the mood
     */
    public int getColour() {
        return this.colour;
    }

    /**
     * @return map : HashMap<>
     *
     * Returns map, the static HashMap where the keys are the emotion
     * names and the values are Pair (first is image resource, second is rgb value of colour)
     */
    public static Map<String, Pair> getMap(){
        return map;
    }

    /**
     * @return keys : ArrayList<>
     *
     * Returns the keys of map
     */
    public static ArrayList<String> getListOfKeys() {
        return new ArrayList<>(map.keySet());
    }
}
