package com.pimme.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pimme.game.graphics.PlayScreen;

public class PyroGame extends Game
{
	public SpriteBatch batch;
	public static final int V_WIDTH = 500;
	public static final int V_HEIGHT = 300;
	public static final float PPM = 100; // pixel per meter

	public static final short DEFAULT_BIT = 1;
	public static final short PYRET_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short BOUNCE_BIT = 16;
	public static final short FLY_BIT = 32;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}
}
