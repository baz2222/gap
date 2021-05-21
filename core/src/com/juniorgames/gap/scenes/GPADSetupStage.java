package com.juniorgames.gap.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.MenuScreen;

public class GPADSetupStage extends Stage {
    private GapGame game;
    private TextButton backBtn;
    private Table hud;

    public GPADSetupStage(GapGame game) {
        super(game.viewport, game.batch);
        this.game = game;
        Gdx.input.setInputProcessor(this);
        game.playMusic(0);
        backBtn = game.createTextMenuButton("BACK TO MENU", game.menuBtnTex, new MenuScreen(game));
        createHUD();
    }
    private void createHUD() {
        hud = new Table();
        hud.setFillParent(true);
        hud.add(new Label("Congratulations! You are finish the game!", game.lStyle)).padLeft(90).padRight(90).padTop(20);
        hud.row();
        hud.add(backBtn).padTop(100);
        addActor(hud);
    }
}
