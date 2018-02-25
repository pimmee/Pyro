package com.pimme.game.entities.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

import java.util.Timer;
import java.util.TimerTask;

public class Tampon extends InteractiveObject
{
    public Tampon(final PlayScreen screen, final MapObject object) {
	super(screen, object);
	fixture.setUserData(this);
	setCategoryFilter(PyroGame.TAMPON_BIT);
    }

    @Override public void onCollision() {
	screen.getHud().setTamponActive(true);
	manager.getAssetManager().get("audio/sounds/tampon.wav", Sound.class).play();
	setCategoryFilter(PyroGame.NOTHING_BIT);
	nullifyCells();
	new Timer().schedule(
		new TimerTask() {
		    @Override
		    public void run() {
			screen.getHud().setTamponActive(false);
		    }
		}, 4000
	);
    }
    /**
     * 	Texture takes up 4 cells. This method returns all cells associated with the body
     */
    public void nullifyCells() {
	TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
	float tileSize = layer.getTileWidth();
	layer.getCell((int)(body.getPosition().x * PyroGame.PPM / tileSize),
				(int)(body.getPosition().y * PyroGame.PPM / tileSize)).setTile(null);
	layer.getCell((int)(body.getPosition().x * PyroGame.PPM / tileSize -1),
				(int)(body.getPosition().y * PyroGame.PPM / tileSize)).setTile(null);
	layer.getCell((int)(body.getPosition().x * PyroGame.PPM / tileSize),
				(int)(body.getPosition().y * PyroGame.PPM / tileSize - 1)).setTile(null);
	layer.getCell((int)(body.getPosition().x * PyroGame.PPM / tileSize - 1),
				(int)(body.getPosition().y * PyroGame.PPM / tileSize - 1)).setTile(null);
    }
}
