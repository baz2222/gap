package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.GPADSetupStage;

public class GPADSetupScreen extends ScreenAdapter {
    private GapGame game;
    private TmxMapLoader loader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private GPADSetupStage stage;

    public GPADSetupScreen(GapGame game) {
        this.game = game;
        game.batch = new SpriteBatch();
        game.cam = new OrthographicCamera();
        game.viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, game.cam);
        loader = game.loader;
        map = loader.load("level0-0.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / game.GAME_PPM);
        game.viewport.getCamera().position.set(game.GAME_WIDTH / 2, game.GAME_HEIGHT / 2, 0);
        stage = new GPADSetupStage(game);
    }//constructor


    public void update(float dt) {
        handleInput(dt);
        game.cam.update();
        renderer.setView(game.cam);//render only what cam shows up
    }

    private void handleInput(float dt) {
    }

    @Override
    public void render(float dt) {
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void dispose() {
    }
}
