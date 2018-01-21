package com.pimme.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pimme.game.PyroGame;
import com.pimme.game.PyroGame.Level;
import com.pimme.game.screens.PlayScreen;
import com.pimme.game.tools.Graphics;
import com.sun.javafx.geom.RoundRectangle2D;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Sprite {
    static final int SPRITE_SIZE = 20;
    private static final float SPEED = 5;
    private static final float JUMP_VEL = 4.4f;
    private static final float FLY_VEL = 5.0f;
    private static final float MAX_SPEED = 2;
    private static final float MAX_FLY_SPEED = 3;
    private static final float FLY_SPEED = 1.5f;
    private static final float SWIM_SPEED = 3.0f;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, FLYING, SWIMMING, HURT, DEAD}

    public State currentState;
    private State previousState;

    public World world;
    private PlayScreen screen;
    public Body body;

    private float stateTimer;
    private boolean damaged = false;
    private boolean runningRight;
    private boolean touchingSpike = false;

    public Player(PlayScreen screen) {
        this.world = screen.getWorld();
        this.screen = screen;

        definePlayer();
        initLevelSpecifics();
    }

    public void update(final float dt) {
        if (currentState == State.DEAD) die();
        handleInput(dt);
        if(PyroGame.getCurrentLevel().equals(Level.FLY)) setPosition(body.getPosition().x - getWidth() / 1.5f, body.getPosition().y - getHeight() / 2);
        else setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if (outOfBounds()) currentState = State.DEAD;
        if (touchingSpike) screen.getHud().reduceHealth(40 * dt);
    }

    public void setTouchingSpike(boolean value) {
        touchingSpike = value;
    }

    private void die() {
        for (int i = 0; i < body.getFixtureList().size; i++) {
            Filter filter = new Filter();
            filter.categoryBits = PyroGame.NOTHING_BIT;
            body.getFixtureList().get(i).setFilterData(filter);
        }
        if (body.getPosition().y <= 0) screen.setGameOver();
    }

    private TextureRegion getFrame(final float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = Graphics.pyretJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = Graphics.pyretRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = Graphics.pyretFalling.getKeyFrame(stateTimer, true);
                break;
            case FLYING:
                region = Graphics.pyretFlying.getKeyFrame(stateTimer, true);
                break;
            case SWIMMING:
                region = Graphics.pyretSwim.getKeyFrame(stateTimer, true);
                break;
            case HURT:
                region = Graphics.pyretHurt.getKeyFrame(stateTimer);
                break;
            default:
                region = Graphics.pyretStanding.getKeyFrame(stateTimer, true);
                break;
        }
        if ((body.getLinearVelocity().x < 0) && !region.isFlipX()) //If running to the left but faceing right
            region.flip(true, false);
        else if ((body.getLinearVelocity().x > 0) && region.isFlipX())
            region.flip(true, false);
        stateTimer = currentState == previousState ? stateTimer + dt : 0; // Does currentstate = previousState? If so, add dt to stateTimer. Else reset timer
        previousState = currentState;
        return region;
    }

    private State getState() {
        if (currentState.equals(State.FLYING))
            return State.FLYING;
        else if (currentState.equals(State.SWIMMING))
            return State.SWIMMING;
        else {
            if (body.getLinearVelocity().y > 0)
                return State.JUMPING;
            else if (body.getLinearVelocity().y < 0)
                return State.FALLING;
            else if (body.getLinearVelocity().x != 0)
                return State.RUNNING;
            else
                return State.STANDING;
        }
    }

    private void handleInput(final float dt) {
        if (Gdx.input.isKeyJustPressed(Keys.UP))// && body.getLinearVelocity().y <= MAX_FLY_SPEED)
            jump(dt);
        if (Gdx.input.isKeyPressed(Keys.RIGHT) && body.getLinearVelocity().x <= MAX_SPEED)
            moveRight(dt);
        if (Gdx.input.isKeyPressed(Keys.LEFT) && body.getLinearVelocity().x >= -MAX_SPEED)
            moveLeft(dt);
        if (Gdx.input.isKeyPressed(Keys.DOWN) && body.getLinearVelocity().y >= -MAX_FLY_SPEED)
            moveDown(dt);
    }

    private void initLevelSpecifics() {
        if (PyroGame.getCurrentLevel() == Level.FLY) {
            currentState = State.FLYING;
            body.setLinearVelocity(new Vector2(FLY_SPEED, 0));
            setSize(getWidth() * 1.7f, getHeight() * 1.5f);
        } else if (PyroGame.getCurrentLevel() == Level.SWIM) currentState = State.SWIMMING;
        else currentState = State.STANDING;
    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(screen.getSpawnPos().x / PyroGame.PPM, screen.getSpawnPos().y / PyroGame.PPM);
        bdef.type = BodyType.DynamicBody;
        body = world.createBody(bdef);


        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
//        PolygonShape dynamicBox = new PolygonShape();
//        Vector2[] boxVertices = new Vector2[8];
//        boxVertices[0] = new Vector2(-0.15f, -0.08f);
//        boxVertices[1] = new Vector2(-0.10f, -0.12f);
//        boxVertices[2] = new Vector2(0.10f, -0.12f);
//        boxVertices[3] = new Vector2(0.15f, -0.08f);
//        boxVertices[4] = new Vector2(0.15f, 0.08f);
//        boxVertices[5] = new Vector2(0.10f, 0.12f);
//        boxVertices[6] = new Vector2(-0.10f, 0.12f);
//        boxVertices[7] = new Vector2(-0.15f, 0.08f);
//        dynamicBox.set(boxVertices);
        shape.setAsBox(14 / PyroGame.PPM, 12 / PyroGame.PPM);
        fdef.filter.categoryBits = PyroGame.PLAYER_BIT;
        fdef.filter.maskBits = PyroGame.BRICK_BIT | // What can player collide with?
                PyroGame.COIN_BIT |
                PyroGame.HP_BIT |
                PyroGame.SPIKE_BIT |
                PyroGame.TAMPON_BIT |
                PyroGame.GOAL_BIT |
                PyroGame.ENEMY_BIT |
                PyroGame.ENEMY_HEAD_BIT |
                PyroGame.BOUNCE_BIT;

        fdef.shape = shape;

        setBounds(0, 0, 40 / PyroGame.PPM, 30 / PyroGame.PPM);

        body.createFixture(fdef).setUserData(this);
    }

    public void hitByEnemy() {
        if (!damaged) screen.getHud().reduceHealth(20f);
        damaged = true;
        flickerSprite();
    }

    public boolean isTouchingSpike() {
        return touchingSpike;
    }


    private boolean outOfBounds() {
        return body.getPosition().x < 0 || body.getPosition().x > 1000 || body.getPosition().y < 0 || body.getPosition().y > 1000;
    }

    private void moveDown(float dt) {
        if (currentState == State.FLYING)
            body.applyLinearImpulse(new Vector2(0, -FLY_VEL * dt), body.getWorldCenter(), true);
        else if (currentState == State.SWIMMING)
            body.applyLinearImpulse(new Vector2(0, -SWIM_SPEED * dt), body.getWorldCenter(), true);
    }

    private void jump(float dt) {
        if (currentState == State.STANDING || currentState == State.RUNNING)
            body.applyLinearImpulse(new Vector2(0, JUMP_VEL), body.getWorldCenter(), true);
        else if (currentState == State.FLYING)
            body.applyLinearImpulse(new Vector2(0, FLY_VEL * dt), body.getWorldCenter(), true);
        else if (currentState == State.SWIMMING)
            body.applyLinearImpulse(new Vector2(0, SWIM_SPEED * dt), body.getWorldCenter(), true);
    }

    private void moveLeft(float dt) {
        if (currentState != State.FLYING && currentState != State.SWIMMING)
            body.applyLinearImpulse(new Vector2(-SPEED * dt, 0), body.getWorldCenter(), true);
        else if (currentState == State.SWIMMING)
            body.applyLinearImpulse(new Vector2(-SWIM_SPEED * dt, 0), body.getWorldCenter(), true);
    }

    private void moveRight(float dt) {
        if (currentState != State.FLYING && currentState != State.SWIMMING)
            body.applyLinearImpulse(new Vector2(SPEED * dt, 0), body.getWorldCenter(), true);
        else if (currentState == State.SWIMMING)
            body.applyLinearImpulse(new Vector2(SWIM_SPEED * dt, 0), body.getWorldCenter(), true);
    }

    public void bounce() {
        if (PyroGame.getCurrentLevel() == Level.BOUNCE)
            body.setLinearVelocity(body.getLinearVelocity().x, 5);
        else body.setLinearVelocity(body.getLinearVelocity().x, 6.5f);
    }

    private void flickerSprite() {
        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    int count = 0;
                    @Override
                    public void run() {
                        count++;
                        if (Math.round(getColor().a) == 1) setAlpha(0.5f);
                        else setAlpha(1);
                        if (count == 30) { // delay * 15 = 1.5sec
                            setAlpha(1);
                            damaged = false;
                            this.cancel();
                        }
                    }
                }, 150, 150
        );
    }
}