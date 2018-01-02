package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

public class Goal extends InteractiveObject
{
    public Goal(final PlayScreen screen, final MapObject object) {
	super(screen, object);
	fixture.setUserData(this);
	setCategoryFilter(PyroGame.GOAL_BIT);
    }

    @Override public void onCollision() {

    }
}
