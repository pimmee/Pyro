package com.pimme.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public abstract class InteractiveObject
{
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;
	protected Fixture fixture;
	protected PlayScreen screen;

	protected InteractiveObject(PlayScreen screen, World world, TiledMap map, Rectangle bounds) {
		this.screen = screen;
		this.world = world;
		this.map = map;
		this.bounds = bounds;

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);
		body = world.createBody(bdef);

		shape.setAsBox(bounds.getWidth() / 2 / PyroGame.PPM, bounds.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
		fdef.shape = shape;
		fixture = body.createFixture(fdef);
	}

	public abstract void onCollision();

	public TiledMapTileLayer.Cell getCell() {
	    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
	    return layer.getCell((int) (body.getPosition().x * PyroGame.PPM / 64),
				 (int) (body.getPosition().y * PyroGame.PPM / 64));
	}

}
