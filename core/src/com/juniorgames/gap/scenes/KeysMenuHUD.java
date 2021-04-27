package com.juniorgames.gap.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.LevelScreen;
import com.juniorgames.gap.screens.MenuScreen;
import com.juniorgames.gap.screens.PlayMenuScreen;

public class KeysMenuHUD implements Disposable {
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

    public KeysMenuHUD(GapGame game, AssetManager manager) {
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
        this.game.setScreen(new MenuScreen(game, manager));
    }

    private void initTable() {
        table = new Table();
        table.bottom();
        table.setFillParent(true);
        table.add().pad(40).padLeft(250).padBottom(160);
        table.add().pad(40).padRight(250).padBottom(160);
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
