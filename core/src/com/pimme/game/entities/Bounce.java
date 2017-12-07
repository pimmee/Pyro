package com.pimme.game.entities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.graphics.PlayScreen;

/**
 * Created by smurf on 2017-11-14.
 */
public class Bounce extends InteractiveObject {
    public Bounce(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
    }

    @Override
    public void onCollision() {
        screen.getPlayer().bounce();
    }
}
