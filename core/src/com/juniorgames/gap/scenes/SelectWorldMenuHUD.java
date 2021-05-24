package com.juniorgames.gap.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.juniorgames.gap.screens.SelectLevelMenuScreen;

public class SelectWorldMenuHUD implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont midFont;
    private Table table;
    private Texture selectWorldMenuButtonTexture, backButtonTexture;
    private TextButtonStyle selectWorldMenuButtonStyle, backButtonStyle;
    private TextButton world1Button;
    private TextButton world2Button;
    private TextButton world3Button;
    private TextButton backButton;
    private GapGame game;
    private AssetManager manager;
    private InputListener world1ButtonInputListener, world2ButtonInputListener, world3ButtonInputListener, backButtonInputListener;

    public SelectWorldMenuHUD(GapGame game) {
        this.game = game;
        this.manager = game.manager;
        batch = new SpriteBatch();
        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(this.stage);

        midFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);

        selectWorldMenuButtonTexture = manager.get("select-world-btn.png", Texture.class);
        selectWorldMenuButtonStyle = new TextButtonStyle();
        selectWorldMenuButtonStyle.font = midFont;
        selectWorldMenuButtonStyle.down = new TextureRegionDrawable(selectWorldMenuButtonTexture);
        selectWorldMenuButtonStyle.up = new TextureRegionDrawable(selectWorldMenuButtonTexture);
        selectWorldMenuButtonStyle.checked = new TextureRegionDrawable(selectWorldMenuButtonTexture);

        backButtonTexture = manager.get("back-btn.png", Texture.class);
        backButtonStyle = new TextButtonStyle();
        backButtonStyle.font = midFont;
        backButtonStyle.down = new TextureRegionDrawable(backButtonTexture);
        backButtonStyle.up = new TextureRegionDrawable(backButtonTexture);
        backButtonStyle.checked = new TextureRegionDrawable(backButtonTexture);

        world1Button = new TextButton("   WORLD 1   ", selectWorldMenuButtonStyle);
        world2Button = new TextButton("   WORLD 2   ", selectWorldMenuButtonStyle);
        world3Button = new TextButton("   WORLD 3   ", selectWorldMenuButtonStyle);
        backButton = new TextButton("BACK", backButtonStyle);

        world1ButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onWorld1ButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//newGameButtonInputListener
        world1Button.addListener(world1ButtonInputListener);//listener - newGameButton

        world2ButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onWorld2ButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//newGameButtonInputListener
        world2Button.addListener(world2ButtonInputListener);//listener - newGameButton

        world3ButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onWorld3ButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//ladGameButtonInputListener
        world3Button.addListener(world3ButtonInputListener);//listener - loadGameButton

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
        this.game.setScreen(new PlayMenuScreen(game));
    }

    private void onWorld1ButtonClicked() {
        game.selectedWorld = 1;
        this.game.setScreen(new SelectLevelMenuScreen(game));
    }

    private void onWorld2ButtonClicked() {
        game.selectedWorld = 2;
        this.game.setScreen(new SelectLevelMenuScreen(game));
    }

    private void onWorld3ButtonClicked() {
        game.selectedWorld = 3;
        this.game.setScreen(new SelectLevelMenuScreen(game));
    }

    private void initTable() {
        table = new Table();
        table.bottom();
        table.setFillParent(true);
        table.add(world1Button).pad(20).padBottom(160).padLeft(145);
        table.add(world2Button).pad(20).padBottom(160);
        table.add(world3Button).pad(20).padBottom(160).padRight(145);
        table.row();
        table.add(backButton).colspan(3).right();

        stage.addActor(table);
    }

    public void update(float dt) {
    }

    @Override
    public void dispose() {
        world1Button.removeListener(world1ButtonInputListener);
        world2Button.removeListener(world2ButtonInputListener);
        world3Button.removeListener(world3ButtonInputListener);
        backButton.removeListener(backButtonInputListener);
        stage.dispose();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }
}
