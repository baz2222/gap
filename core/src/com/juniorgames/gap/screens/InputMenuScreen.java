package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.KeysMenuHUD;

public class InputMenuScreen extends ScreenAdapter {
    private GapGame game;
    private AssetManager manager;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private KeysMenuHUD keysMenuHud;
    //tiled map values
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public InputMenuScreen(GapGame game) {
        this.game = game;
        this.manager = game.manager;
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(game.GAME_WIDTH / game.GAME_PPM, game.GAME_HEIGHT / game.GAME_PPM, camera);

        keysMenuHud = new KeysMenuHUD(game);

        maploader = new TmxMapLoader();
        map = maploader.load("level0-0.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / game.GAME_PPM);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
    }//constructor

    public void update(float dt) {
        handleInput(dt);
        keysMenuHud.update(dt);
        camera.update();
        renderer.setView(camera);
    }

    private void handleInput(float dt) {
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render screen map
        renderer.render();
        //render HUD
        batch.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(keysMenuHud.stage.getCamera().combined);
        keysMenuHud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        keysMenuHud.resize(width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        keysMenuHud.dispose();
        manager.dispose();
    }
}
