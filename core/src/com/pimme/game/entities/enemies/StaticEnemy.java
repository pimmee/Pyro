package com.pimme.game.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public class StaticEnemy extends Enemy
{
    public StaticEnemy(final PlayScreen screen, final MapObject object, final Texture texture)
    {
	super(screen, object);
    }

    @Override public void defineEnemy() {
	BodyDef bdef = new BodyDef();
	FixtureDef fdef = new FixtureDef();
	PolygonShape shape = new PolygonShape();

	bdef.type = BodyDef.BodyType.StaticBody;
	bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM,
			  (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);

	body = world.createBody(bdef);

	shape.setAsBox(bounds.getWidth() / 2 / PyroGame.PPM, bounds.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
	fdef.shape = shape;
	fdef.isSensor = true;
	fixture = body.createFixture(fdef);
	fixture.setUserData(this);
    }
	@Override public void update(final float dt) {

	}
    }
