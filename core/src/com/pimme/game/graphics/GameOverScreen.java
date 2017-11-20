package com.pimme.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pimme.game.PyroGame;
import com.badlogic.gdx.graphics.Color;



/**
 * Created by smurf on 2017-11-16.
 */
public class GameOverScreen implements Screen {
    private final PyroGame game;
    private Viewport viewPort;
    private Stage stage;

    public GameOverScreen(final PyroGame game) {
        this.game = game;
        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, game.batch);


        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.CHARTREUSE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);


//        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
//
//        TextButton playAgainButton = new TextButton("Play again", style);
//        playAgainButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent e, float x, float y) {
//                game.setScreen(new PlayScreen(game));
//                dispose();
//            }
//        });
        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("PLAY AGAIN", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);
//        table.add(playAgainButton).expandX().padTop(15f);
//        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen(game));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
