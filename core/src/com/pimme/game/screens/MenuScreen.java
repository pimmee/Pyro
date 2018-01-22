package com.pimme.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pimme.game.PyroGame;
import com.pimme.game.PyroGame.Level;
import com.pimme.game.tools.Utils;

public class MenuScreen implements Screen
{
    private final PyroGame game;
    private FitViewport viewPort;
    private Stage stage;
    private Table table;
    private Skin skin;

    private Array<TextButton> buttons;
    private TextButton playButton;
    private TextButton exitButton;
    private TextButton backButton;
    private TextButton mensLevel;
    private TextButton bounceLevel;
//    private TextButton flyLevel;
    private TextButton swimLevel;
    private TextButton mens2Level;

    //ÄNDRA FONT HOVER COLOR TILL font_joker_hover
    public MenuScreen(final PyroGame game) {
        this.game = game;

        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, game.batch);
        Gdx.input.setInputProcessor(stage);

        table = new Table();
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
        mensLevel = new TextButton("Mens Knas", Utils.skin);
        bounceLevel = new TextButton("Bouncy", Utils.skin);
        mens2Level = new TextButton("Mens Knas 2", Utils.skin);
        swimLevel = new TextButton("Swim", Utils.skin);

        playButton.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                selectLevel();
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
        mensLevel.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                PyroGame.currentLevel = Level.MENS;
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                mensLevel.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                mensLevel.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });
        bounceLevel.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                PyroGame.currentLevel = Level.BOUNCE;
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
        mens2Level.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                PyroGame.currentLevel = Level.MENS2;
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                mens2Level.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                mens2Level.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });
        swimLevel.addListener(new ClickListener()
        {
            @Override public void clicked(InputEvent event, float x, float y) {
                PyroGame.currentLevel = Level.SWIM;
                game.setScreen(new PlayScreen(game));
                dispose();
            }
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                swimLevel.setStyle(Utils.skin.get("hover", TextButtonStyle.class));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                swimLevel.setStyle(Utils.skin.get("default", TextButtonStyle.class));
            }
        });

    }

    private void mainMenu() {
        table.clear();
        table.add(playButton).row();
        table.add(exitButton).row();
    }

    private void selectLevel() {
        table.clear();
        table.add(mensLevel).row();
        table.add(bounceLevel).row();
        table.add(mens2Level).row();
        table.add(swimLevel).row();
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
