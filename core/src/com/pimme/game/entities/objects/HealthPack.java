package com.pimme.game.entities.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.PyroGame;
import com.pimme.game.screens.PlayScreen;

public class HealthPack extends InteractiveObject
{
    public HealthPack(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PyroGame.HP_BIT);
    }

    @Override public void onCollision() {
        screen.getHud().addHealth(20);
        getCell().setTile(null);
        setCategoryFilter(PyroGame.NOTHING_BIT);
        PyroGame.manager.get("audio/sounds/healthpack.wav", Sound.class).play();
    }
}
