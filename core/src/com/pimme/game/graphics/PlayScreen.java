package com.pimme.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pimme.game.PyroGame;
import com.pimme.game.entities.Platform;
import com.pimme.game.entities.Player;
import com.pimme.game.entities.Player.State;
import com.pimme.game.PyroGame.Level;
import com.pimme.game.tools.B2World;
import com.pimme.game.tools.Highscore;

public class PlayScreen implements Screen
{
    private PyroGame game;
    private OrthographicCamera gameCam;
    private FitViewport viewPort;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private ShapeRenderer shapeRenderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2World worldCreator;

    private Player player;
    private Hud hud;
    private Level level;

    private static final float FPS = 60;

    public PlayScreen(PyroGame game, Level level) {
        this.game = game;
        this.level = level;

        // cam to follow character
        gameCam = new OrthographicCamera();
        gameCam.zoom = 2f;

        // FitViewPort to maintain virtual aspect ratios despite screen size
        viewPort = new FitViewport(PyroGame.V_WIDTH / PyroGame.PPM, PyroGame.V_HEIGHT / PyroGame.PPM, gameCam);
        // Load our map and setup map renderer
        mapLoader = new TmxMapLoader();
        generateLevel();
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PyroGame.PPM);
        shapeRenderer = new ShapeRenderer();
        //gameCam.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);
        gameCam.position.x = PyroGame.V_WIDTH / PyroGame.PPM;
        gameCam.position.y = PyroGame.V_HEIGHT / PyroGame.PPM;

        world = new World(new Vector2(0, -8), true); // 1 parameter gravity, 2 sleep objects at rest
        b2dr = new Box2DDebugRenderer();
        player = new Player(world, this);
        hud = new Hud(this, game.batch);

        worldCreator = new B2World(this, map);

    }

    @Override public void show() {}

    public void update(final float dt) {
        world.step(1 / FPS, 6, 2);
        player.update(dt);
        updatePlatforms();
        setCameraPos();
        gameCam.update();
        renderer.setView(gameCam);
    }

    private void updatePlatforms() {
        for (Platform platform : worldCreator.getPlatforms()) {
            platform.update();
        }
    }


    private void setCameraPos() {
        if (player.body.getPosition().x > (PyroGame.V_WIDTH / PyroGame.PPM))
            gameCam.position.x = player.body.getPosition().x;
        if (player.body.getPosition().y > (PyroGame.V_HEIGHT / PyroGame.PPM))
            gameCam.position.y = player.body.getPosition().y;
    }



    @Override public void render(final float delta) {
        update(delta);
        //CLEAR SCREEN
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game map
        renderer.render();  // renders textures to bodies
        b2dr.render(world, gameCam.combined);


        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Platform pLatform : worldCreator.getPlatforms())
            pLatform.draw(game.batch);
        game.batch.end();

        hud.render();
        hud.stage.draw();

        if(gameOver()) {
            GameOverScreen gameOverScreen = new GameOverScreen(game);
            gameOverScreen.setScore(hud.getScore());
            if (Highscore.getHighScore(5) < hud.getScore()) Highscore.setHighScore(hud.getScore());
            game.setScreen(gameOverScreen);
            dispose();
        }

    }

    private void generateLevel() {
        switch (level) {
            case MENS:
                map = mapLoader.load("winter_map.tmx");
                break;
            case BOUNCE:
                map = mapLoader.load("bounce_map.tmx");
                break;
        }
    }

    public void reachedGoal() {
        game.setScreen(new GameOverScreen(game));
    }


    public Player getPlayer() {
        return player;
    }
    public Hud getHud() { return hud; }
    public World getWorld() { return world; }
    public TiledMap getMap() { return map; }
    public OrthographicCamera getGameCam() {
        return gameCam;
    }

    public Level getLevel() {
        return level;
    }

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
        hud.dispose();
    }
}
