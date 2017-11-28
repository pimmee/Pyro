package com.pimme.game.graphics;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pimme.game.PyroGame;

public class MenuScreen implements Screen
{
    private PyroGame game;
    private FitViewport viewPort;
    private Stage stage;
    private Table table;
    public MenuScreen(PyroGame game) {
        this.game = game;

        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());




    }
    @Override public void show() {

    }

    @Override public void render(final float delta) {

    }

    @Override public void resize(final int width, final int height) {

    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public void hide() {

    }

    @Override public void dispose() {

    }
}
