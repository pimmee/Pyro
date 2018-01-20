package com.pimme.game.entities.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;
import com.pimme.game.tools.Graphics;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by smurf on 2017-11-14.
 */
public class Bounce extends InteractiveObject
{

    public Bounce(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
	setCategoryFilter(PyroGame.BOUNCE_BIT);

    }

    @Override
    public void onCollision() {
        if (screen.getPlayer().body.getLinearVelocity().y < 0) {
            getCell().setTile(new StaticTiledMapTile(Graphics.bounceTexture));
            screen.getPlayer().bounce();
        new Timer().schedule(
       		new TimerTask() {
       		    @Override
       		    public void run() {
       			getCell().setTile(new StaticTiledMapTile(Graphics.idleBounceTexture));
       		    }
       		}, 800
       	);
    }}
}
