package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Highscore
{
    private static Preferences prefs;

    public static void load() {

        prefs = Gdx.app.getPreferences("PyroGame");

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
	}
    }

    public static void setHighscore(int score) {
        prefs.putInteger("highScore", score);
        prefs.flush();
    }

    public static int getHighscore() {
        return prefs.getInteger("highScore");
    }
}
