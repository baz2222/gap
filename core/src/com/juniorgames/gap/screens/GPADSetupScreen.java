package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.juniorgames.gap.GapGame;

public class GPADSetupScreen extends ScreenAdapter {
    private GapGame game;
    private TextButton backBtn;
    private Table hud;

    public GPADSetupScreen(GapGame game) {
        this.game = game;
        backBtn = game.createTextMenuButton("BACK TO MENU", game.menuBtnTex, new MenuScreen(game));
        createHUD();
        Gdx.input.setInputProcessor(game.stage);
    }//constructor

    private void createHUD() {
        hud = new Table();
        hud.setFillParent(true);
        hud.center();
        hud.add(new Label("GamePad Connected!", game.labelStyle)).padLeft(90).padRight(90).padTop(20);
        hud.row();
        hud.add(backBtn).padTop(100);
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
        game.stage.dispose();
        game.renderer.dispose();
        game.map.dispose();
    }
}
