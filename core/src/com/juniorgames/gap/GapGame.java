package com.juniorgames.gap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.juniorgames.gap.screens.PlayScreen;

public class GapGame extends Game {
    //virtual screen width and height
    public static final int GAME_WIDTH = 960;
    public static final int GAME_HEIGHT = 544;
    public static final float GAME_PPM = 100; //pixels per meter for Box2D
    public SpriteBatch batch;
    @Override
    public void create() {
    batch = new SpriteBatch();
    setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
