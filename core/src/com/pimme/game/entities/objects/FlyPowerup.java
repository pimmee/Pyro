package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.graphics.PlayScreen;

public class FlyPowerup extends InteractiveObject
{
    public FlyPowerup(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
    }

    @Override public void onCollision() {

    }
}
