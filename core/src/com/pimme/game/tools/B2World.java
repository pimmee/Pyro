package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.*;
import com.pimme.game.graphics.PlayScreen;

public class B2World implements ContactListener
{
	private World world;
	public B2World(PlayScreen screen, TiledMap map) {
		this.world = screen.getWorld();

		world.setContactListener(this);

		for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
			new Brick(screen, object);
		}

		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			new Coin(screen, object);
		}

		for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			new HealthPack(screen, object);
		}

		for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			new MagnetPowerup(screen, object);
		}
	}

	@Override
	public void beginContact(Contact contact) {
		final Fixture fixA = contact.getFixtureA();
		final Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		final Body toRemove;
		switch(cDef){

			case PyroGame.PYRET_BIT | PyroGame.COIN_BIT:
			case PyroGame.PYRET_BIT | PyroGame.HEART_BIT:
			case PyroGame.PYRET_BIT | PyroGame.MAGNET_BIT:
			case PyroGame.PYRET_BIT | PyroGame.FLY_BIT:
				if(fixA.getFilterData().categoryBits == PyroGame.PYRET_BIT) {
					((InteractiveObject) fixB.getUserData()).onCollision();
					toRemove = fixB.getBody();
				}
				else {
					((InteractiveObject) fixA.getUserData()).onCollision();
					toRemove = fixA.getBody();
				}
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						world.destroyBody(toRemove);
					}
				});
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
