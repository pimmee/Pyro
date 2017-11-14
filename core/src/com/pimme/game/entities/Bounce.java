package com.pimme.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.graphics.PlayScreen;

/**
 * Created by smurf on 2017-11-14.
 */
public class Bounce extends InteractiveObject {
    public Bounce(PlayScreen screen, World world, TiledMap map, Rectangle bounds) {
        super(screen, world, map, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onCollision() {
        screen.getPlayer().bounce();
    }
}
