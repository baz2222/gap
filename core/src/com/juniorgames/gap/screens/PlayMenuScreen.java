package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.juniorgames.gap.GapGame;

public class PlayMenuScreen extends ScreenAdapter {
    private GapGame game;
    private Table hud;
    private TextButton newBtn, selectBtn, loadBtn, backBtn;

    public PlayMenuScreen(GapGame game) {
        this.game = game;
        game.stage = new Stage(game.viewport, game.batch);
        //newBtn = game.createTextMenuButton("NEW GAME", game.menuBtnTex, new LevelScreen(game, true));
        //selectBtn = game.createTextMenuButton("  SELECT LEVEL  ", game.menuBtnTex, new SelectWorldMenuScreen(game));
        //loadBtn = game.createTextMenuButton("CONTINUE", game.menuBtnTex, new LevelScreen(game, false));
        //backBtn = game.createTextMenuButton("BACK", game.backBtnTex, new MenuScreen(game));
        createHUD();
        Gdx.input.setInputProcessor(game.stage);
    }//constructor

    private void createHUD() {
        hud = new Table();
        hud.bottom();
        hud.setFillParent(true);
        hud.add(newBtn).pad(20).padBottom(160).padLeft(115);
        hud.add(selectBtn).pad(20).padBottom(160);
        hud.add(loadBtn).pad(20).padBottom(160).padRight(115);
        hud.row();
        hud.add(backBtn).colspan(3).right();
        game.stage.addActor(hud);
    }

    public void update(float dt) {
        handleInput(dt);
        game.cam.update();
        game.renderer.setView(game.cam);
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
        game.map.dispose();
        game.renderer.dispose();
        game.stage.dispose();
    }
}
