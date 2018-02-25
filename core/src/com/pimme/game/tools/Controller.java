package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.pimme.game.entities.Player;
import com.pimme.game.screens.PlayScreen;

/**
 * Created by Simon on 2/15/2018.
 */

public class Controller extends GestureAdapter {
    private Player player;
    private boolean keyBoard, multiTouch;
    private float screenWidth;

    public Controller(PlayScreen screen) {
        player = screen.getPlayer();
        keyBoard = Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard);
        multiTouch = Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
        screenWidth = Gdx.graphics.getWidth();
    }

    public void update(final float dt) {
        handleInput(dt);
    }

    private void handleInput(final float dt) {
        /* Keyboard device */
        if (keyBoard) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))// && body.getLinearVelocity().y <= MAX_FLY_SPEED)
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                player.moveRight(dt);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                player.moveLeft(dt);
        }
        /* Touch device */
        if (multiTouch) {
            for (int i = 0; i < 10; i++) { // Max 10 fingers
                if (Gdx.input.isTouched(i)) {
                    final int pointerX = Gdx.input.getX(i);
                    if (pointerX < screenWidth / 2)
                        player.moveLeft(dt);
                    if (pointerX > screenWidth / 2)
                        player.moveRight(dt);
                    if (Math.abs(Gdx.input.getDeltaY(i)) > 15)
                        player.jump();
                }
            }
        }
    }

    /**
     * A user touches the screen.
     */
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    /**
     * A user touches the screen and lifts the finger again.
     * The finger must not move outside a specified square area
     * around the initial touch position for a tap to be registered.
     * Multiple consecutive taps will be detected if the user performs
     * taps within a specified time interval.
     */
    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }


    /**
     * A user touches the screen for some time.
     */
    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    /**
     * A user dragged the finger across the screen, then lifted it.
     * Useful to implement swipe gestures.
     */
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    /**
     * A user drags a finger across the screen. The detector will report
     * the current touch coordinates as well as the delta between the current
     * and previous touch positions. Useful to implement camera panning in 2D.
     */
//    @Override
//    public boolean pan(float x, float y, float deltaX, float deltaY) {
//        if (Math.abs(deltaY) > 15 && x > screenWidth/4 && x < screenWidth - screenWidth / 4)
//            player.jump();
//        return false;
//    }
}

