package com.pimme.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.graphics.PlayScreen;

/**
 * Created by smurf on 2017-11-12.
 */


public class Coin extends InteractiveObject {
    public Coin(PlayScreen screen, World world, TiledMap map, Rectangle bounds) {
        super(screen, world, map, bounds);
        fixture.setUserData(this);
        screen.addCoin(this);
    }

    @Override
    public void onCollision() {
        screen.getHud().addScore(100);
        screen.removeCoin(this);
        getCell().setTile(null);
    }
}
