package com.pimme.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.utils.Array;

public final class Graphics
{
	//Player
	public static Animation<TextureRegion> pyretRun;
	public static Animation<TextureRegion> pyretJump;
	public static Animation<TextureRegion> pyretFalling;
	public static Animation<TextureRegion> pyretStanding;
	public static Animation<TextureRegion> pyretFlying;
	public static Animation<TextureRegion> pyretHurt;
	public static Animation<TextureRegion> pyretSwim;

	//Enemies
	public static Animation<TextureRegion> flyAnimation;
	public static Animation<TextureRegion> fishAnimation;
	public static Animation<TextureRegion> snailAnimation;

	public static TextureRegion flyDead, fishDead, snailDead;
    public static TextureRegion bounceTexture, idleBounceTexture;
	//Sprites
	private static Texture puppySprite;
	private static Texture tilesSprite;
	private static Texture enemiesSprite;
	private static Texture platform;


	public static void init() {
		puppySprite = new Texture(Gdx.files.internal("puppy_pack.png"));
	    tilesSprite = new Texture(Gdx.files.internal("spritesheet_tiles32.png"));
	    bounceTexture = new TextureRegion(tilesSprite, 32, 425, 32, 23);
        idleBounceTexture = new TextureRegion(tilesSprite, 32, 463, 32, 17);
		platform = new Texture(Gdx.files.internal("winter_ledges.png"));
		enemiesSprite = new Texture(Gdx.files.internal("enemies_spritesheet.png"));
		initPlayer();
		initEnemies();

	}

	private static void initPlayer() {
		Array<TextureRegion> frames = new Array<>();
		//RUNNING
		frames.add(new TextureRegion(puppySprite, 62,  372, 257, 178));
		frames.add(new TextureRegion(puppySprite, 358, 377, 254, 167));
		frames.add(new TextureRegion(puppySprite, 666,  373, 260, 166));
		frames.add(new TextureRegion(puppySprite, 952,  361, 259, 177));
		frames.add(new TextureRegion(puppySprite, 1278,  371, 266, 158));
		frames.add(new TextureRegion(puppySprite, 1589,  375, 254, 171));
		pyretRun = new Animation<>(0.09f, frames);
		//JUMPING
		frames.clear();
		frames.add(new TextureRegion(puppySprite, 84, 1597, 253, 167));
		frames.add(new TextureRegion(puppySprite, 423, 1536, 249, 203));
		frames.add(new TextureRegion(puppySprite, 751, 1430, 253, 209));
		frames.add(new TextureRegion(puppySprite, 1086, 1384, 262, 193));
		frames.add(new TextureRegion(puppySprite, 1426, 1358, 254, 171));
		pyretJump = new Animation<>(0.15f, frames);

		//FALLING
		frames.clear();
//        frames.add(new TextureRegion(getTexture(), 1781, 1375, 254, 172));
		frames.add(new TextureRegion(puppySprite, 2126, 1482, 257, 196));
//        frames.add(new TextureRegion(getTexture(), 2468, 1594, 253, 170));
		pyretFalling = new Animation<>(0.2f, frames);
		//STANDING
		frames.clear();
		frames.add(new TextureRegion(puppySprite, 58, 70, 257, 163));
		frames.add(new TextureRegion(puppySprite, 367, 71, 257, 162));
		frames.add(new TextureRegion(puppySprite, 677, 72, 257, 161));
		frames.add(new TextureRegion(puppySprite, 991, 72, 257, 161));
		pyretStanding = new Animation<>(0.7f, frames);

		//FLYING
		frames.clear();
		frames.add(new TextureRegion(puppySprite, 78, 688, 481, 237));
		frames.add(new TextureRegion(puppySprite, 627, 680, 501, 253));
		frames.add(new TextureRegion(puppySprite, 1209, 681, 516, 251));
		pyretFlying = new Animation<>(0.2f, frames);
		frames.clear();

		frames.add(new TextureRegion(puppySprite, 63, 1054, 259, 209));
		frames.add(new TextureRegion(puppySprite, 365, 1054, 259, 209));
		frames.add(new TextureRegion(puppySprite, 680, 1088, 243, 167));

		frames.add(new TextureRegion(puppySprite, 972, 1134, 252, 129));
		frames.add(new TextureRegion(puppySprite, 1271, 1147, 263, 119));
		frames.add(new TextureRegion(puppySprite, 1582, 1148, 259, 118));
		pyretHurt = new Animation<>(0.1f, frames);
		frames.clear();

		frames.add(new TextureRegion(puppySprite, 62,  372, 257, 178));
		frames.add(new TextureRegion(puppySprite, 358, 377, 254, 167));
		frames.add(new TextureRegion(puppySprite, 666,  373, 260, 166));
		frames.add(new TextureRegion(puppySprite, 952,  361, 259, 177));
		frames.add(new TextureRegion(puppySprite, 1278,  371, 266, 158));
		frames.add(new TextureRegion(puppySprite, 1589,  375, 254, 171));
		pyretSwim = new Animation<>(0.07f, frames);
		frames.clear();

	}

	private static void initEnemies() {
		flyDead = new TextureRegion(enemiesSprite, 143, 0,59,33);
		fishDead = new TextureRegion(enemiesSprite, 0,69,66,42);
		snailDead = new TextureRegion(enemiesSprite, 148,118,44,30);
		Array<TextureRegion> frames = new Array<>();
		//Fly
		frames.add(new TextureRegion(enemiesSprite, 0, 0, 75, 31));
		frames.add(new TextureRegion(enemiesSprite, 0, 32, 72, 36));
		flyAnimation = new Animation<>(0.5f, frames);
		frames.clear();
		//Fish
		frames.add(new TextureRegion(enemiesSprite, 76, 0, 66, 42));
		frames.add(new TextureRegion(enemiesSprite, 73,43,62,43));
		fishAnimation = new Animation<>(0.5f, frames);
		frames.clear();
		//Snail
		frames.add(new TextureRegion(enemiesSprite, 67, 87, 57, 31));
		frames.add(new TextureRegion(enemiesSprite, 143, 34, 54, 31));
		snailAnimation = new Animation<>(0.5f, frames);
		frames.clear();

	}
}
