package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.GameOverHUD;
import com.juniorgames.gap.scenes.GamePADSetupHUD;
import com.juniorgames.gap.scenes.KeysMenuHUD;
import com.juniorgames.gap.scenes.PlayMenuHUD;

public class GamePADSetupScreen extends ScreenAdapter {
    private GapGame game;
    private AssetManager manager;
    private SpriteBatch batch;
    private Viewport viewport;
    private GamePADSetupHUD gamePADSetupHUD;
    private OrthographicCamera camera;
    //tiled map values
    private TmxMapLoader mapLoader;
    private TiledMap platformMap;
    private OrthogonalTiledMapRenderer renderer;
    private Stage stage;

    public GamePADSetupScreen(GapGame game) {
        this.game = game;
        this.manager = game.manager;
        this.batch = game.batch;
        this.viewport = game.viewport;
        this.camera = game.camera;
        this.stage = game.stage;
        //gamePADSetupHUD = new GamePADSetupHUD(this.game);
        this.mapLoader = game.mapLoader;
        this.platformMap = game.mapLoader.load("level0-0.tmx");
        //this.renderer = this.game.renderer;
        this.renderer = new OrthogonalTiledMapRenderer(platformMap, 1 / this.game.GAME_PPM);
        //this.renderer.setMap(platformMap);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        game.playMusic(0);

    }//constructor

    public void update(float dt) {
        handleInput(dt);
        //gamePADSetupHUD.update(dt);
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
        batch.setProjectionMatrix(this.stage.getCamera().combined);
        //gamePADSetupHUD.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        //gamePADSetupHUD.resize(width, height);
    }

    @Override
    public void dispose() {
        //gamePADSetupHUD.dispose();
    }
}
