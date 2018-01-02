package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.*;
import com.pimme.game.graphics.PlayScreen;

/**
 * Created by smurf on 2017-11-12.
 */


public class Coin extends InteractiveObject
{

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PyroGame.COIN_BIT);
    }

    @Override
    public void onCollision() {
        screen.getHud().addScore(100);
        getCell().setTile(null);
        setCategoryFilter(PyroGame.NOTHING_BIT);
    }

    public void moveTowards(Player p) {
        //body.setType(BodyDef.BodyType.DynamicBody);
        while (Math.abs(body.getPosition().x + p.getX()) > 3 || Math.abs(body.getPosition().y + p.getY()) > 3) {
            //System.out.println(Math.abs(body.getPosition().x + p.getX()));
            System.out.println(body.getPosition().x);
//            if (body.getPosition().x > p.getX()) body.setTransform(body.getPosition().x - 1, body.getPosition().y, body.getAngle());
//            if (body.getPosition().x < p.getX()) body.setTransform(body.getPosition().x + 1, body.getPosition().y, body.getAngle());
//            if (body.getPosition().y < p.getY()) body.setTransform(body.getPosition().x, body.getPosition().y + 1, body.getAngle());
//            if (body.getPosition().y > p.getX()) body.setTransform(body.getPosition().x, body.getPosition().y - 1, body.getAngle());
            if (body.getPosition().x > p.getX()) body.applyLinearImpulse(new Vector2(-1f, 0), body.getWorldCenter(), true);
            if (body.getPosition().x < p.getX()) body.applyLinearImpulse(new Vector2(1f, 0), body.getWorldCenter(), true);
            if (body.getPosition().y < p.getY()) body.applyLinearImpulse(new Vector2(0, 1f), body.getWorldCenter(), true);
            if (body.getPosition().y > p.getX()) body.applyLinearImpulse(new Vector2(0, -1f), body.getWorldCenter(), true);
        }

    }

}
