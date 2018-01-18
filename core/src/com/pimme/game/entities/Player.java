package com.pimme.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.PyroGame.Level;
import com.pimme.game.entities.objects.FlyPowerup;
import com.pimme.game.graphics.GameOverScreen;
import com.pimme.game.graphics.PlayScreen;

public class Player extends Sprite
{
    static final int SPRITE_SIZE = 20;
    private static final float SPEED = 5;
    private static final float JUMP_VEL = 4.4f;
    private static final float FLY_VEL = 5.0f;
    private static final float MAX_SPEED = 2;
    private static final float MAX_FLY_SPEED = 3;
    private static final float FLY_SPEED = 1.5f;

    public enum State { FALLING, JUMPING, STANDING, RUNNING, FLYING, SWIMMING, DEAD}
    public State currentState;
    private State previousState;

    public World world;
    private PlayScreen screen;
    public Body body;

    private Animation<TextureRegion> pyretRun;
    private Animation<TextureRegion> pyretJump;
    private Animation<TextureRegion> pyretFalling;
    private Animation<TextureRegion> pyretStanding;
    private Animation<TextureRegion> pyretFlying;

    private float stateTimer;
    private boolean runningRight;
    private Array<TextureRegion> frames;
    private float spriteScale = 1;
    private boolean touchingSpike = false;

    public Player(PlayScreen screen) {
        super(new Texture(Gdx.files.internal("puppy_pack.png")));
        this.world = screen.getWorld();
        this.screen = screen;

        if (PyroGame.getCurrentLevel() == Level.FLY) {
            currentState = State.FLYING;
            setSize(getWidth()*1.9f, getHeight() * 1.9f);
        }
        else if (PyroGame.getCurrentLevel() == Level.SWIM) currentState = State.SWIMMING;
        else currentState = State.STANDING;

        runningRight = true;
        initAnimations();
        definePlayer();
    }

    public void update(final float dt) {
        if (currentState == State.DEAD)
            screen.getGame().setScreen(new GameOverScreen(screen.getGame(), screen.getHud().getScore()));
        handleInput(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if(outOfBounds())
            currentState = State.DEAD;
        if(touchingSpike)
            screen.getHud().reduceHealth(20 * dt);
    }

    public void setTouchingSpike(boolean value) {
        touchingSpike = value;
    }

    public TextureRegion getFrame(final float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = pyretJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = pyretRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = pyretFalling.getKeyFrame(stateTimer, true);
                break;
            case FLYING:
                region = pyretFlying.getKeyFrame(stateTimer, true);
                break;
            case SWIMMING:
                region = pyretJump.getKeyFrame(stateTimer, true);
                break;
            default:
                region = pyretStanding.getKeyFrame(stateTimer, true);
                break;
        }
        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) { //If running to the left but faceing right
            region.flip(true, false);
            runningRight = false;
        }
        else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0; // Does currentstate = previousState? If so, add dt to stateTimer. Else reset timer
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (currentState.equals(State.FLYING))
            return State.FLYING;
        else if(currentState.equals(State.SWIMMING))
            return State.SWIMMING;
        else{ if(body.getLinearVelocity().y > 0)
            return State.JUMPING;
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;}
    }

    public void handleInput(final float dt) {
        if (Gdx.input.isKeyPressed(Keys.UP) && body.getLinearVelocity().y <= MAX_FLY_SPEED)
            jump(dt);
        if(Gdx.input.isKeyPressed(Keys.RIGHT) && body.getLinearVelocity().x <= MAX_SPEED)
            moveRight(dt);
        if(Gdx.input.isKeyPressed(Keys.LEFT) && body.getLinearVelocity().x >= -MAX_SPEED)
            moveLeft(dt);
        if(Gdx.input.isKeyPressed(Keys.DOWN) && body.getLinearVelocity().y >= -MAX_FLY_SPEED)
            moveDown(dt);
    }

    private void initAnimations() {
        frames = new Array<TextureRegion>();
        //RUNNING
        frames.add(new TextureRegion(getTexture(), 62,  372, 257, 178));
        frames.add(new TextureRegion(getTexture(), 358, 377, 254, 167));
        frames.add(new TextureRegion(getTexture(), 666,  373, 260, 166));
        frames.add(new TextureRegion(getTexture(), 952,  361, 259, 177));
        frames.add(new TextureRegion(getTexture(), 1278,  371, 266, 158));
        frames.add(new TextureRegion(getTexture(), 1589,  375, 254, 171));
        pyretRun = new Animation<TextureRegion>(0.09f, frames);
        //JUMPING
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 84, 1597, 253, 167));
        frames.add(new TextureRegion(getTexture(), 423, 1536, 249, 203));
        frames.add(new TextureRegion(getTexture(), 751, 1430, 253, 209));
        frames.add(new TextureRegion(getTexture(), 1086, 1384, 262, 193));
        frames.add(new TextureRegion(getTexture(), 1426, 1358, 254, 171));
        pyretJump = new Animation<TextureRegion>(0.15f, frames);

        //FALLING
        frames.clear();
