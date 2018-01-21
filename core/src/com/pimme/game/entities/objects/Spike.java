package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

public class Spike extends InteractiveObject
{
    public Spike(final PlayScreen screen, final MapObject object) {
	super(screen, object);
	fixture.setUserData(this);
	setCategoryFilter(PyroGame.SPIKE_BIT);
    }

    @Override public void onCollision() {
        if (!screen.getPlayer().isTouchingSpike())
            screen.getHud().reduceHealth(5);
        screen.getPlayer().setTouchingSpike(true);
    }
}
