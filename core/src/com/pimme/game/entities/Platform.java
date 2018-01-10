package com.pimme.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public class Platform extends Sprite
{
    private PlayScreen screen;
    private World world;
    private TiledMap map;
    private Rectangle bounds;
    private Body body;
    private Fixture fixture;

    private static final float MOVE_DISTANCE = 1.5f;
    private static final float SPEED = 1;
    private float spawnX, spawnY;
    public Platform(PlayScreen screen, MapObject object) {
        super(new Texture(Gdx.files.internal("winter_ledges.png")));
	this.screen = screen;
	this.world = screen.getWorld();
	this.map = screen.getMap();
	this.bounds = ((RectangleMapObject) object).getRectangle();

	definePlatform();
    }

    public void update() {
	setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
	if (body.getPosition().x > spawnX + MOVE_DISTANCE)
	    body.setLinearVelocity(new Vector2(-SPEED, 0));
	else if (body.getPosition().x < spawnX - MOVE_DISTANCE)
	    body.setLinearVelocity(new Vector2(SPEED, 0));
    }

    private void definePlatform() {
	BodyDef bdef = new BodyDef();
	FixtureDef fdef = new FixtureDef();
	PolygonShape shape = new PolygonShape();

	bdef.type = BodyType.KinematicBody;
	bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);

	body = world.createBody(bdef);

	shape.setAsBox(bounds.getWidth() / 2 / PyroGame.PPM, bounds.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
	fdef.shape = shape;
	fixture = body.createFixture(fdef);

	fixture.setUserData(this);

	spawnX = body.getPosition().x;
	spawnY = body.getPosition().y;
	body.setLinearVelocity(new Vector2(-SPEED, 0));
	setSize(bounds.getWidth() / PyroGame.PPM, bounds.getHeight() / PyroGame.PPM);

    }

    public void setCategoryFilter(short filterBit) {
	Filter filter = new Filter();
	filter.categoryBits = filterBit;
	fixture.setFilterData(filter);
    }
}
