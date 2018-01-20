package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by smurf on 2017-11-14.
 */
public class Bounce extends InteractiveObject
{
    private TiledMapTile defaultTile;
    private TiledMapTile bounceTile;

    public Bounce(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
	setCategoryFilter(PyroGame.BOUNCE_BIT);


	defaultTile = map.getTileSets().getTileSet("tileset32_new").getTile(114);
	bounceTile = map.getTileSets().getTileSet("tileset32_new").getTile(106);
    }

    @Override
    public void onCollision() {
        if (screen.getPlayer().body.getLinearVelocity().y < 0) {
            System.out.println("coll");
            getCell().setTile(bounceTile);
            screen.getPlayer().bounce();
        new Timer().schedule(
       		new TimerTask() {
       		    @Override
       		    public void run() {
       			getCell().setTile(defaultTile);
       		    }
       		}, 800
       	);
    }}
}
