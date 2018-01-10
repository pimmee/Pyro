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
import com.pimme.game.PyroGame.Level;
import com.badlogic.gdx.graphics.Color;
import com.pimme.game.tools.Highscore;

//LÄGG TILL TABLES FÖR 1-5 highscores
/**
 * Created by smurf on 2017-11-16.
 */
public class GameOverScreen implements Screen {
    private PyroGame game;
    private Viewport viewPort;
    private Stage stage;
    private int score;

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
        Highscore.load();
        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("CLICK TO PLAY AGAIN", font);
        Label scoreLabel = new Label("SCORE: " + score, font);
        Label highScoreLabel = new Label("HIGHSCORE: " + Highscore.getHighScore(1), font);

        table.add(gameOverLabel).expandX().row();
        table.add(playAgainLabel).expandX().row();
        table.add(highScoreLabel).expandX().padTop(10f);

        stage.addActor(table);
    }

    public void setScore(int amount) {
        score = amount;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen(game, Level.MENS));
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
