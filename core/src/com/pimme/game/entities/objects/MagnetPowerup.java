package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import com.pimme.game.graphics.PlayScreen;

public class MagnetPowerup extends InteractiveObject
{
    public MagnetPowerup(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        //setCategoryFilter(PyroGame.MAGNET_BIT);
    }
    @Override public void onCollision() {
        float playerX = screen.getPlayer().getX();
        float playerY = screen.getPlayer().getY();
        getCell().setTile(tileSet.getTile(1000));
        //Array change as it is looping over it
        Array<Coin> toRemove = new Array();
	for(Coin coin: screen.getCoins()) {
	    float dx = coin.body.getPosition().x - playerX;
	    float dy = coin.body.getPosition().y - playerY;
	    if (dx * dx + dy * dy <= 50) {
	        //screen.getHud().addScore(100);
	        //toRemove.add(coin);
            coin.moveTowards(screen.getPlayer());
	    }
	}
	for(Coin coin: toRemove) {
	    screen.removeCoin(coin);
        coin.getCell().setTile(tileSet.getTile(10));
    }
    }
}
