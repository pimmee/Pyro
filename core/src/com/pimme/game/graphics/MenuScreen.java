package com.pimme.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pimme.game.PyroGame;
import com.pimme.game.PyroGame.Level;

public class MenuScreen implements Screen
{
    private final PyroGame game;
    private FitViewport viewPort;
    private Stage stage;
    private Table table;
    private Skin skin;

    private TextButton playButton;
    private TextButton exitButton;
    private TextButton backButton;
    private TextButton mensLevel;
    private TextButton bounceLevel;

    public MenuScreen(final PyroGame game) {
        this.game = game;

        //viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());

        stage = new Stage();
        skin = new Skin();
        Gdx.input.setInputProcessor(stage);

        BitmapFont font = new BitmapFont(Gdx.files.internal("joker_font.fnt"));
        skin.add("default", font); // stores the default font under "default"

        TextButtonStyle tbs = new TextButtonStyle();
//        tbs.up = skin.newDrawable("white", Color.DARK_GRAY);
//        tbs.down = skin.newDrawable("white", Color.DARK_GRAY);
//        tbs.checked = skin.newDrawable("white", Color.BLUE);
//        tbs.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        tbs.font = skin.getFont("default");
        skin.add("default", tbs);


        table = new Table();
        table.padTop(10);
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
        stage.draw();
    }

    private void initButtons() {
        playButton = new TextButton("PLAY", skin);
        exitButton = new TextButton("EXIT", skin);
        backButton = new TextButton("BACK", skin);
        mensLevel = new TextButton("MENS CHAOS", skin);
        bounceLevel = new TextButton("BOUNCY", skin);

        playButton.getLabel().setFontScale(3.0f, 3.0f);
        exitButton.getLabel().setFontScale(3.0f, 3.0f);
        backButton.getLabel().setFontScale(3.0f, 3.0f);
        mensLevel.getLabel().setFontScale(3.0f, 3.0f);
        bounceLevel.getLabel().setFontScale(3.0f, 3.0f);


        playButton.addListener(new ChangeListener()
        {
            @Override public void changed(final ChangeEvent event, final Actor actor) {
                selectLevel();
            }
        });
        exitButton.addListener(new ChangeListener()
        {
            @Override public void changed(final ChangeEvent event, final Actor actor) {
                Gdx.app.exit();
            }
        });
        backButton.addListener(new ChangeListener()
        {
            @Override public void changed(final ChangeEvent event, final Actor actor) {
                mainMenu();
            }
        });
        mensLevel.addListener(new ChangeListener()
        {
            @Override public void changed(final ChangeEvent event, final Actor actor) {
                game.setScreen(new PlayScreen(game, Level.MENS));
            }
        });
        bounceLevel.addListener(new ChangeListener()
        {
            @Override public void changed(final ChangeEvent event, final Actor actor) {
                game.setScreen(new PlayScreen(game, Level.BOUNCE));
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
        table.add(backButton).row();
    }

    @Override public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public void hide() {

    }

    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
