package com.pimme.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pimme.game.graphics.MenuScreen;
import com.pimme.game.tools.Highscore;
import com.pimme.game.tools.Utils;

public class PyroGame extends Game
{
    public SpriteBatch batch;
    public static final int V_WIDTH = 700;
    public static final int V_HEIGHT = 400;
    public static final float PPM = 100; // pixel per meter
    public static final float FPS = 60;
    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short FLY_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short HP_BIT = 16;
    public static final short SPIKE_BIT = 32;
    public static final short BOUNCE_BIT = 64;
    public static final short TAMPON_BIT = 128;
    public static final short GOAL_BIT = 256;
    public static final short ENEMY_BIT = 512;

    private static Level currentLevel;

    public enum Level {
        MENS,
        BOUNCE,
        FLY,
        SWIM
    }

    @Override
    public void create () {
	batch = new SpriteBatch();
        Utils.load();
//        Highscore.load();
//        Highscore.reset();
	setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose () {
	super.dispose();
	batch.dispose();
    }

    public static void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    public static Level getCurrentLevel() {
        return currentLevel;
    }
}
