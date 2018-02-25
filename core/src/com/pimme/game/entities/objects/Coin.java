package com.pimme.game.entities.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.*;
import com.pimme.game.screens.PlayScreen;

/**
 * Created by smurf on 2017-11-12.
 */


public class Coin extends InteractiveObject
{

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PyroGame.COIN_BIT);
    }

    @Override
    public void onCollision() {
        screen.getHud().addScore(100);
        getCell().setTile(null);
        setCategoryFilter(PyroGame.NOTHING_BIT);
        manager.getAssetManager().get("audio/sounds/coin2.wav", Sound.class).play();
    }

}
