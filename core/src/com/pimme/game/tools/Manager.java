package com.pimme.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

/**
 * Created by Simon on 2/22/2018.
 */

public class Manager {

    private PyroGame game;
    private AssetManager assetManager;
    private Music music;
    private Level currentLevel;
    private Array<Level> completedLevels;
    private Highscore highscore;
    private int totalHighscore;
    public static final int LEVELS_AMOUNT = 4;
    public static final int MAX_LIVES = 3;
    private int livesLeft = MAX_LIVES;

    public enum Level {
        LEVEL1,
        LEVEL2,
        LEVEL3,
        BOUNCE,
    }
    public Manager(PyroGame game) {
        this.game = game;
        assetManager = new AssetManager();
        highscore = new Highscore(this);

        loadSounds();
    }

    public void render() {
        if (game.getScreen() instanceof PlayScreen)
            playMusic();
        else
            stopmusic();
    }

    public void loseLife() {
        if (livesLeft > 0) livesLeft--;
    }

    public int getLivesLeft() {
        return livesLeft;
    }

    public void resetLivesLeft() {
        livesLeft = MAX_LIVES;
    }

    public void resetCompletedLevels() {
        completedLevels = new Array<>();
    }

    public void setCompletedLevelsNull() {
        completedLevels = null;
    }

    public Array<Level> getCompletedLevels() {
        return completedLevels;
    }

    public void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getHighScore(int i) {
        return highscore.getHighScore(i);
    }

    public void setHighScore(int score) {
        highscore.setHighScore(score);
    }

    public void setTotalHighscore(int amount) {
        totalHighscore = amount;
    }

    public int getTotalHighscore() {
        return totalHighscore;
    }

    public void loadHighScores() {
        highscore.load();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }


    private void loadSounds() {
        assetManager.load("audio/music/Avener_lonely_boy.mp3", Music.class);
        assetManager.load("audio/music/woohoo.mp3", Music.class);
        assetManager.load("audio/music/fadeintoyou.mp3", Music.class);
        assetManager.load("audio/music/panama.mp3", Music.class);

        assetManager.load("audio/sounds/coin2.wav", Sound.class);
        assetManager.load("audio/sounds/tampon.wav", Sound.class);
        assetManager.load("audio/sounds/bounce.wav", Sound.class);
        assetManager.load("audio/sounds/goal_reached.wav", Sound.class);
        assetManager.load("audio/sounds/enemyBounce.wav", Sound.class);
        assetManager.load("audio/sounds/healthpack.wav", Sound.class);
        assetManager.finishLoading();
    }


    private void playMusic() {
        if (currentLevel.equals(Level.LEVEL1)) music = assetManager.get("audio/music/Avener_lonely_boy.mp3", Music.class);
        if (currentLevel.equals(Level.LEVEL2)) music = assetManager.get("audio/music/woohoo.mp3", Music.class);
        if (currentLevel.equals(Level.LEVEL3)) music = assetManager.get("audio/music/panama.mp3", Music.class);
        if (currentLevel.equals(Level.BOUNCE)) music = assetManager.get("audio/music/fadeintoyou.mp3", Music.class);
        music.setVolume(0.4f);
        music.play();
    }

    private void stopmusic() {
        if (music != null) music.stop();
    }
}
