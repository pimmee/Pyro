package com.pimme.game.entities.objects;

import com.badlogic.gdx.maps.MapObject;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Player;
import com.pimme.game.screens.PlayScreen;

public class Bomb extends InteractiveObject {
    public Bomb(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PyroGame.BOMB_BIT);
    }

    @Override
    public void onCollision() {
        screen.getPlayer().currentState = Player.State.DEAD;
        getCell().setTile(null);
        setCategoryFilter(PyroGame.NOTHING_BIT);
    }
}
