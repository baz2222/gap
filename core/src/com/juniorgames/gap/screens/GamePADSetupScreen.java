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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;

public class GamePADSetupScreen extends ScreenAdapter {
    private GapGame game;
    private AssetManager manager;
    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TmxMapLoader mapLoader;
    private TiledMap platformMap;
    private OrthogonalTiledMapRenderer renderer;
    private Stage stage;
    private TextButton backButton;
    private Table table;

    public GamePADSetupScreen(GapGame game) {
        this.game = game;
        this.manager = game.manager;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(game.GAME_WIDTH / game.GAME_PPM, game.GAME_HEIGHT / game.GAME_PPM, camera);
        this.camera.position.set(game.GAME_WIDTH / 2, game.GAME_HEIGHT / 2, 0);
        this.stage = new Stage(viewport, batch);
        this.mapLoader = game.mapLoader;
        this.platformMap = mapLoader.load("level0-0.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(platformMap, 1 / game.GAME_PPM);

        Gdx.input.setInputProcessor(this.stage);

        //=====================================================================================================================
        this.game.playMusic(0);
        backButton = this.game.createTextMenuButton("BACK TO MENU", this.game.menuButtonTexture, new MenuScreen(this.game));
        createTable();
        //=====================================================================================================================
    }//constructor

    private void createTable() {
        table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(new Label("Congratulations! You are finish the game!", game.labelStyle)).padLeft(90).padRight(90).padTop(20);
        table.row();
        table.add(backButton).padTop(100);
        this.stage.addActor(table);
    }

    public void update(float dt) {
        handleInput(dt);
        camera.update();
        renderer.setView(camera);
    }

    private void handleInput(float dt) {
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        table.draw(batch, 1f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
    }
}
