package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.PyroGame;
import com.pimme.game.graphics.PlayScreen;

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
        if (screen.getPlayer().body.getLinearVelocity().y < 0)
            screen.getPlayer().bounce();
    }
}
