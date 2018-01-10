package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class Highscore
{
    private static Preferences prefs;
    public static void load() {

        prefs = Gdx.app.getPreferences("PyroGame");

        if (!prefs.contains("HighScore0")) prefs.putInteger("HighScore0", 0);
        if (!prefs.contains("HighScore1")) prefs.putInteger("HighScore1", 0);
        if (!prefs.contains("HighScore2")) prefs.putInteger("HighScore2", 0);
        if (!prefs.contains("HighScore3")) prefs.putInteger("HighScore3", 0);
        if (!prefs.contains("HighScore4")) prefs.putInteger("HighScore4", 0);
    }

    public static void setHighScore(int score) {
        Array<Integer> highScores = new Array<Integer>();
        highScores.add(score);
        for (int i = 0; i < 5; i++)
            highScores.add(getHighScore(i));
        highScores.sort();
        highScores.removeIndex(highScores.size - 1);
        for (int i = 0; i < highScores.size; i++)
            prefs.putInteger("HighScore" + i, highScores.get(i));
        prefs.flush();
    }

    public static int getHighScore(int i) {
        return prefs.getInteger("HighScore" + i);
    }

}
