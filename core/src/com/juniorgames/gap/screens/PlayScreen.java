package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.HUD;
import com.juniorgames.gap.sprites.Player;
import com.juniorgames.gap.tools.B2WorldCreator;
import com.juniorgames.gap.tools.WorldContactListener;

import static com.juniorgames.gap.GapGame.GAME_PPM;

public class PlayScreen extends ScreenAdapter {
    private GapGame game;
    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private Viewport viewport;
    private HUD hud;
    //tiled map values
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //box2d values
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;

    public PlayScreen(GapGame game) {
        atlas = new TextureAtlas("player.pack");
        this.game = game;
        camera = new OrthographicCamera();
        //viewport = new StretchViewport(480*2,272*2, camera);
        viewport = new FitViewport(GapGame.GAME_WIDTH / GAME_PPM, GapGame.GAME_HEIGHT / GAME_PPM, camera);
        //viewport = new ScreenViewport(camera);
        hud = new HUD(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / GAME_PPM);//scaling map with PPM

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map);
        player = new Player(world, this);
        world.setContactListener(new WorldContactListener());
    }//constructor

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);//60 times per second
        //camera.position.x = player.b2body.getPosition().x; //move camera with the character
        player.update(dt);
        camera.update();
        renderer.setView(camera);
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }

    private void handleInput(float dt) {
        //jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }
        //move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(0.2f,0), player.b2body.getWorldCenter(), true);
        }
        //move left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-0.2f,0), player.b2body.getWorldCenter(), true);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game map
        renderer.render();
        //render Box2DDebugLines
        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        b2dr.dispose();
        renderer.dispose();
        world.dispose();
        hud.dispose();
    }
}
