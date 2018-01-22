package com.pimme.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

public abstract class Enemy extends Sprite
{
	protected World world;
	protected PlayScreen screen;
	protected TiledMap map;
	public Body body;
	protected Fixture fixture;
	protected Rectangle bounds;

	protected Vector2 velocity;
	protected Animation<TextureRegion> animation;
	protected TextureRegion region;

	protected MapProperties properties;

	public Enemy(PlayScreen screen, MapObject object) {
		this.world = screen.getWorld();
		this.map = screen.getMap();
		this.screen = screen;
		this.bounds = ((RectangleMapObject) object).getRectangle();
		properties = object.getProperties();
		setPosition((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);
		setSize(bounds.getWidth() / PyroGame.PPM, bounds.getHeight() / PyroGame.PPM);
	}

	public abstract void defineEnemy();
	public abstract void update(final float dt);
	public abstract void hitOnHead();

	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}


	public void reverseVelocity() {
		if (Math.abs(velocity.x) > 0) velocity.x = -velocity.x;
		if (Math.abs(velocity.y) > 0) velocity.y = -velocity.y;
	}

}
