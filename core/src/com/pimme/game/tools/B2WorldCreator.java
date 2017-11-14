package com.pimme.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Bounce;
import com.pimme.game.entities.Brick;
import com.pimme.game.entities.HealthPack;
import com.pimme.game.graphics.PlayScreen;

public class B2WorldCreator
{
	public B2WorldCreator(PlayScreen screen, World world, TiledMap map) {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef(); // to add fixture to body
		Body body;

//		for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
//			Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//			bdef.type = BodyDef.BodyType.StaticBody;
//			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PyroGame.PPM, (rect.getY() + rect.getHeight() / 2) / PyroGame.PPM);
//			body = world.createBody(bdef);
//
//			shape.setAsBox(rect.getWidth() / 2 / PyroGame.PPM, rect.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
//			fdef.shape = shape;
//			body.createFixture(fdef);
//		}

		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			new Bounce(screen, world, map, rect);
		}

		//coins
//		for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
//			Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//			bdef.type = BodyDef.BodyType.StaticBody;
//			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PyroGame.PPM, (rect.getY() + rect.getHeight() / 2) / PyroGame.PPM);
//			body = world.createBody(bdef);
//
//			shape.setAsBox(rect.getWidth() / 2 / PyroGame.PPM, rect.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
//			fdef.shape = shape;
//			body.createFixture(fdef);
//		}
		//Bricks
		for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			new Brick(screen, world, map, rect);
		}
	}
}
