package com.pimme.game.entities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public class Brick
{
    private Fixture fixture;
    private Body body;
    private PlayScreen screen;
    private World world;
    private TiledMap map;
    private Rectangle bounds;
    public Brick(PlayScreen screen, MapObject object) {


        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / PyroGame.PPM, bounds.getHeight() / 2 / PyroGame.PPM); // start at x and goes all directions
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

        fixture.setUserData(this);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }


}