//        frames.add(new TextureRegion(getTexture(), 1781, 1375, 254, 172));
        frames.add(new TextureRegion(getTexture(), 2126, 1482, 257, 196));
//        frames.add(new TextureRegion(getTexture(), 2468, 1594, 253, 170));
        pyretFalling = new Animation<TextureRegion>(0.2f, frames);
        //STANDING
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 58, 70, 257, 163));
        frames.add(new TextureRegion(getTexture(), 367, 71, 257, 162));
        frames.add(new TextureRegion(getTexture(), 677, 72, 257, 161));
        frames.add(new TextureRegion(getTexture(), 991, 72, 257, 161));
        pyretStanding = new Animation<TextureRegion>(0.7f, frames);

        //FLYING
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 78, 688, 481, 237));
        frames.add(new TextureRegion(getTexture(), 627, 680, 501, 253));
        frames.add(new TextureRegion(getTexture(), 1209, 681, 516, 251));
        pyretFlying = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();
    }

    private void initLevelSpecifics() {
        if (PyroGame.getCurrentLevel() == Level.FLY) {
            currentState = State.FLYING;
            body.setLinearVelocity(new Vector2(FLY_SPEED, 0));
        }
    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(screen.getSpawnPos().x / PyroGame.PPM, screen.getSpawnPos().y / PyroGame.PPM);
        bdef.type = BodyType.DynamicBody;
        body = world.createBody(bdef);

        initLevelSpecifics();

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(14 / PyroGame.PPM, 12 / PyroGame.PPM);
        fdef.filter.categoryBits = PyroGame.PLAYER_BIT;
        fdef.filter.maskBits = PyroGame.GROUND_BIT | // What can player collide with?
                               PyroGame.FLY_BIT |
                               PyroGame.COIN_BIT |
                               PyroGame.HP_BIT |
                               PyroGame.SPIKE_BIT |
                               PyroGame.TAMPON_BIT |
                               PyroGame.GOAL_BIT |
                               PyroGame.ENEMY_BIT |
                               PyroGame.BOUNCE_BIT;

        fdef.shape = shape;

        setBounds(0, 0, 40 / PyroGame.PPM, 30 / PyroGame.PPM);

        body.createFixture(fdef).setUserData(this);
    }

    public boolean outOfBounds() {
        return body.getPosition().x < 0 || body.getPosition().x > 1000 || body.getPosition().y < 0 || body.getPosition().y > 1000;
    }

    public void moveDown(float dt) {
        if (currentState == State.FLYING || currentState == State.SWIMMING)
            body.applyLinearImpulse(new Vector2(0, -FLY_VEL * dt), body.getWorldCenter(), true);
    }

    public void jump(float dt) {
        if  (currentState == State.STANDING || currentState == State.RUNNING)
            body.applyLinearImpulse(new Vector2(0, JUMP_VEL), body.getWorldCenter(), true);
        else if (currentState == State.FLYING || currentState == State.SWIMMING) {
            body.applyLinearImpulse(new Vector2(0, FLY_VEL * dt), body.getWorldCenter(), true);
        }
    }

    public void moveLeft(float dt) {
        if (currentState != State.FLYING)
            body.applyLinearImpulse(new Vector2(-SPEED * dt, 0), body.getWorldCenter(), true);
    }

    public void moveRight(float dt) {
        if (currentState != State.FLYING)
            body.applyLinearImpulse(new Vector2(SPEED * dt, 0), body.getWorldCenter(), true);
    }

    public void bounce() {
        if(PyroGame.getCurrentLevel() == Level.BOUNCE)
            body.setLinearVelocity(body.getLinearVelocity().x, 5);
        else body.setLinearVelocity(body.getLinearVelocity().x, 6.5f);
    }
}
