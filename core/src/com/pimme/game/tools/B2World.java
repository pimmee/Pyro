package com.pimme.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Brick;
import com.pimme.game.entities.enemies.Enemy;
import com.pimme.game.entities.enemies.MovingEnemy;
import com.pimme.game.entities.Platform;
import com.pimme.game.entities.objects.*;
import com.pimme.game.screens.GameOverScreen;
import com.pimme.game.screens.PlayScreen;

public class B2World implements ContactListener
{
	private World world;
	private TiledMap map;
	private PlayScreen screen;
	private Array<Platform> platforms;
	private Array<Enemy> enemies;

	public B2World(PlayScreen screen) {
		this.screen = screen;
		this.world = screen.getWorld();
		this.map = screen.getMap();
		platforms = new Array<>();
		enemies = new Array<>();

		world.setContactListener(this);
		initMapObjects();

	}

	private void initMapObjects() {
		int layers = map.getLayers().getCount();
		for (int layer = 2; layer < layers; layer++) {
			switch (layer) {
				case 2:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rec = ((RectangleMapObject) object).getRectangle();
						screen.setSpawnPosition(new Vector2(rec.getX() + rec.getWidth() / 2 / PyroGame.PPM, rec.getY() + rec.getHeight() / 2 / PyroGame.PPM));
					}
					break;

				case 3:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						new Goal(screen, object);
					break;
				case 4:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						new Brick(screen, object);
					break;
				case 5:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						new Coin(screen, object);
					break;
				case 6:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						enemies.add(new MovingEnemy(screen, object));
					break;
				case 7:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						new HealthPack(screen, object);
					break;
				case 8:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						new Bounce(screen, object);
					break;
				case 9:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						new Spike(screen, object);
					break;
				case 10:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						new Tampon(screen, object);
					break;
				case 11:
					for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class))
						platforms.add(new Platform(screen, object));
					break;
			}
		}
	}

	@Override
	public void beginContact(Contact contact) {
		if (PyroGame.getCurrentLevel() == PyroGame.Level.FLY) screen.getGame().setScreen(new GameOverScreen(screen.getGame(), screen.getHud().getScore()));
		final Fixture fixA = contact.getFixtureA();
		final Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		//final Body toRemove;
		switch(cDef){
			case PyroGame.PLAYER_BIT | PyroGame.COIN_BIT:
			case PyroGame.PLAYER_BIT | PyroGame.HP_BIT:
			case PyroGame.PLAYER_BIT | PyroGame.BOUNCE_BIT:
			case PyroGame.PLAYER_BIT | PyroGame.TAMPON_BIT:
			case PyroGame.PLAYER_BIT | PyroGame.SPIKE_BIT:
			case PyroGame.PLAYER_BIT | PyroGame.GOAL_BIT:
				if(fixA.getFilterData().categoryBits == PyroGame.PLAYER_BIT)
					((InteractiveObject) fixB.getUserData()).onCollision();
				else ((InteractiveObject) fixA.getUserData()).onCollision();
				break;
			case PyroGame.PLAYER_BIT | PyroGame.ENEMY_BIT:
				screen.getPlayer().hitByEnemy();
				if(fixA.getFilterData().categoryBits == PyroGame.PLAYER_BIT)
					((Enemy) fixB.getUserData()).reverseVelocity();
				else ((Enemy) fixA.getUserData()).reverseVelocity();
				break;
			case PyroGame.PLAYER_BIT | PyroGame.ENEMY_HEAD_BIT:
				if(fixA.getFilterData().categoryBits == PyroGame.PLAYER_BIT)
					((Enemy) fixB.getUserData()).hitOnHead();
				else ((Enemy) fixA.getUserData()).hitOnHead();
				break;
			case PyroGame.ENEMY_BIT | PyroGame.BRICK_BIT:
				if(fixA.getFilterData().categoryBits == PyroGame.BRICK_BIT)
					((Enemy) fixB.getUserData()).reverseVelocity();
				else ((Enemy) fixA.getUserData()).reverseVelocity();
				break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		final Fixture fixA = contact.getFixtureA();
		final Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		switch (cDef) {
			case PyroGame.PLAYER_BIT | PyroGame.SPIKE_BIT:
				screen.getPlayer().setTouchingSpike(false);
				break;
		}
	}

	public Array<Platform> getPlatforms() {
		return platforms;
	}
	public Array<Enemy> getEnemies() {
		return enemies;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
