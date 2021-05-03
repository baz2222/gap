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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.LevelScreen;
import com.juniorgames.gap.screens.MenuScreen;

public class LevelHUD implements Disposable {
    public Stage stage;
    public GapGame game;
    private SpriteBatch batch;
    public AssetManager manager;
    private Viewport viewport;
    private Integer levelTimer;
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private static Label scoreLabel;
    private Label levelNameLabel;
    private ImageButton backButton;
    private TextButton continueButton, exitButton, restartButton;
    private ImageButton.ImageButtonStyle backButtonStyle;
    private Texture backButtonTexture, pauseButtonTexture;
    private InputListener backButtonInputListener, continueButtonInputListener, exitButtonInputListener, restartButtonListener;
    private BitmapFont midFont;

    private Table pauseTable;
    private TextButton.TextButtonStyle pauseButtonStyle;

    public LevelHUD(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        batch = new SpriteBatch();
        levelTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(this.stage);

        midFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);

        countdownLabel = new Label(String.format("%03d", levelTimer), new Label.LabelStyle(midFont, Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(midFont, Color.WHITE));
        levelNameLabel = new Label("World " + game.savedGame.world + "-" + game.savedGame.level, new Label.LabelStyle(midFont, Color.WHITE));

        backButtonTexture = manager.get("left-arrow-btn.png", Texture.class);
        pauseButtonTexture = manager.get("menu-btn.png", Texture.class);

        backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.down = new TextureRegionDrawable(backButtonTexture);
        backButtonStyle.up = new TextureRegionDrawable(backButtonTexture);
        backButtonStyle.checked = new TextureRegionDrawable(backButtonTexture);
        backButton = new ImageButton(backButtonStyle);

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
        };//newGameButtonInputListener
        backButton.addListener(backButtonInputListener);//listener - newGameButton

        pauseButtonStyle = new TextButton.TextButtonStyle();
        pauseButtonStyle.font = midFont;
        pauseButtonStyle.down = new TextureRegionDrawable(pauseButtonTexture);
        pauseButtonStyle.up = new TextureRegionDrawable(pauseButtonTexture);
        pauseButtonStyle.checked = new TextureRegionDrawable(pauseButtonTexture);
        continueButton = new TextButton("CONTINUE", pauseButtonStyle);
        restartButton = new TextButton("RESTART LEVEL", pauseButtonStyle);
        exitButton = new TextButton("EXIT TO MENU", pauseButtonStyle);

        continueButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onContinueButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//continueButtonInputListener
        continueButton.addListener(continueButtonInputListener);//listener - continueButton

        restartButtonListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onRestartButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//restartButtonInputListener
        restartButton.addListener(restartButtonListener);//listener - continueButton

        exitButtonInputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onExitButtonClicked();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        };//exitButtonInputListener
        exitButton.addListener(exitButtonInputListener);//listener - exitButton

        Table tableHUD = new Table();
        tableHUD.top();
        tableHUD.setFillParent(true);
        tableHUD.add(backButton).padRight(150);
        tableHUD.add(scoreLabel).padRight(150);
        tableHUD.add(levelNameLabel).padRight(150);
        tableHUD.add(countdownLabel).padRight(120);

        stage.addActor(tableHUD);

        pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();
        pauseTable.add(continueButton).pad(10);
        pauseTable.row();
        pauseTable.add(restartButton).pad(10);
        pauseTable.row();
        pauseTable.add(exitButton).pad(10);
    }//constructor

    private void onBackButtonClicked() {
        game.stopMusic();
        stage.addActor(pauseTable);
        game.gamePaused = true;
    }

    private void onContinueButtonClicked() {
        pauseTable.remove();
        game.gamePaused = false;
        game.playMusic(game.savedGame.world);
    }

    private void onExitButtonClicked() {
        game.gamePaused = false;
        game.savedGame.save();
        game.stopMusic();
        game.setScreen(new MenuScreen(game, manager));
    }

    private void onRestartButtonClicked() {
        game.gamePaused = false;
        game.stopMusic();
        game.setScreen(new LevelScreen(game, manager));
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {//one second
            levelTimer--;
            countdownLabel.setText(String.format("%03d", levelTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
