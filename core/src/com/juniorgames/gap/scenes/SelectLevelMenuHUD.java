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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.LevelScreen;
import com.juniorgames.gap.screens.MenuScreen;
import com.juniorgames.gap.screens.SelectWorldMenuScreen;

public class SelectLevelMenuHUD implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont midFont;
    private Table table;
    private Texture selectLevelMenuButtonTexture, backButtonTexture;
    private TextButtonStyle selectLevelMenuButtonStyle, backButtonStyle;
    private Array<TextButton> levelButtons;
    private TextButton backButton;
    private GapGame game;
    private AssetManager manager;
    private InputListener levelButtonInputListener, backButtonInputListener;
    private int i;

    public SelectLevelMenuHUD(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        batch = new SpriteBatch();
        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(this.stage);

        midFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);

        selectLevelMenuButtonTexture = manager.get("select-level-btn.png", Texture.class);
        selectLevelMenuButtonStyle = new TextButtonStyle();
        selectLevelMenuButtonStyle.font = midFont;
        selectLevelMenuButtonStyle.down = new TextureRegionDrawable(selectLevelMenuButtonTexture);
        selectLevelMenuButtonStyle.up = new TextureRegionDrawable(selectLevelMenuButtonTexture);
        selectLevelMenuButtonStyle.checked = new TextureRegionDrawable(selectLevelMenuButtonTexture);

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

        table = new Table();
        table.bottom().right();
        table.setFillParent(true);

        stage.addActor(table);
        levelButtons = new Array<>();

        levelButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println();
                game.selectedLevel = Integer.parseInt(event.getTarget().getName());
                game.stopMusic();
                game.savedGame.world = game.selectedWorld;
                game.savedGame.level = game.selectedLevel;
                game.savedGame.save();
                game.setScreen(new LevelScreen(game, manager));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//levelButtonInputListener

        for (i = 1; i <= 10; i++) {
            levelButtons.add(new TextButton(String.valueOf(i), selectLevelMenuButtonStyle));
            levelButtons.get(i-1).getLabel().setName(String.valueOf(i));
            levelButtons.get(i-1).addListener(levelButtonInputListener);
            if (i % 5 == 0){
                table.add(levelButtons.get(i-1)).pad(40).fill(1.5f,1).padRight(140);
                table.row();
            }else{
                table.add(levelButtons.get(i-1)).pad(40).fill(1.5f,1);
            }
        }//for

        table.row();
        table.add(backButton).colspan(5).right().padTop(100);

    }//constructor

    private void onBackButtonClicked() {
        this.game.setScreen(new SelectWorldMenuScreen(game, manager));
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
