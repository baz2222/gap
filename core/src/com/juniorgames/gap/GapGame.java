package com.juniorgames.gap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.juniorgames.gap.screens.MenuScreen;
import com.juniorgames.gap.tools.SavedGame;

public class GapGame extends Game {
    //virtual screen width and height
    public final int GAME_WIDTH = 960;
    public final int GAME_HEIGHT = 544;
    public final float GAME_PPM = 100; //pixels per meter for Box2D

    public final short DEFAULT_BIT = 1;
    public final short PLAYER_BIT = 2;//must be power of two for binary operations with fixtures filters
    public final short DOOR_BIT = 4;
    public final short DESTROYED_BIT = 8;

    public SavedGame savedGame;

    public boolean soundsMuted = false;//sound off
    public boolean musicMuted = true;//music off
    public boolean gamePaused = false;//game paused

    public static final int SPRITES_MULTIPLIER = 2; // multiplier for all sprites and textures - DEPENDS ON QUALITY AND SCREEN SIZE!

    //using asset manager in a stataic way can cause issues, especially on Android!!!
    public AssetManager manager;

    @Override
    public void create() {
        manager = new AssetManager();
        manager.load("audio/music/world1-music.mp3", Music.class);
        manager.load("audio/sounds/jump.mp3", Sound.class);
        manager.load("audio/sounds/exit.mp3", Sound.class);
        manager.load("audio/sounds/step.mp3", Sound.class);
        manager.load("audio/sounds/land.mp3", Sound.class);
        manager.load("fonts/big-font.fnt", BitmapFont.class);
        manager.load("fonts/mid-font.fnt", BitmapFont.class);
        manager.load("menu-btn.png", Texture.class);
        manager.load("play-menu-btn.png", Texture.class);
        manager.load("back-btn.png", Texture.class);
        manager.load("left-arrow-btn.png", Texture.class);
        manager.load("right-arrow-btn.png", Texture.class);
        manager.load("bg1.png", Texture.class);
        manager.load("bg2.png", Texture.class);
        manager.load("bg3.png", Texture.class);
        manager.finishLoading();

        savedGame = new SavedGame();

        //Gdx.graphics.setContinuousRendering(false);
        //Gdx.graphics.requestRendering();

        this.setScreen(new MenuScreen(this, manager));
    }//create()

    @Override
    public void dispose() {
        manager.dispose();
    }

    @Override
    public void render() {
        super.render();
        manager.update();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
