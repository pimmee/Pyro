package com.pimme.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.pimme.game.graphics.PlayScreen;

import java.util.Timer;
import java.util.TimerTask;

public class MagnetPowerup extends InteractiveObject
{
    public MagnetPowerup(PlayScreen screen, World world, TiledMap map, Rectangle bounds) {
	super(screen, world, map, bounds);
    }
    @Override public void onCollision() {

	Timer timer = new Timer();
	timer.schedule(new TimerTask()
	{
	    @Override public void run() {
		final float playerX = screen.getPlayer().getX();
		final float playerY = screen.getPlayer().getY();
		for(Coin coin: screen.getCoins()) {
		    float dx = coin.body.getPosition().x - playerX;
		    float dy = coin.body.getPosition().y - playerY;
		    if (dx * dx + dy * dy <= 5) {
			screen.getHud().addScore(100);
			screen.removeCoin(coin);
		    }
		}
	    }
	}, 0, 5000);


    }
}
