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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.LevelScreen;
import com.juniorgames.gap.screens.MenuScreen;
import com.juniorgames.gap.tools.Task;

public class LevelHUD implements Disposable {
    public Stage stage;
    public GapGame game;
    private SpriteBatch batch;
    public AssetManager manager;
    private Viewport viewport;
    private Integer levelTimer;
    private float timeCount;
    private static Integer score;

    private Label levelNameLabel;
    private ImageButton backButton;
    private TextButton continueButton, exitButton, restartButton;
    private ImageButton.ImageButtonStyle backButtonStyle;
    private Texture backButtonTexture, pauseButtonTexture;
    private InputListener backButtonInputListener, continueButtonInputListener, exitButtonInputListener, restartButtonListener;
    private BitmapFont midFont;
    private Table tableHUD, pauseTable;

    private TextButton.TextButtonStyle pauseButtonStyle;

    public LevelHUD(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        batch = new SpriteBatch();
        tableHUD = new Table();
        pauseTable = new Table();

        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(this.stage);

        midFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);
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

        tableHUD.top();
        tableHUD.padTop(20);
        tableHUD.setFillParent(true);
        tableHUD.add(backButton).padLeft(10).padRight(320);
        tableHUD.add(levelNameLabel).padRight(380);

        stage.addActor(tableHUD);

        pauseTable.setFillParent(true);
        pauseTable.center();
        pauseTable.add(continueButton).pad(10);
        pauseTable.row();
        pauseTable.add(restartButton).pad(10);
        pauseTable.row();
        pauseTable.add(exitButton).pad(10);

        //tutorial label

    }//constructor

    public void showTutorial(String text) {
        Label.LabelStyle textLabelStyle = new Label.LabelStyle();
        textLabelStyle.font = midFont;
        textLabelStyle.fontColor = Color.WHITE;
        Label textLabel = new Label(text, textLabelStyle);
        textLabel.setWrap(true);
        textLabel.setWidth(800);
        textLabel.setAlignment(Align.center);
        textLabel.setPosition(game.GAME_WIDTH / 2 - textLabel.getWidth() / 2, game.GAME_HEIGHT * 0.75f);
        stage.addActor(textLabel);
        Timer timer = new Timer();
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                hideTutorial(textLabel);
            }//run
        };
        timer.scheduleTask(task, 8);
    }

    public void showTask(Task task){
        Image img = new Image(new TextureRegion(manager.get(task.taskImagePath, Texture.class), 0, 0, 256, 128));
        img.setPosition(game.GAME_WIDTH * 0.85f - img.getWidth() / 2, game.GAME_HEIGHT * 0.6f);
        stage.addActor(img);
        System.out.println("task done");
        Timer timer = new Timer();
        Timer.Task timerTask = new Timer.Task() {
            @Override
            public void run() {
                hideTask(img);
            }//run
        };
        timer.scheduleTask(timerTask, 5);
    }

    private void hideTask(Image image){
        image.remove();
    }

    private void hideTutorial(Label t){
        t.remove();
    }

    private void onBackButtonClicked() {
        game.stopMusic();
        tableHUD.remove();
        stage.addActor(pauseTable);
        game.gamePaused = true;
    }

    private void onContinueButtonClicked() {
        pauseTable.remove();
        stage.addActor(tableHUD);
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

    public void update(float dt) {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
