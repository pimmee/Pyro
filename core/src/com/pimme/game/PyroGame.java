package com.pimme.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.screens.MenuScreen;
import com.pimme.game.screens.PlayScreen;
import com.pimme.game.tools.Graphics;
import com.pimme.game.tools.Highscore;
import com.pimme.game.tools.Utils;

public class PyroGame extends Game
{
    public SpriteBatch batch;
    public static int V_WIDTH = 700;
    public static int V_HEIGHT = 400;
    public static final int SWIM_V_WIDTH = 400;
    public static final int SWIM_V_HEIGHT = 250;

    public static final float PPM = 100; // pixel per meter
    public static final float FPS = 60;

    public static final short NOTHING_BIT = 0;
    public static final short BRICK_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short ENEMY_HEAD_BIT = 16;
    public static final short SPIKE_BIT = 32;
    public static final short BOUNCE_BIT = 64;
    public static final short TAMPON_BIT = 128;
    public static final short GOAL_BIT = 256;
    public static final short HP_BIT = 512;

    public static Level currentLevel;
    public static AssetManager manager;
    public static Array<Level> completedLevels;
    public static Music music;

    public enum Level {
        MENS,
        MENS2,
        MENS3,
        BOUNCE,
    }

    @Override
    public void create () {
        batch = new SpriteBatch();
        Graphics.init();
        Utils.load();
        manager = new AssetManager();
        loadSounds();
        //Highscore.reset();
        setScreen(new MenuScreen(this));
    }

    private void loadSounds() {
        manager.load("audio/music/Avener_lonely_boy.mp3", Music.class);
        manager.load("audio/music/woohoo.mp3", Music.class);
        manager.load("audio/music/fadeintoyou.mp3", Music.class);
        manager.load("audio/music/panama.mp3", Music.class);
        manager.load("audio/sounds/coin2.wav", Sound.class);
        manager.load("audio/sounds/bounce.wav", Sound.class);
        manager.load("audio/sounds/enemyBounce.wav", Sound.class);
        manager.load("audio/sounds/healthpack.wav", Sound.class);
        manager.finishLoading();
    }

    @Override
    public void render() {
        super.render();
        if (getScreen() instanceof PlayScreen)
            playMusic();
        else stopmusic();
    }

    private void playMusic() {
        if (PyroGame.currentLevel.equals(Level.MENS)) music = PyroGame.manager.get("audio/music/Avener_lonely_boy.mp3", Music.class);
        if (PyroGame.currentLevel.equals(Level.MENS2)) music = PyroGame.manager.get("audio/music/woohoo.mp3", Music.class);
        if (PyroGame.currentLevel.equals(Level.MENS3)) music = PyroGame.manager.get("audio/music/panama.mp3", Music.class);
        if (PyroGame.currentLevel.equals(Level.BOUNCE)) music = PyroGame.manager.get("audio/music/fadeintoyou.mp3", Music.class);
        music.setVolume(0.4f);
        music.play();
    }

    private void stopmusic() {
        if (music != null) music.stop();
    }

    @Override
    public void dispose () {
        super.dispose();
        batch.dispose();
        manager.dispose();
    }
}
