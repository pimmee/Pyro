package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.tools.Manager.Level;


public class Highscore
{
    private Manager manager;
    private Preferences prefs;
    public static final int MAX_SCORES = 5;

    public Highscore(Manager manager) {
        this.manager = manager;
    }

    public void load() {

        prefs = Gdx.app.getPreferences("PyroGame");
        for(Level level : Level.values()) { // Creates prefs with key HighScore0MENS
            if (!prefs.contains("HighScore0" + level)) prefs.putInteger("HighScore0" + level, 0);
            if (!prefs.contains("HighScore1" + level)) prefs.putInteger("HighScore1" + level, 0);
            if (!prefs.contains("HighScore2" + level)) prefs.putInteger("HighScore2" + level, 0);
            if (!prefs.contains("HighScore3" + level)) prefs.putInteger("HighScore3" + level, 0);
            if (!prefs.contains("HighScore4" + level)) prefs.putInteger("HighScore4" + level, 0);
        }
        if (!prefs.contains("HighScore1TOTAL")) prefs.putInteger("HighScore1TOTAL", 0);
        if (!prefs.contains("HighScore2TOTAL")) prefs.putInteger("HighScore2TOTAL", 0);
        if (!prefs.contains("HighScore3TOTAL")) prefs.putInteger("HighScore3TOTAL", 0);
        if (!prefs.contains("HighScore4TOTAL")) prefs.putInteger("HighScore4TOTAL", 0);
        if (!prefs.contains("HighScore5TOTAL")) prefs.putInteger("HighScore5TOTAL", 0);
    }

    public void reset() {
        prefs = Gdx.app.getPreferences("PyroGame");
        for (Level level : Level.values()) { // Creates prefs with key HighScore0MENS
            prefs.putInteger("HighScore0" + level, 0);
            prefs.putInteger("HighScore1" + level, 0);
            prefs.putInteger("HighScore2" + level, 0);
            prefs.putInteger("HighScore3" + level, 0);
            prefs.putInteger("HighScore4" + level, 0);
        }
        prefs.putInteger("HighScore0TOTAL", 0);
        prefs.putInteger("HighScore1TOTAL", 0);
        prefs.putInteger("HighScore2TOTAL", 0);
        prefs.putInteger("HighScore3TOTAL", 0);
        prefs.putInteger("HighScore4TOTAL", 0);
        prefs.flush();
    }

    public void setHighScore(int score) {
        Array<Integer> highScores = new Array<>();
        highScores.add(score); // Add score to list
        for (int i = 0; i < MAX_SCORES; i++)
            highScores.add(getHighScore(i)); // Add previous scores to list
        highScores.sort();
        highScores.reverse(); // Highest score first in list
        highScores.removeIndex(highScores.size - 1);// Remove lowest score
        for (int i = 0; i < highScores.size; i++) {
            if (manager.getCompletedLevels() != null && manager.getCompletedLevels().size == 4)
                prefs.putInteger("HighScore" + i + "TOTAL", highScores.get(i));
            else
                prefs.putInteger("HighScore" + i + manager.getCurrentLevel(), highScores.get(i)); // Writes scores in correct order
        }
        prefs.flush();
    }

    public int getHighScore(int i) {
        if (manager.getCompletedLevels() != null && manager.getCompletedLevels().size == 4)
            return prefs.getInteger("HighScore" + i + "TOTAL");
        else
            return prefs.getInteger("HighScore" + i + manager.getCurrentLevel());
    }

}
