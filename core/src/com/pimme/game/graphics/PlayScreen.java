package com.pimme.game.graphics;

import com.badlogic.gdx.Gdx;
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
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Coin;
import com.pimme.game.entities.Player;
import com.pimme.game.entities.Player.State;
import com.pimme.game.tools.B2WorldCreator;
import com.pimme.game.tools.CollisionListener;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen
{
    private float WATER_HEIGHT = 0f;
    private PyroGame game;
    private OrthographicCamera gameCam;
    Texture texture;
    private FitViewport viewPort;
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
    private Hud hud;
    private List<Coin> coins = new ArrayList<Coin>();

    public PlayScreen(PyroGame game) {
        this.game = game;
        texture = new Texture("badlogic.jpg");
        // cam to follow character
        gameCam = new OrthographicCamera();
        gameCam.zoom = 2f;

        // FitViewPort to maintain virtual aspect ratios despite screen size
        viewPort = new FitViewport(PyroGame.V_WIDTH / PyroGame.PPM, PyroGame.V_HEIGHT / PyroGame.PPM, gameCam);

        System.out.println(Gdx.files.internal("level1test.tmx").file().getAbsolutePath());
        // Load our map and setup map renderer
        mapLoader = new TmxMapLoader();
        //map = mapLoader.load("level1.tmx");
        //map = mapLoader.load("bounce_map.tmx");
	map = mapLoader.load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PyroGame.PPM);
        shapeRenderer = new ShapeRenderer();
        gameCam.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -8), true); // 1 parameter gravity, 2 sleep objects at rest
        b2dr = new Box2DDebugRenderer();
        atlas = new TextureAtlas("puppy_pack.atlas");
        player = new Player(world, this);
        hud = new Hud(this, game.batch);

        worldCreator = new B2WorldCreator(this, world, map);
        world.setContactListener(new CollisionListener());

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
    @Override public void show() {

    }

    public void update(final float dt) {
        world.step(1/ 60f, 6, 2);
        player.update(dt);
        setCameraPos();
        gameCam.update();
        renderer.setView(gameCam);
    }

    private void setCameraPos() {
        if (player.body.getPosition().x > (PyroGame.V_WIDTH / PyroGame.PPM))
            gameCam.position.x = player.body.getPosition().x;
        else gameCam.position.x = PyroGame.V_WIDTH / PyroGame.PPM;
        gameCam.position.y = player.body.getPosition().y;
    }



    @Override public void render(final float delta) {
        update(delta);
        //CLEAR SCREEN
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game map
        renderer.render();  // renders textures to bodies
        //b2dr.render(world, gameCam.combined);
        drawWater();
        hud.render();


        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        hud.stage.draw();

        if(gameOver()) {
            GameOverScreen gameOverScreen = new GameOverScreen(game);
            gameOverScreen.setScore(hud.getScore());
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }

    private void drawWater() {
        //Draws water
        WATER_HEIGHT += 0.001f;
        if (player.getY() <= WATER_HEIGHT)
            hud.reduceHealth(0.2f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(gameCam.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 1, 0.5f);
        shapeRenderer.rect(0, 0, 10, WATER_HEIGHT);
        shapeRenderer.end();
    }

    public Player getPlayer() {
        return player;
    }
    public Hud getHud() { return hud; }
    public void addCoin(Coin coin) {
        coins.add(coin);
    }

    public void removeCoin(Coin coin) {
        coins.remove(coin);
    }
    public Iterable<Coin> getCoins() { return coins; }
    private boolean gameOver() {
        return(player.currentState == State.DEAD);
    }

    @Override public void resize(final int width, final int height) {
        viewPort.update(width, height);
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
