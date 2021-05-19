package com.juniorgames.gap.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.LevelScreen;
import com.juniorgames.gap.screens.MenuScreen;
import com.juniorgames.gap.screens.PlayMenuScreen;

public class TasksMenuHUD implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont midFont;
    private Table table;
    private Texture backButtonTexture;
    private TextButton backButton;
    private GapGame game;
    private AssetManager manager;
    private InputListener backButtonInputListener;
    private TextButtonStyle backButtonStyle;

    public TasksMenuHUD(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        batch = new SpriteBatch();
        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(this.stage);

        midFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);

        backButtonTexture = manager.get("back-btn.png", Texture.class);
        backButtonStyle = new TextButtonStyle();
        backButtonStyle.font = midFont;
        backButtonStyle.down = new TextureRegionDrawable(backButtonTexture);
        backButtonStyle.up = new TextureRegionDrawable(backButtonTexture);
        backButtonStyle.checked = new TextureRegionDrawable(backButtonTexture);

        backButton = new TextButton("BACK", backButtonStyle);

        backButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onBackButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//backButtonInputListener
        backButton.addListener(backButtonInputListener);//listener - backButton

        initTable();
    }//constructor

    private void onBackButtonClicked() {
        this.game.setScreen(new MenuScreen(game));
    }

    private void initTable() {
        Image image;
        table = new Table();
        table.bottom();
        table.padTop(75);
        table.setFillParent(true);
        for (int i = 1; i <= 12; i++) {
            Texture texture = manager.get(game.tasksTracker.tasks.get(i - 1).taskStripImagePath, Texture.class);
            int width = texture.getWidth() / 2;
            if (game.tasksTracker.tasks.get(i - 1).completed == false) {
                image = new Image(new TextureRegion(texture, 0, 0, width, 96));
            } else {
                image = new Image(new TextureRegion(texture, 0 + width, 0, width, 96));
            }//if else
            if (i > 1 && i % 2 != 0) {
                table.row();
            }
            table.add(image).padTop(5).padBottom(5).padLeft(30).padRight(40);
        }
        table.row();
        table.add(backButton).colspan(2).right();

        stage.addActor(table);
    }

    public void update(float dt) {
    }

    @Override
    public void dispose() {
        backButton.removeListener(backButtonInputListener);
        stage.dispose();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }
}
