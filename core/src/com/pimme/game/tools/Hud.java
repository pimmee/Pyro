package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Player.State;
import com.pimme.game.screens.PlayScreen;
import com.pimme.game.tools.Manager.Level;


/**
 * Created by smurf on 2017-11-09.
 */
public class Hud {

    private PlayScreen screen;
    private Manager manager;
    public Stage stage;
    private Viewport viewPort;

    private static final int HP_WIDTH = 100;
    private static final int HP_HEIGHT = 10;
    private static final float MAX_HEALTH = 100;

    private ShapeRenderer shapeRenderer;
    private static final float WATER_SPEED = 0.005f;
    private static final float WATER_MIN = -0.7f;
    private float waterLevel = WATER_MIN;
    private boolean tamponActive = false;
    private float hp = 100;
    private int score = 0;
    private float time = 0;
    private Label scoreLabel, levelLabel, livesLabel;

    public Hud(PlayScreen screen, SpriteBatch batch) {
        this.screen = screen;
        manager = screen.getGame().getManager();

        viewPort = new FitViewport(PyroGame.V_WIDTH, PyroGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, batch);

        shapeRenderer = new ShapeRenderer();

        Table table = new Table();
        table.top();

        table.setFillParent(true);

        //scoreLabel = new Label("SCORE: " + Integer.toString(score), new LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(scoreLabel);
        if (manager.getCompletedLevels() != null && manager.getCurrentLevel() != Level.LEVEL1) {
            //levelLabel = new Label("LEVEL  " + Integer.toString(manager.getCompletedLevels().size + 1), Utils.skin);
            livesLabel = new Label("LIVES  " + Integer.toString(manager.getLivesLeft()), Utils.skin);
        }
        Table leftTable = new Table();
        Table rightTable = new Table();
        rightTable.add(levelLabel);
        leftTable.add(livesLabel);
        leftTable.left().top();
        rightTable.right().top();
        leftTable.setFillParent(true);
        rightTable.setFillParent(true);
        stage.addActor(table);
        stage.addActor(leftTable);
        stage.addActor(rightTable);
    }

    public void update(final float dt) {
        time += dt;
    }

    public void render() {
        renderHealthBar();
//        renderScore();
        drawWater();
    }


//    private void renderScore() {
//        scoreLabel.setText("SCORE: " + (Integer.toString(getScore())));
//    }

    private void drawWater() {
        if (tamponActive && waterLevel > WATER_MIN)
            waterLevel -= WATER_SPEED;
        else
            waterLevel += WATER_SPEED;
        //Draws water
        if (screen.getPlayer().getY() <= waterLevel)
            reduceHealth(0.2f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(screen.getGameCam().combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 0.3f);
        if (waterLevel < 0)
            shapeRenderer.rect(0, WATER_MIN, 100, Math.abs(WATER_MIN -waterLevel));
        else
            shapeRenderer.rect(0, WATER_MIN, 100, Math.abs(WATER_MIN) + waterLevel);
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
//    private void renderAirBar() {
//        shapeRenderer.begin(ShapeType.Filled);
//        shapeRenderer.setProjectionMatrix(viewPort.getCamera().combined);
//        shapeRenderer.setColor(Color.SKY);
//        roundedRect((PyroGame.V_WIDTH >> 1) - (HP_WIDTH >> 1), HP_HEIGHT, HP_WIDTH, HP_HEIGHT, 3);
//        if (air > 3) {
//            shapeRenderer.setColor(Color.BLUE);
//            roundedRect((PyroGame.V_WIDTH >> 1) - (HP_WIDTH >> 1), HP_HEIGHT, air, HP_HEIGHT, 3);
//        }
//        shapeRenderer.end();
//    }

    private void roundedRect(float x, float y, float width, float height, float radius){
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


    public int getScore() {
        int calcScore = (int) Math.floor(1/Math.sqrt(time) * score / 10 * hp);
        if (calcScore < 0) calcScore = 0;
        return calcScore; }
    public float getHealth() { return hp; }
    public void addHealth(float amount) {
        if (hp + amount > MAX_HEALTH) hp = MAX_HEALTH;
        else hp += amount;
    }

    public void dispose() {
        stage.dispose();
    }
}
