package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Player;
import com.pimme.game.entities.Player.State;
import com.pimme.game.graphics.PlayScreen;

import java.util.Timer;
import java.util.TimerTask;

public class FlyPowerup extends InteractiveObject
{
    private Player player;
    public FlyPowerup(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PyroGame.FLY_BIT);
        player = screen.getPlayer();
    }

    @Override public void onCollision() {
        getCell().setTile(null);
        setCategoryFilter(PyroGame.NOTHING_BIT);
        player.currentState = State.FLYING;
        player.body.setGravityScale(0);
	final float defaultSpriteWidth = player.getWidth();
	final float defaultSpriteHeight = player.getHeight();
	player.setSize(player.getWidth()*1.9f, player.getHeight() * 1.9f);
       	new Timer().schedule(
       		new TimerTask() {
       		    @Override
       		    public void run() {
       			player.body.setGravityScale(1);
       			player.currentState = State.FALLING;
       			player.setSize(defaultSpriteWidth, defaultSpriteHeight);
       		    }
       		}, 10000
       	);
           }

}
