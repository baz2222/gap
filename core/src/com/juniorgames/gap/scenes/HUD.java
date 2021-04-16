package com.juniorgames.gap.scenes;

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

public class HUD implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Integer levelTimer;
    private float timeCount;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelNameLabel;
    Label levelLabel;
    Label characterLabel;
    public HUD(SpriteBatch spriteBatch){
        levelTimer = 300;
        timeCount = 0;
        score = 0;
        viewport = new FitViewport(GapGame.GAME_WIDTH, GapGame.GAME_WIDTH, new OrthographicCamera());
        stage = new Stage(viewport,spriteBatch);

        BitmapFont HUDFont = new BitmapFont();
        Table tableHUD = new Table();
        tableHUD.top();
        tableHUD.setFillParent(true);
        countdownLabel = new Label(String.format("%03d",levelTimer), new Label.LabelStyle(HUDFont, Color.WHITE));
        scoreLabel = new Label(String.format("%06d",score), new Label.LabelStyle(HUDFont, Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(HUDFont, Color.WHITE));
        //level name. In future will be World-Level name...
        levelLabel = new Label("LEVEL", new Label.LabelStyle(HUDFont, Color.WHITE));
        levelNameLabel = new Label("World 1-1", new Label.LabelStyle(HUDFont, Color.WHITE));
        //character name = RED, BLUE, WHITE...
        characterLabel = new Label("Player Name", new Label.LabelStyle(HUDFont, Color.WHITE));
        tableHUD.add(characterLabel).expandX().padTop(50);
        tableHUD.add(levelLabel).expandX().padTop(50);
        tableHUD.add(timeLabel).expandX().padTop(50);
        tableHUD.row();
        tableHUD.add(scoreLabel).expandX().padTop(30);
        tableHUD.add(levelNameLabel).expandX().padTop(30);
        tableHUD.add(countdownLabel).expandX().padTop(30);

        stage.addActor(tableHUD);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
