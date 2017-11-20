package com.pimme.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.graphics.PlayScreen;

public class HealthPack extends InteractiveObject
{
    public HealthPack(PlayScreen screen, final World world, final TiledMap map, final Rectangle bounds)
    {
	super(screen, world, map, bounds);
	fixture.setUserData(this);
    }

    @Override public void onCollision() {
        screen.getHud().addHealth(20);
    }
}
