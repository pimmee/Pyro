package com.pimme.game.entities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

/**
 * Created by smurf on 2017-11-12.
 */


public class Coin extends InteractiveObject {
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("miscSprite");
        fixture.setUserData(this);
        setCategoryFilter(PyroGame.COIN_BIT);
        screen.addCoin(this);
    }

    @Override
    public void onCollision() {
        screen.getHud().addScore(100);
        getCell().setTile(tileSet.getTile(10));
        screen.removeCoin(this);
        getCell().setTile(null);
    }

    public void moveTowards(Player p) {
        body.applyLinearImpulse(new Vector2(5f, 0), body.getWorldCenter(), true);
//        do {
//            if (body.getPosition().x > p.getX())
//                body.applyLinearImpulse(new Vector2(-5f, 0), body.getWorldCenter(), true);
//            if (body.getPosition().x < p.getX())
//                body.applyLinearImpulse(new Vector2(5f, 0), body.getWorldCenter(), true);
//            if (body.getPosition().y < p.getY())
//                body.applyLinearImpulse(new Vector2(0, 5f), body.getWorldCenter(), true);
//            if (body.getPosition().y > p.getX())
//                body.applyLinearImpulse(new Vector2(0, -5f), body.getWorldCenter(), true);
//        } while(fixture.getUserData() != null);
 }
}
