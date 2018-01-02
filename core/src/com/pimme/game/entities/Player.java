package com.pimme.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public class Player extends Sprite
{
    static final int SPRITE_SIZE = 20;
    private static final float SPEED = 7;
    private static final float JUMP_VEL = 4.4f;
    private static final float FLY_VEL = 2f;
    private Vector2 spawnPos = new Vector2();

    public enum State { FALLING, JUMPING, STANDING, RUNNING, FLYING, SWIMMING, DEAD}
    public State currentState;
    private State previousState;

    public World world;
    private PlayScreen screen;
    public Body body;

    private Animation pyretRun;
    private Animation pyretJump;
    private Animation pyretFalling;
    private Animation pyretStanding;
    private Animation pyretFlying;

    private float stateTimer;
    private boolean runningRight;
    private Array<TextureRegion> frames;
    private float spriteScale = 1;
    private boolean touchingSpike = false;

    public Player(World world, PlayScreen screen) {
        super(new Texture(Gdx.files.internal("puppy_pack.png")));
        this.world = world;
        this.screen = screen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        initAnimations();
        setSpawnPosition();

        definePlayer();
        setBounds(0, 0, 40 / PyroGame.PPM, 30 / PyroGame.PPM);
    }

    public void update(final float dt) {
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
                region = (TextureRegion) pyretJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) pyretRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = (TextureRegion) pyretFalling.getKeyFrame(stateTimer, true);
                break;
            case FLYING:
                region = (TextureRegion) pyretFlying.getKeyFrame(stateTimer, true);
                break;
            default:
                region = (TextureRegion) pyretStanding.getKeyFrame(stateTimer, true);
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
        else if(body.getLinearVelocity().y > 0) //|| (body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void handleInput(final float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            jump();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 2)
            moveRight(dt);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x >= -2)
            moveLeft(dt);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
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
        pyretRun = new Animation(0.09f, frames);
        //JUMPING
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 84, 1597, 253, 167));
        frames.add(new TextureRegion(getTexture(), 423, 1536, 249, 203));
        frames.add(new TextureRegion(getTexture(), 751, 1430, 253, 209));
        frames.add(new TextureRegion(getTexture(), 1086, 1384, 262, 193));
        frames.add(new TextureRegion(getTexture(), 1426, 1358, 254, 171));
        pyretJump = new Animation(0.1f, frames);

        //FALLING
        frames.clear();
//        frames.add(new TextureRegion(getTexture(), 1781, 1375, 254, 172));
        frames.add(new TextureRegion(getTexture(), 2126, 1482, 257, 196));
//        frames.add(new TextureRegion(getTexture(), 2468, 1594, 253, 170));
        pyretFalling = new Animation(0.2f, frames);
        //STANDING
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 58, 70, 257, 163));
        frames.add(new TextureRegion(getTexture(), 367, 71, 257, 162));
        frames.add(new TextureRegion(getTexture(), 677, 72, 257, 161));
        frames.add(new TextureRegion(getTexture(), 991, 72, 257, 161));
        pyretStanding = new Animation(0.7f, frames);

        //FLYING
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 78, 688, 481, 237));
        frames.add(new TextureRegion(getTexture(), 627, 680, 501, 253));
        frames.add(new TextureRegion(getTexture(), 1209, 681, 516, 251));
        pyretFlying = new Animation(0.2f, frames);
        frames.clear();
    }

    public void setSpawnPosition() {
        switch (screen.getLevel()) {
            case MENS:
                spawnPos.set(100, 100);
                break;
            case BOUNCE:
                spawnPos.set(600, 300);
                break;
        }
    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnPos.x / PyroGame.PPM, spawnPos.y / PyroGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(14 / PyroGame.PPM, 12 / PyroGame.PPM);
        fdef.filter.categoryBits = PyroGame.PYRET_BIT;
        fdef.filter.maskBits = PyroGame.GROUND_BIT | // What can player collide with?
                               PyroGame.FLY_BIT |
                               PyroGame.COIN_BIT |
                               PyroGame.HP_BIT |
                               PyroGame.SPIKE_BIT |
                               PyroGame.TAMPON_BIT |
                               PyroGame.GOAL_BIT |
                               PyroGame.BOUNCE_BIT;

        fdef.shape = shape;

        body.createFixture(fdef).setUserData(this);
    }

    public boolean outOfBounds() {
        return body.getPosition().x < 0 || body.getPosition().x > 1000 || body.getPosition().y < 0 || body.getPosition().y > 1000;
    }

    public void moveDown(float dt) {
        if (currentState == State.FLYING)
            body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, -FLY_VEL));
    }

    public void jump() {
        if  (currentState == State.STANDING || currentState == State.RUNNING)
            body.applyLinearImpulse(new Vector2(0, JUMP_VEL), body.getWorldCenter(), true);
        else if (currentState == State.FLYING || currentState == State.SWIMMING) {
            body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, FLY_VEL));
        }
    }

    public void moveLeft(float dt) {
        body.applyLinearImpulse(new Vector2(-SPEED * dt, 0), body.getWorldCenter(), true);
    }

    public void moveRight(float dt) {
        body.applyLinearImpulse(new Vector2(SPEED * dt, 0), body.getWorldCenter(), true);
    }

    public void bounce() {
        body.setLinearVelocity(body.getLinearVelocity().x, 6.2f);
    }
}
