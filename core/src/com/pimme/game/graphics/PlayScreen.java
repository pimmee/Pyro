package com.pimme.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Player;
import com.pimme.game.entities.Player.State;
import com.pimme.game.tools.B2WorldCreator;

public class PlayScreen implements Screen
{
    public static final float X_VEL= 0.08f;
    public static final float JUMP_VEL = 3.5f;
    private float WATER_HEIGHT = 0f;
    private PyroGame game;
    private OrthographicCamera camera;
    Texture texture;
    private StretchViewport gamePort;
    private TextureAtlas atlas;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private ShapeRenderer shapeRenderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator worldCreator;

    private Player player;

    public PlayScreen(PyroGame game) {
        this.game = game;
        texture = new Texture("badlogic.jpg");
        // cam to follow character
        camera = new OrthographicCamera();

        // FitViewPort to maintain virtual aspect ratios despite screen size
        gamePort = new StretchViewport(PyroGame.V_WIDTH / PyroGame.PPM, PyroGame.V_HEIGHT / PyroGame.PPM, camera);


        // Load our map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PyroGame.PPM);
        shapeRenderer = new ShapeRenderer();
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -8), true); // 1 parameter gravity, 2 sleep objects at rest
        b2dr = new Box2DDebugRenderer();
        atlas = new TextureAtlas("puppy_pack.atlas");
        player = new Player(world, this);

        worldCreator = new B2WorldCreator(world, map);

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
    @Override public void show() {

    }

    public void update(final float dt) {
        handleInput(dt);

        world.step(1/ 60f, 6, 2);
        player.update(dt);
        if(player.b2body.getPosition().x > PyroGame.V_WIDTH / PyroGame.PPM/ 2)
            camera.position.x = player.b2body.getPosition().x;
        camera.update();
        renderer.setView(camera);
    }

    public void handleInput(final float dt) {
        if (Gdx.input.isKeyJustPressed(Keys.UP) && player.getState() != State.JUMPING && player.getState() != State.FALLING)
            player.b2body.applyLinearImpulse(new Vector2(0, JUMP_VEL), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(X_VEL, 0), player.b2body.getWorldCenter(), true);

        if(Gdx.input.isKeyPressed(Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-X_VEL, 0), player.b2body.getWorldCenter(), true);

    }

    @Override public void render(final float delta) {
        update(delta);
        //CLEAR SCREEN
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game map
        renderer.render();  // renders textures to bodies
        //b2dr.render(world, camera.combined);
        drawWater();


        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

    }

    private void drawWater() {
        //Draws water
        if (player.getY() <= WATER_HEIGHT)
            player.reduceHealth();
        WATER_HEIGHT += 0.001f;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 1, 0.5f);
        shapeRenderer.rect(0, 0, 10, WATER_HEIGHT);
        shapeRenderer.end();
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
