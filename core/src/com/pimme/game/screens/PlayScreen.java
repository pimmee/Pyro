package com.pimme.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pimme.game.PyroGame;
import com.pimme.game.sprites.Pyret;
import com.pimme.game.tools.B2WorldCreator;

public class PlayScreen implements Screen
{
    private PyroGame game;
    private OrthographicCamera gameCam;
    Texture texture;
    private Viewport gamePort;
    private TextureAtlas atlas;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator worldCreator;

    private Pyret player;

    public PlayScreen(PyroGame game) {
        this.game = game;
        texture = new Texture("badlogic.jpg");
        // cam to follow character
        gameCam = new OrthographicCamera();

        // FitViewPort to maintain virtual aspect ratios despite screen size
        gamePort = new FitViewport(PyroGame.V_WIDTH / PyroGame.PPM,PyroGame.V_HEIGHT / PyroGame.PPM, gameCam);


        // Load our map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PyroGame.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true); // 1 parameter gravity, 2 sleep objects at rest
        b2dr = new Box2DDebugRenderer();
	atlas = new TextureAtlas("puppy_pack.atlas");
        player = new Pyret(world, this);

        worldCreator = new B2WorldCreator(world, map);

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
    @Override public void show() {

    }

    public void update(final float dt) {
        handleInput(dt);

        world.step(1/60f, 6, 2);
        player.update(dt);
        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    public void handleInput(final float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
    	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
    	    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

	if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
	    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

    }

    @Override public void render(final float delta) {
        update(delta);
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game map
        renderer.render();

        b2dr.render(world, gameCam.combined);
	game.batch.setProjectionMatrix(gameCam.combined);
	game.batch.begin();
	player.draw(game.batch);
	game.batch.end();

    }

    @Override public void resize(final int width, final int height) {
	gamePort.update(width, height);
    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public void hide() {

    }

    @Override public void dispose() {
	map.dispose();
	renderer.dispose();
	world.dispose();
	b2dr.dispose();
	//hud.dispose()
    }
}
