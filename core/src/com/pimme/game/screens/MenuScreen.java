package com.pimme.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pimme.game.PyroGame;
import com.pimme.game.tools.Manager.Level;
import com.pimme.game.tools.Manager;
import com.pimme.game.tools.Utils;

public class MenuScreen implements Screen
{
    private PyroGame game;
    private Manager manager;
    private FitViewport viewPort;
    private Stage stage;
    private Table table;

    private TextButton playButton;
    private TextButton exitButton;
    private TextButton backButton;
    private TextButton level1;
    private TextButton level2;
    private TextButton level3;
    private TextButton bounceLevel;
    private TextButton playAll;

    public MenuScreen(PyroGame game) {
        this.game = game;

        manager = game.getManager();

        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, game.batch);
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);

        initButtons();
        mainMenu();
    }

    @Override public void show() {

    }

    @Override public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    private void initButtons() {
        playButton = new TextButton("Play", Utils.skin);
        exitButton = new TextButton("Exit", Utils.skin);
        backButton = new TextButton("Back", Utils.skin);
        playAll = new TextButton(".", Utils.skin);
        level1 = new TextButton("Level 1", Utils.skin);
        level2 = new TextButton("Level 2", Utils.skin);
        level3 = new TextButton("Level 3", Utils.skin);
        bounceLevel = new TextButton("Level 4", Utils.skin);

        playButton.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                manager.setCurrentLevel(Level.LEVEL1);
                game.setScreen(new PlayScreen(game));
                dispose();
            }

            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playButton.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playButton.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }

        });
        exitButton.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });
        backButton.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                mainMenu();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

        playAll.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                selectLevel();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playAll.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playAll.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

        level1.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                manager.setCurrentLevel(Level.LEVEL1);
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                level1.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                level1.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

        level2.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                manager.setCurrentLevel(Level.LEVEL2);
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                level2.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                level2.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });
        level3.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                manager.setCompletedLevelsNull();
                manager.setCurrentLevel(Level.LEVEL3);
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                level3.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                level3.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

        bounceLevel.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                manager.setCompletedLevelsNull();
                manager.setCurrentLevel(Level.BOUNCE);
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                bounceLevel.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                bounceLevel.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

    }

    private void mainMenu() {
        manager.resetCompletedLevels();

        table.clear();
        table.add(playButton).row();
        table.add(exitButton).row();
        table.add(playAll).right().expandX().top().row();
    }

    private void selectLevel() {
        manager.setCompletedLevelsNull();

        table.clear();
        table.add(level1).row();
        table.add(level2).row();
        table.add(level3).row();
        table.add(bounceLevel).row();
        table.add(backButton).row();
    }

    @Override public void resize(final int width, final int height) {
        viewPort.update(width, height, true);
    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public void hide() {

    }

    @Override public void dispose() {
        stage.dispose();
    }
}
