package com.pimme.game.entities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.Hud;
import com.pimme.game.graphics.PlayScreen;

public class HealthPack extends InteractiveObject
{
    public HealthPack(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PyroGame.HEART_BIT);
    }

    @Override public void onCollision() {
        screen.getHud().addHealth(20);
        getCell().setTile(tileSet.getTile(10));
    }
}
