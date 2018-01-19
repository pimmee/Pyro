package com.pimme.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public abstract class Enemy extends Sprite
{
    protected World world;
    protected PlayScreen screen;
    protected TiledMap map;
    protected Body body;
    protected Fixture fixture;
    protected Rectangle bounds;

    protected MapProperties properties;

    public Enemy(PlayScreen screen, MapObject object) {
	this.world = screen.getWorld();
	this.map = screen.getMap();
	this.screen = screen;
	this.bounds = ((RectangleMapObject) object).getRectangle();
	properties = object.getProperties();
	setPosition((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);
	setSize(bounds.getWidth() / PyroGame.PPM, bounds.getHeight() / PyroGame.PPM);
	//setSize(0, 0);

    }

    public abstract void defineEnemy();
    public abstract void update(final float dt);
    public abstract void hitOnHead();

    public void setCategoryFilter(short filterBit) {
	Filter filter = new Filter();
	filter.categoryBits = filterBit;
	fixture.setFilterData(filter);
    }

    public TextureRegion getPicture() {
	TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
	float tileSize = layer.getTileWidth();
	int scaledX = (int)(body.getPosition().x * PyroGame.PPM / tileSize);
	int scaledY = (int)(body.getPosition().y * PyroGame.PPM / tileSize);
	TextureRegion region = layer.getCell(scaledX, scaledY).getTile().getTextureRegion(); // Extract texture

	layer.getCell(scaledX, scaledY).setTile(null); // Nullify texture
	return region;}

}
