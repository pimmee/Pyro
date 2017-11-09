package com.pimme.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Brick extends InteractiveObject
{
    public Brick(final World world, final TiledMap map, final Rectangle bounds)
    {
	super(world, map, bounds);
    }
}
