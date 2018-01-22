package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.PyroGame.Level;

public final class Highscore
{
    private static Preferences prefs;
    public static final int MAX_SCORES = 5;

    private Highscore() {}

    public static void load() {

        prefs = Gdx.app.getPreferences("PyroGame");
        for(Level level : Level.values()) { // Creates prefs with key HighScore0MENS
            if (!prefs.contains("HighScore0" + level)) prefs.putInteger("HighScore0" + level, 0);
            if (!prefs.contains("HighScore1" + level)) prefs.putInteger("HighScore1" + level, 0);
            if (!prefs.contains("HighScore2" + level)) prefs.putInteger("HighScore2" + level, 0);
            if (!prefs.contains("HighScore3" + level)) prefs.putInteger("HighScore3" + level, 0);
            if (!prefs.contains("HighScore4" + level)) prefs.putInteger("HighScore4" + level, 0);
        }
    }

    public static void reset() {
        for (Level level : Level.values()) { // Creates prefs with key HighScore0MENS
            prefs.putInteger("HighScore0" + level, 0);
            prefs.putInteger("HighScore1" + level, 0);
            prefs.putInteger("HighScore2" + level, 0);
            prefs.putInteger("HighScore3" + level, 0);
            prefs.putInteger("HighScore4" + level, 0);
        }
        prefs.flush();
    }

    public static void setHighScore(int score) {
        Array<Integer> highScores = new Array<Integer>();
        highScores.add(score); // Add score to list
        for (int i = 0; i < MAX_SCORES; i++)
            highScores.add(getHighScore(i)); // Add previous scores to list
        highScores.sort();
        highScores.reverse(); // Highest score first in list
        highScores.removeIndex(highScores.size - 1);// Remove lowest score
        for (int i = 0; i < highScores.size; i++)
            prefs.putInteger("HighScore" + i + PyroGame.getCurrentLevel(), highScores.get(i)); // Writes scores in correct order
        prefs.flush();
    }

    public static int getHighScore(int i) {
        return prefs.getInteger("HighScore" + i + PyroGame.getCurrentLevel());
    }

}
