package com.pimme.game.entities.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;
import com.pimme.game.tools.Graphics;

public class MovingEnemy extends Enemy
{
	private float moveDistance;
	private boolean horizontal;
	private String type;
	private float spawnX, spawnY;
	private float speed;
	private float stateTime;
	private boolean setToDestroy, destroyed;
	public MovingEnemy(final PlayScreen screen, final MapObject object) {
		super(screen, object);
		destroyed = false;
		setToDestroy = false;
		stateTime = 0;

		// Get properties
		moveDistance = properties.get("distance", Float.class);
		horizontal = properties.get("horizontal", Boolean.class);
		speed = properties.get("speed", Float.class);
		type = properties.get("type", String.class);

		defineEnemy();
		body.setActive(false);
		setAnimation();
		initVelocity();
	}

	@Override public void update(final float dt) {
		stateTime += dt;
		if (setToDestroy && !destroyed) {
			world.destroyBody(body);
			destroyed = true;
			setRegion(getDeadTexture());
			stateTime = 0;
		} else if (!destroyed) {
			checkDistance();
			setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
			setRegion(getFrame());
		}
	}

	private TextureRegion getFrame() {
		region = animation.getKeyFrame(stateTime, true);
		if((region.isFlipX() && velocity.x < 0.1f) || (!region.isFlipX() && velocity.x > 0.1f))
			region.flip(true, false);
		return region;
	}

	private TextureRegion getDeadTexture() {
		switch (type) {
			case "fly":
				return Graphics.flyDead;
			case "fish":
				return Graphics.fishDead;
			case "snail":
				return Graphics.snailDead;
			default:
				return Graphics.flyDead;
		}
	}

	private void checkDistance() {
		if (horizontal) {
			if ((body.getPosition().x > (spawnX + moveDistance) && velocity.x > 0))
				velocity.x = -speed;
			else if (body.getPosition().x < (spawnX - moveDistance) && velocity.x < 0)
				velocity.x = speed;
		}
		else {
			if (body.getPosition().y > spawnY + moveDistance)
				velocity.y = -speed;
			if(body.getPosition().y < spawnY - moveDistance)
				velocity.y = speed;
		}
		body.setLinearVelocity(velocity);
	}


	private void setAnimation() {
		switch (type) {
			case "fly":
				animation = Graphics.flyAnimation;
				break;
			case "fish":
				animation = Graphics.fishAnimation;
				break;
			case "snail":
				animation = Graphics.snailAnimation;
				break;

		}
	}

	private void initVelocity() {
		if(horizontal) velocity = new Vector2(speed, 0);
		else velocity = new Vector2(0, speed);

		body.setLinearVelocity(velocity);
	}

	@Override public void defineEnemy()  {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(getX(), getY());
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		body = world.createBody(bdef);
		body.setGravityScale(0);
		spawnX = body.getPosition().x;
		spawnY = body.getPosition().y;

		shape.setAsBox(bounds.getWidth() / 2 / PyroGame.PPM, bounds.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
		fdef.filter.categoryBits = PyroGame.ENEMY_BIT;
		fdef.filter.maskBits = PyroGame.BRICK_BIT |
				PyroGame.PLAYER_BIT;


		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);

		//Create the Head here:
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-18, 21).scl(1 / PyroGame.PPM);
		vertice[1] = new Vector2(18, 21).scl(1 / PyroGame.PPM);
		vertice[2] = new Vector2(-3, 3).scl(1 / PyroGame.PPM);
		vertice[3] = new Vector2(3, 3).scl(1 / PyroGame.PPM);
		head.set(vertice);

		fdef.shape = head;
		fdef.isSensor = true;
		fdef.restitution = 1f;
		fdef.filter.categoryBits = PyroGame.ENEMY_HEAD_BIT;

		body.createFixture(fdef).setUserData(this);
	}

	public void draw(Batch batch) {
		if (!destroyed || stateTime < 1)
			super.draw(batch);
	}

	@Override public void hitOnHead() {
		setToDestroy = true;
		for (int i = 0; i < body.getFixtureList().size; i++) { // to not cause collision right at bounce
			Filter filter = new Filter();
			filter.categoryBits = PyroGame.NOTHING_BIT;
			body.getFixtureList().get(i).setFilterData(filter);
		}
		screen.getPlayer().body.setLinearVelocity(new Vector2(screen.getPlayer().body.getLinearVelocity().x, 3.5f));
		PyroGame.manager.get("audio/sounds/enemyBounce.wav", Sound.class).play();
	}
}
