package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pimme.game.PyroGame;
import com.pimme.game.PyroGame.Level;
import com.pimme.game.entities.Player.State;
import com.pimme.game.screens.PlayScreen;


/**
 * Created by smurf on 2017-11-09.
 */
public class Hud {

    private PlayScreen screen;
    public Stage stage;
    private Viewport viewPort;

    private static final int HP_WIDTH = 100;
    private static final int HP_HEIGHT = 10;
    public static final float MAX_HEALTH = 100;

    private ShapeRenderer shapeRenderer;
    private float waterLevel = -0.7f;
    private float waterSpeed = 0.005f;
    private boolean tamponActive = false;
    private float hp = 100;
    private float air = 100;
    private int score = 0;
    private float ticks = 0;
    private float time = 0;
    private Label scoreLabel;
    private Label levelLabel;

    public Hud(PlayScreen screen, SpriteBatch batch) {
        this.screen = screen;

        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, batch);

        shapeRenderer = new ShapeRenderer();

        //table to organize our hud's labels
        Table table = new Table();
        //top align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        scoreLabel = new Label("SCORE: " + Integer.toString(score), new LabelStyle(new BitmapFont(), Color.WHITE));
        table.padLeft(PyroGame.V_WIDTH - 100);
        table.add(scoreLabel);
        stage.addActor(table);
    }

    public void update(final float dt) {
        time += dt;
    }

    public void render() {
        renderHealthBar();
        renderScore();
        if(PyroGame.getCurrentLevel() == Level.MENS || PyroGame.getCurrentLevel() == Level.MENS2)
            drawWater();
        if (PyroGame.getCurrentLevel() == Level.SWIM) {
            decreaseAir();
            renderAirBar();
        }
    }

    private void decreaseAir() {
        if (air > 0) air -= 0.1;
    }

    private void renderScore() {
        scoreLabel.setText("SCORE: " + Integer.toString(score));
    }

    private void drawWater() {
        if (tamponActive && waterLevel > 0)
            waterLevel -= waterSpeed;
        else
            waterLevel += waterSpeed;
        //Draws water
        if (screen.getPlayer().getY() <= waterLevel)
            reduceHealth(0.2f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(screen.getGameCam().combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 0.3f);
        shapeRenderer.rect(0, 0, 50, waterLevel);
        shapeRenderer.end();
    }

    private void renderHealthBar() {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(viewPort.getCamera().combined);
        shapeRenderer.setColor(Color.RED);
        roundedRect((PyroGame.V_WIDTH >> 1) - (HP_WIDTH >> 1), 0, HP_WIDTH, HP_HEIGHT, 3);
        if (hp > 3) {
            shapeRenderer.setColor(Color.GREEN);
            roundedRect((PyroGame.V_WIDTH >> 1) - (HP_WIDTH >> 1), 0, hp, HP_HEIGHT, 3);
        }
        shapeRenderer.end();
    }
    private void renderAirBar() {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(viewPort.getCamera().combined);
        shapeRenderer.setColor(Color.SKY);
        roundedRect((PyroGame.V_WIDTH >> 1) - (HP_WIDTH >> 1), HP_HEIGHT, HP_WIDTH, HP_HEIGHT, 3);
        if (air > 3) {
            shapeRenderer.setColor(Color.BLUE);
            roundedRect((PyroGame.V_WIDTH >> 1) - (HP_WIDTH >> 1), HP_HEIGHT, air, HP_HEIGHT, 3);
        }
        shapeRenderer.end();
    }

    public void roundedRect(float x, float y, float width, float height, float radius){
        // Central rectangle
        shapeRenderer.rect(x + radius, y + radius, width - 2*radius, height - 2*radius);

        // Four side rectangles, in clockwise order
        shapeRenderer.rect(x + radius, y, width - 2*radius, radius);
        shapeRenderer.rect(x + width - radius, y + radius, radius, height - 2*radius);
        shapeRenderer.rect(x + radius, y + height - radius, width - 2*radius, radius);
        shapeRenderer.rect(x, y + radius, radius, height - 2*radius);

        // Four arches, clockwise too
        shapeRenderer.arc(x + radius, y + radius, radius, 180.0f, 90.0f);
        shapeRenderer.arc(x + width - radius, y + radius, radius, 270.0f, 90.0f);
        shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0.0f, 90.0f);
        shapeRenderer.arc(x + radius, y + height - radius, radius, 90.0f, 90.0f);
    }

    public void reduceHealth(float amount) {
        if (hp > 0) hp -= amount;
        if (hp <= 0) screen.getPlayer().currentState = State.DEAD;
    }

    public void setTamponActive(boolean value) {
        tamponActive = value;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getTimeScore() {
        return (int) Math.floor(1/Math.sqrt(time) * 100);
    }
    public int getScore() { return score; }
    public float getHealth() { return hp; }
    public void addHealth(float amount) {
        if (hp + amount > MAX_HEALTH) hp = MAX_HEALTH;
        else hp += amount;
    }

    public void dispose() {
        stage.dispose();
    }
}