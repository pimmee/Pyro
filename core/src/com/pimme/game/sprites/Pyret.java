package com.pimme.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

public class Pyret extends Sprite
{
    public World world;
    public Body b2body;
    private TextureRegion pyretStand;

    public Pyret(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("Dog_Idle"));
        this.world = world;
        definePyret();
	pyretStand = new TextureRegion(getTexture(), 0, 0, 330, 300);
	setBounds(0, 0, 30 / PyroGame.PPM, 30 / PyroGame.PPM);
	setRegion(pyretStand);
    }

    public void update(final float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void definePyret() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PyroGame.PPM, 32 / PyroGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
	CircleShape shape = new CircleShape();
	shape.setRadius(6 / PyroGame.PPM);

	fdef.shape = shape;
	b2body.createFixture(fdef);
    }
}
