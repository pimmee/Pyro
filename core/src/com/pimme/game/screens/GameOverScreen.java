package com.pimme.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pimme.game.PyroGame;
import com.pimme.game.tools.Highscore;
import com.pimme.game.tools.Manager;
import com.pimme.game.tools.Manager.Level;
import com.pimme.game.tools.Utils;

/**
 * Created by smurf on 2017-11-16.
 */
public class GameOverScreen implements Screen {
    private PyroGame game;
    private Manager manager;
    private Viewport viewPort;
    private Stage stage;
    private Label gameOverLabel;

    private TextButton playAgainButton;
    private TextButton menuButton;
    private TextButton exitButton;

    public GameOverScreen(PyroGame game, int score) {
        this.game = game;
        manager = game.getManager();

        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, game.batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);
        initButtons();

        manager.loadHighScores();
        gameOverLabel = new Label("GAME OVER", Utils.skin);
        gameOverLabel.setFontScale(1.5f);

        setGameStatus();

        table.add(gameOverLabel).colspan(3).row();
        table.add(playAgainButton).colspan(3).row();
        table.add(menuButton).colspan(3).row();
        table.add(exitButton).colspan(3).row();
        table.add(new Label("Highscores:      ", Utils.skin));
        table.add(new Label("Score:   ", Utils.skin));
        table.add(new Label(Integer.toString(score), Utils.skin.get("green", LabelStyle.class))).left().row();

        for (int i = 0; i < Highscore.MAX_SCORES; i ++) {   // Show highscores
            table.add(new Label(Integer.toString(i + 1) + ".         " + Integer.toString(manager.getHighScore(i)), Utils.skin)).left().row();
       }
    }

    @Override
    public void show() {

    }

    private void setGameStatus() {
        if (manager.getCompletedLevels() != null) { // If story mode
            manager.loseLife();
            if (manager.getLivesLeft() == 0 || manager.getCurrentLevel().equals(Level.LEVEL1)) { // If out of lives or level 1
                manager.setCurrentLevel(Level.LEVEL1);
                manager.resetCompletedLevels();
                manager.resetLivesLeft();
                manager.setTotalHighscore(0);
            } else {
                gameOverLabel.setText("LIVES " + manager.getLivesLeft());
                playAgainButton.setText("Try again");
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    private void initButtons() {
        playAgainButton = new TextButton("Restart", Utils.skin);
        menuButton = new TextButton("Menu", Utils.skin);
        exitButton = new TextButton("Exit", Utils.skin);

        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playAgainButton.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playAgainButton.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                menuButton.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                menuButton.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
