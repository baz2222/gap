package com.juniorgames.gap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.juniorgames.gap.screens.MenuScreen;

public class GapGame extends Game {
    //virtual screen width and height
    public static final int GAME_WIDTH = 960;
    public static final int GAME_HEIGHT = 544;
    public static final float GAME_PPM = 100; //pixels per meter for Box2D

    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;//must be power of to for binary operations with fixtures filters
    public static final short DOOR_BIT = 4;
    public static final short DESTROYED_BIT = 8;

    public boolean soundsMuted = false;//sound off
    public boolean musicMuted = true;//music off

    public static final int SPRITES_MULTIPLIER = 2; // multiplier for all sprites and textures - DEPENDS ON QUALITY AND SCREEN SIZE!

    public SpriteBatch batch;

    //using asset manager in a stataic way can cause issues, especially on Android!!!
    public AssetManager manager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("audio/music/world1-music.mp3", Music.class);
        manager.load("audio/sounds/jump.mp3", Sound.class);
        manager.load("audio/sounds/exit.mp3", Sound.class);
        manager.load("audio/sounds/step.mp3", Sound.class);
        manager.load("audio/sounds/land.mp3", Sound.class);
        manager.load("fonts/pacifico-regular.fnt", BitmapFont.class);
        manager.load("menu-btn.png", Texture.class);
        manager.finishLoading();

        setScreen(new MenuScreen(this, manager));
    }//create()


    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }

    @Override
    public void render() {
        super.render();
        manager.update();
    }
}
