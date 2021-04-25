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
import com.juniorgames.gap.screens.PlayMenuScreen;

public class MenuHUD implements Disposable {
    public Stage stage;
    public Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont midFont, bigFont;
    private Table table;
    private Texture menuButtonTexture;
    private TextButtonStyle menuButtonStyle;
    private TextButton playButton;
    private TextButton keysButton;
    private TextButton tasksButton;
    private Label gameNameLabel, byLabel;
    private GapGame game;
    private AssetManager manager;
    private InputListener playButtonListener;

    public MenuHUD(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        batch = new SpriteBatch();
        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(this.stage);
        midFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);
        bigFont = manager.get("fonts/big-font.fnt", BitmapFont.class);
        menuButtonTexture = manager.get("menu-btn.png", Texture.class);
        menuButtonStyle = new TextButtonStyle();
        menuButtonStyle.font = midFont;
        menuButtonStyle.down = new TextureRegionDrawable(menuButtonTexture);
        menuButtonStyle.up = new TextureRegionDrawable(menuButtonTexture);
        menuButtonStyle.checked = new TextureRegionDrawable(menuButtonTexture);
        playButton = new TextButton("PLAY", menuButtonStyle);
        keysButton = new TextButton("KEYS", menuButtonStyle);
        tasksButton = new TextButton("TASKS", menuButtonStyle);
        gameNameLabel = new Label("GAP", new Label.LabelStyle(bigFont, Color.WHITE));
        byLabel = new Label("by Junior Games", new Label.LabelStyle(midFont, Color.WHITE));

        playButtonListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onPlayButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };
        playButton.addListener(playButtonListener);

        initTable();
    }//constructor

    private void onPlayButtonClicked() {
        this.game.setScreen(new PlayMenuScreen(game, manager));
    }

    private void initTable() {
        table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(gameNameLabel).colspan(3).pad(20);
        table.row();
        table.add(playButton).pad(20);
        table.add(keysButton).pad(20);
        table.add(tasksButton).pad(20);
        table.row();
        table.add(byLabel).colspan(3).padTop(40);
        stage.addActor(table);
    }

    public void update(float dt) {
    }

    @Override
    public void dispose() {
        playButton.removeListener(playButtonListener);
        stage.dispose();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
