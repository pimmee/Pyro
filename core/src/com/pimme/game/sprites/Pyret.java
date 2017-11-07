package com.pimme.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

public class Pyret extends Sprite
{
    public enum State { FALLING, JUMPING, STANDING, RUNNING}
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private Animation pyretRun;
    private Animation pyretJump;
    private Animation pyretFalling;
    private Animation pyretStanding;
    private float stateTimer;
    private boolean runningRight;
    private Array<TextureRegion> frames;

    public Pyret(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("Dog_Idle"));
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
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

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
                region = (TextureRegion) pyretFalling.getKeyFrame(stateTimer);
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
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0) && previousState == State.JUMPING)
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    private void initAnimations() {
        frames = new Array<TextureRegion>();
        //RUNNING
        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(getTexture(), 40 + i * 300, 350, 300, 200));
        pyretRun = new Animation(0.08f, frames);
        frames.clear();
        //JUMPING
        frames.add(new TextureRegion(getTexture(), 80, 1595, 260, 170));
        frames.add(new TextureRegion(getTexture(), 420, 1530, 260, 210));
        frames.add(new TextureRegion(getTexture(), 750, 1425, 260, 215));
        frames.add(new TextureRegion(getTexture(), 1080, 1380, 270, 300));
        frames.add(new TextureRegion(getTexture(), 1420, 1355, 260, 175));
        //The fall
        frames.add(new TextureRegion(getTexture(), 1780, 1370, 260, 180));
        frames.add(new TextureRegion(getTexture(), 2120, 1480, 265, 200));
        frames.add(new TextureRegion(getTexture(), 2465, 1590, 260, 175));
        pyretJump = new Animation(0.08f, frames);
        frames.clear();
        //FALLING
        frames.add(new TextureRegion(getTexture(), 1780, 1370, 260, 180));
        frames.add(new TextureRegion(getTexture(), 2120, 1480, 265, 200));
        frames.add(new TextureRegion(getTexture(), 2465, 1590, 260, 175));
        pyretFalling = new Animation(0.08f, frames);
        frames.clear();
        //STANDING
        frames.add(new TextureRegion(getTexture(), 56, 68, 260, 270));
        frames.add(new TextureRegion(getTexture(), 366, 69, 260, 270));
        frames.add(new TextureRegion(getTexture(), 675, 70, 260, 270));
        frames.add(new TextureRegion(getTexture(), 989, 70, 260, 270));
        pyretStanding = new Animation(0.3f, frames);





    }

    private void definePyret() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PyroGame.PPM, 32 / PyroGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);


        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(8 / PyroGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
