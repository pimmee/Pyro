package com.pimme.game.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pimme.game.PyroGame;


/**
 * Created by smurf on 2017-11-09.
 */
public class Hud {

    private Stage stage;
    private Viewport viewPort;

    private float hp;
    private Label hpLabel;
    private Label levelLabel;

    public Hud(SpriteBatch batch) {
        hp = 100;

        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, batch);

        //table to organize our hud's labels
        Table table = new Table();
        //top align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);
        batch.begin();
    }
}
