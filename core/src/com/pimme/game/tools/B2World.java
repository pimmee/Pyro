package com.pimme.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Brick;
import com.pimme.game.entities.Platform;
import com.pimme.game.entities.objects.*;
import com.pimme.game.entities.objects.Tampon;
import com.pimme.game.graphics.PlayScreen;

public class B2World implements ContactListener
{
    private World world;
    private PlayScreen screen;
    private Array<Platform> platforms;

    public B2World(PlayScreen screen, TiledMap map) {
	this.screen = screen;
	this.world = screen.getWorld();
	platforms = new Array<Platform>();

	world.setContactListener(this);
	int layers = map.getLayers().getCount();
	for (int layer = 2; layer < layers; layer++) {
	    switch (layer) {
		case 2:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
		        new Goal(screen, object);
		    break;
		case 3:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
			new Brick(screen, object);
		    break;
		case 4:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
			new Coin(screen, object);
		    break;
		case 5:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
			new HealthPack(screen, object);
		    break;
		case 6:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
			new Bounce(screen, object);
		    break;
		case 7:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
			new Spike(screen, object);
		    break;
		case 8:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
			new Tampon(screen, object);
		    break;
		case 9:
		    for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
			platforms.add(new Platform(screen, object));
		    break;
	    }
	}
    }

    @Override
    public void beginContact(Contact contact) {
	final Fixture fixA = contact.getFixtureA();
	final Fixture fixB = contact.getFixtureB();

	int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
	//final Body toRemove;
	switch(cDef){
	    case PyroGame.PYRET_BIT | PyroGame.COIN_BIT:
	    case PyroGame.PYRET_BIT | PyroGame.HP_BIT:
	    case PyroGame.PYRET_BIT | PyroGame.BOUNCE_BIT:
	    case PyroGame.PYRET_BIT | PyroGame.TAMPON_BIT:
	    case PyroGame.PYRET_BIT | PyroGame.FLY_BIT:
	    case PyroGame.PYRET_BIT | PyroGame.SPIKE_BIT:
		if(fixA.getFilterData().categoryBits == PyroGame.PYRET_BIT)
		    ((InteractiveObject) fixB.getUserData()).onCollision();
		else
		    ((InteractiveObject) fixA.getUserData()).onCollision();
		break;

	    case PyroGame.PYRET_BIT | PyroGame.GOAL_BIT:
	        screen.reachedGoal();
	        break;
		/*Gdx.app.postRunnable(new Runnable() {

		    @Override
		    public void run() {
			world.destroyBody(toRemove);
		    }
		});*/
	}
    }

    @Override
    public void endContact(Contact contact) {
	final Fixture fixA = contact.getFixtureA();
	final Fixture fixB = contact.getFixtureB();

	int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
	switch (cDef) {
	    case PyroGame.PYRET_BIT | PyroGame.SPIKE_BIT:
		screen.getPlayer().setTouchingSpike(false);
		break;
	}
    }

    public Array<Platform> getPlatforms() {
        return platforms;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
