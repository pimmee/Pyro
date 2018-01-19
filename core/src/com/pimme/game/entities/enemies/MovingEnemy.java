package com.pimme.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;
import com.pimme.game.tools.Graphics;

public class MovingEnemy extends Enemy
{
	private float moveDistance;
	private boolean horizontal;
	private String type;
	private float spawnX, spawnY;
	private float speed;
	private Vector2 velocity;
	private Animation<TextureRegion> animation;
	private float stateTime, angle;
	private boolean setToDestroy, destroyed, movingRight;
	public MovingEnemy(final PlayScreen screen, final MapObject object) {
		super(screen, object);//, new Texture(Gdx.files.internal("fishPink_swim.png")));
		defineEnemy();
//	setCategoryFilter(PyroGame.ENEMY_BIT);
		destroyed = false;
		setToDestroy = false;
		movingRight = false;
		stateTime = 0;

		// Get properties
		moveDistance = properties.get("distance", Float.class);
		horizontal = properties.get("horizontal", Boolean.class);
		speed = properties.get("speed", Float.class);
		type = properties.get("type", String.class);
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
			setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
			setRegion(getFrame(dt));
			checkDistance();
		}
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
			if (body.getPosition().x >= spawnX + moveDistance || body.getPosition().x <= spawnX - moveDistance) {
				body.setLinearVelocity(-body.getLinearVelocity().x, body.getLinearVelocity().y);
				System.out.println("weird");
			}
		} else {
			if (body.getPosition().y >= spawnY + moveDistance || body.getPosition().y <= spawnY - moveDistance) {
				body.setLinearVelocity(body.getLinearVelocity().x, -speed);
			}
		}
	}

	private TextureRegion getFrame(final float dt) {
		TextureRegion region = animation.getKeyFrame(dt, true);
		if ((body.getLinearVelocity().x < 0 || !movingRight)) {// && !region.isFlipX()) { //If running to the left but faceing right
			region.flip(true, false);
			movingRight = false;
		} else if (body.getLinearVelocity().x > 0 || movingRight) {// && region.isFlipX()) {
			region.flip(true, false);
			movingRight = true;
		}
		return region;
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
		bdef.type = BodyType.KinematicBody;
		bdef.position.set(getX(), getY());
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();


		body = world.createBody(bdef);
		spawnX = body.getPosition().x;
		spawnY = body.getPosition().y;

		shape.setAsBox(bounds.getWidth() / 2 / PyroGame.PPM, bounds.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
		fdef.filter.categoryBits = PyroGame.ENEMY_BIT;


		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);

		//Create the Head here:
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-15, 20).scl(1 / PyroGame.PPM);
		vertice[1] = new Vector2(15, 20).scl(1 / PyroGame.PPM);
		vertice[2] = new Vector2(-3, 3).scl(1 / PyroGame.PPM);
		vertice[3] = new Vector2(3, 3).scl(1 / PyroGame.PPM);
		head.set(vertice);

		fdef.shape = head;
		fdef.restitution = 0.5f;
		fdef.filter.categoryBits = PyroGame.ENEMY_HEAD_BIT;
		body.createFixture(fdef).setUserData(this);
	}

	public void draw(Batch batch) {
		if (!destroyed || stateTime < 1)
			super.draw(batch);
	}

	@Override public void hitOnHead() {
		setToDestroy = true;
		screen.getPlayer().body.setLinearVelocity(new Vector2(screen.getPlayer().body.getLinearVelocity().x,3.5f));
	}


}
