package com.pimme.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public class MovingEnemy extends Enemy
{
    private float moveDistance;
    private boolean horizontal;
    private float spawnX, spawnY;
    private float speed;
    public MovingEnemy(final PlayScreen screen, final MapObject object) {
	super(screen, object);//, new Texture(Gdx.files.internal("fishPink_swim.png")));
	defineEnemy();
	setCategoryFilter(PyroGame.ENEMY_BIT);
	body.setLinearVelocity(velocity);
    }

    @Override public void defineEnemy()  {
	BodyDef bdef = new BodyDef();
	FixtureDef fdef = new FixtureDef();
	PolygonShape shape = new PolygonShape();

	bdef.type = BodyType.KinematicBody;
	bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);

	body = world.createBody(bdef);
	spawnX = body.getPosition().x;
	spawnY = body.getPosition().y;

	shape.setAsBox(bounds.getWidth() / 2 / PyroGame.PPM, bounds.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
	fdef.shape = shape;
	fdef.isSensor = true;
	fixture = body.createFixture(fdef);
	fixture.setUserData(this);

	setRegion(getPicture(), 0, 0, 32, 32);
	moveDistance = properties.get("distance", Float.class);
	horizontal = properties.get("horizontal", Boolean.class);
	speed = properties.get("speed", Float.class);
	if(horizontal) velocity = new Vector2(speed, 0);
	else velocity = new Vector2(0, speed);
    }

    @Override public void update(final float dt) {
	setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

	if (horizontal) {
	    if (body.getPosition().x > spawnX + moveDistance || body.getPosition().x < spawnX - moveDistance){
		body.setLinearVelocity(-body.getLinearVelocity().x, body.getLinearVelocity().y);
	}
	} else {
	    if (body.getPosition().y > spawnY + moveDistance || body.getPosition().y < spawnY - moveDistance)
	    	body.setLinearVelocity(body.getLinearVelocity().x, -body.getLinearVelocity().y);
	}}
}
