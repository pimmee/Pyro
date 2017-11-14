package com.pimme.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public class Player extends Sprite
{
    private static final float SPEED = 7;
    private static final float JUMP_VEL = 4.4f;

    public enum State { FALLING, JUMPING, STANDING, RUNNING}
    private State currentState;
    private State previousState;
    public World world;
    public Body b2body;
    private Animation pyretRun;
    private Animation pyretJump;
    private Animation pyretFalling;
    private Animation pyretStanding;
    private float stateTimer;
    private boolean runningRight;
    private Array<TextureRegion> frames;
    private int health = 100;

    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("Dog_Faint"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        initAnimations();

        definePyret();
        setBounds(0, 0, 40 / PyroGame.PPM, 30 / PyroGame.PPM);
    }

    public void update(final float dt) {
        handleInput(dt);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if(outOfBounds()) definePyret();
    }

    public TextureRegion getFrame(final float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) pyretJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) pyretRun.getKeyFrame(stateTimer, true); //true for loop
                break;
            case FALLING:
                region = (TextureRegion) pyretFalling.getKeyFrame(stateTimer, true);
                break;
            default:
                region = (TextureRegion) pyretStanding.getKeyFrame(stateTimer, true);
                break;
        }
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) { //If running to the left but faceing right
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0; // Does currentstate = previousState? If so, add dt to stateTimer. Else reset timer
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(b2body.getLinearVelocity().y > 0) //|| (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void handleInput(final float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getState() != State.JUMPING && getState() != State.FALLING)
            jump();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && b2body.getLinearVelocity().x <= 2)
            moveRight(dt);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && b2body.getLinearVelocity().x >= -2)
            moveLeft(dt);
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
        frames.clear();
        //JUMPING
        frames.add(new TextureRegion(getTexture(), 84, 1597, 253, 167));
        frames.add(new TextureRegion(getTexture(), 423, 1536, 249, 203));
        frames.add(new TextureRegion(getTexture(), 751, 1430, 253, 209));
        frames.add(new TextureRegion(getTexture(), 1086, 1384, 262, 193));
        frames.add(new TextureRegion(getTexture(), 1426, 1358, 254, 171));

        pyretJump = new Animation(0.1f, frames);
        frames.clear();
        //FALLING
//        frames.add(new TextureRegion(getTexture(), 1781, 1375, 254, 172));
        frames.add(new TextureRegion(getTexture(), 2126, 1482, 257, 196));
//        frames.add(new TextureRegion(getTexture(), 2468, 1594, 253, 170));

        pyretFalling = new Animation(0.2f, frames);
        frames.clear();
        //STANDING
        frames.add(new TextureRegion(getTexture(), 58, 70, 257, 163));
        frames.add(new TextureRegion(getTexture(), 367, 71, 257, 162));
        frames.add(new TextureRegion(getTexture(), 677, 72, 257, 161));
        frames.add(new TextureRegion(getTexture(), 991, 72, 257, 161));
        pyretStanding = new Animation(0.7f, frames);
    }

    private void definePyret() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PyroGame.PPM, 100 / PyroGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(14 / PyroGame.PPM, 12 / PyroGame.PPM);

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData("pyret");
    }

    public void reduceHealth(int amount) {
        health -= amount;
    }

    public boolean outOfBounds() {
        return b2body.getPosition().x < 0 || b2body.getPosition().x > 1000 || b2body.getPosition().y < 0 || b2body.getPosition().y > 1000;
    }

    public void jump() {
        b2body.applyLinearImpulse(new Vector2(0, JUMP_VEL), b2body.getWorldCenter(), true);
    }

    public void moveLeft(float dt) {
        b2body.applyLinearImpulse(new Vector2(-SPEED * dt, 0), b2body.getWorldCenter(), true);
    }

    public void moveRight(float dt) {
        b2body.applyLinearImpulse(new Vector2(SPEED * dt, 0), b2body.getWorldCenter(), true);
    }

    public void bounce() {
        b2body.applyLinearImpulse(new Vector2(0, 10), b2body.getWorldCenter(), true);

    }
}
