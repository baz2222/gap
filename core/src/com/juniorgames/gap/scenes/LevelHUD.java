package com.juniorgames.gap.scenes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;

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
    private Label timeLabel;
    private Label levelNameLabel;
    private Label levelLabel;
    private Label characterLabel;

    public LevelHUD(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        batch = new SpriteBatch();
        levelTimer = 300;
        timeCount = 0;
        score = 0;
        viewport = new FitViewport(game.GAME_WIDTH, game.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        BitmapFont levelHUDFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);
        Table tableHUD = new Table();
        tableHUD.top();
        tableHUD.setFillParent(true);
        countdownLabel = new Label(String.format("%03d", levelTimer), new Label.LabelStyle(levelHUDFont, Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(levelHUDFont, Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(levelHUDFont, Color.WHITE));
        //level name. In future will be World-Level name...
        levelLabel = new Label("LEVEL", new Label.LabelStyle(levelHUDFont, Color.WHITE));
        levelNameLabel = new Label("World 1-1", new Label.LabelStyle(levelHUDFont, Color.WHITE));
        //character name = RED, BLUE, WHITE...
        characterLabel = new Label("Player Name", new Label.LabelStyle(levelHUDFont, Color.WHITE));
        tableHUD.add(characterLabel).expandX().padTop(50);
        tableHUD.add(levelLabel).expandX().padTop(50);
        tableHUD.add(timeLabel).expandX().padTop(50);
        tableHUD.row();
        tableHUD.add(scoreLabel).expandX().padTop(30);
        tableHUD.add(levelNameLabel).expandX().padTop(30);
        tableHUD.add(countdownLabel).expandX().padTop(30);

        stage.addActor(tableHUD);
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
