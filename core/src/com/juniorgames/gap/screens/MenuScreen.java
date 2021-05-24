package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.juniorgames.gap.GapGame;

public class MenuScreen extends ScreenAdapter {
    private GapGame game;
    private TextButton playBtn, inputBtn, tasksBtn;
    private Label gameNameLbl, byLbl;
    private Table hud;

    public MenuScreen(GapGame game) {
        this.game = game;
        game.playBackgroundMusic("menu", true);
        playBtn = game.createTextMenuButton("PLAY", game.menuBtnTex, new PlayMenuScreen(game));
        inputBtn = game.createTextMenuButton("INPUT", game.menuBtnTex, new InputMenuScreen(game));
        tasksBtn = game.createTextMenuButton("TASKS", game.menuBtnTex, new TasksMenuScreen(game));
        gameNameLbl = new Label("GAP", game.bigLabelStyle);
        byLbl = new Label("by Vasyl Velhus", game.labelStyle);
        createHUD();
        Gdx.input.setInputProcessor(game.stage);
    }//constructor

    private void createHUD() {
        hud = new Table();
        hud.setFillParent(true);
        hud.center();
        hud.add(gameNameLbl).colspan(3).pad(20);
        hud.row();
        hud.add(playBtn).pad(20);
        hud.add(inputBtn).pad(20);
        hud.add(tasksBtn).pad(20);
        hud.row();
        hud.add(byLbl).colspan(3).padTop(40);
        game.stage.addActor(hud);
    }

    public void update(float dt) {
        handleInput(dt);
        game.cam.update();
        game.renderer.setView(game.cam);//render only what cam shows up
    }

    private void handleInput(float dt) {
    }

    @Override
    public void render(float dt) {
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.renderer.render();
        game.batch.setProjectionMatrix(game.cam.combined);
        game.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void dispose() {
        game.renderer.dispose();
        game.stage.dispose();
        game.map.dispose();
    }
}
