package com.pimme.game.entities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

public class Brick
{
    private Fixture fixture;
    private World world;
    private Rectangle bounds;
    public Brick(PlayScreen screen, MapObject object) {
        this.world = screen.getWorld();
        this.bounds = ((RectangleMapObject) object).getRectangle();

        defineBrick();
        setCategoryFilter(PyroGame.BRICK_BIT);
    }

    private void defineBrick() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PyroGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PyroGame.PPM);

        Body body = world.createBody(bdef);

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
