package com.juniorgames.gap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.juniorgames.gap.screens.GameOverScreen;
import com.juniorgames.gap.screens.MenuScreen;
import com.juniorgames.gap.tools.*;

public class GapGame extends Game {
    //virtual screen width and height
    public final int GAME_WIDTH = 960;
    public final int GAME_HEIGHT = 544;
    public final float GAME_PPM = 100; //pixels per meter for Box2D
    public final float MUSIC_VOLUME = 0.2f;
    public final float SOUND_VOLUME = 0.2f;

    public final short DEFAULT_BIT = 1;
    public final short PLAYER_BIT = 2;//must be power of two for binary operations with fixtures filters
    public final short DOOR_BIT = 4;
    public final short DESTROYED_BIT = 8;
    public final short GROUND_BIT = 16;
    public final short CRUMBLES_BIT = 512;
    public final short SPIKES_BIT = 32;
    public final short ENEMY_BIT = 64;
    public final short CHANGE_DIRECTION_BOX_BIT = 128;
    public final short SPIKE_ENEMY_BIT = 256;
    public final short SWITCH_BIT = 1024;
    public final short BUMP_BIT = 2048;
    public final short BUFF_BIT = 4096;

    public TextureAtlas spikeEnemyAtlas;
    public TextureAtlas playerAtlas;
    public TextureAtlas enemyAtlas;
    public TextureAtlas doorAtlas;
    public TextureAtlas switchAtlas;
    public TextureAtlas bumpAtlas;
    public TextureAtlas buffAtlas;

    public World world;
    public WorldContactListener contactListener;

    public SavedGame savedGame;
    public TasksTracker tasksTracker;
    public LevelData levelData;

    public int selectedWorld;
    public int selectedLevel;

    public TmxMapLoader maploader;
    public TiledMap platformMap;
    public OrthogonalTiledMapRenderer renderer;
    public Rectangle bounds;

    private Music music;
    public Sound jumpSound;
    public Sound stepSound;
    public Sound warpSound;
    public Sound exitSound;
    public Sound dieSound;

    public boolean soundsMuted = false;//sound off
    public boolean musicMuted = false;//music off
    public boolean gamePaused = false;//game paused

    public AssetManager manager;
    public Task currentTask;

    @Override
    public void create() {
        savedGame = new SavedGame();
        savedGame.load();
        tasksTracker = new TasksTracker(this);
        tasksTracker.update(savedGame);
        currentTask = null;

        manager = new AssetManager();
        for (Task task : tasksTracker.tasks) {
            manager.load(task.taskImagePath, Texture.class);
        }//for
        manager.load("audio/music/world1-music.mp3", Music.class);
        manager.load("audio/music/world2-music.mp3", Music.class);
        manager.load("audio/music/world3-music.mp3", Music.class);
        manager.load("audio/music/menu-music.mp3", Music.class);
        manager.load("audio/sounds/jump.mp3", Sound.class);
        manager.load("audio/sounds/exit.mp3", Sound.class);
        manager.load("audio/sounds/warp.mp3", Sound.class);
        manager.load("audio/sounds/step.mp3", Sound.class);
        manager.load("audio/sounds/land.mp3", Sound.class);
        manager.load("audio/sounds/die.mp3", Sound.class);
        manager.load("fonts/big-font.fnt", BitmapFont.class);
        manager.load("fonts/mid-font.fnt", BitmapFont.class);
        manager.load("menu-btn.png", Texture.class);
        manager.load("play-menu-btn.png", Texture.class);
        manager.load("select-world-btn.png", Texture.class);
        manager.load("select-level-btn.png", Texture.class);
        manager.load("back-btn.png", Texture.class);
        manager.load("left-arrow-btn.png", Texture.class);
        manager.load("right-arrow-btn.png", Texture.class);
        manager.load("bg1.png", Texture.class);
        manager.load("bg2.png", Texture.class);
        manager.load("bg3.png", Texture.class);
        for (int i = 0; i < 12; i++) {
            manager.load(tasksTracker.tasks.get(i).taskStripImagePath, Texture.class);
            manager.load(tasksTracker.tasks.get(i).taskImagePath, Texture.class);
        }
        manager.finishLoading();

        jumpSound = manager.get("audio/sounds/jump.mp3", Sound.class);
        stepSound = manager.get("audio/sounds/step.mp3", Sound.class);
        warpSound = manager.get("audio/sounds/warp.mp3", Sound.class);
        exitSound = manager.get("audio/sounds/exit.mp3", Sound.class);
        dieSound = manager.get("audio/sounds/die.mp3", Sound.class);
        playMusic(0);

        this.setScreen(new MenuScreen(this, manager));
    }//create()

    public void gameOver() {
        savedGame.reset();
        this.setScreen(new GameOverScreen(this, manager));
    }

    public void playSound(Sound sound) {
        if (!soundsMuted) {
            sound.setLooping(sound.play(SOUND_VOLUME), false);
        }//if
    }

    public void playMusic(int world) {
        if (world > 0) {
            music = manager.get("audio/music/world" + world + "-music.mp3", Music.class);
        } else {
            music = manager.get("audio/music/menu-music.mp3", Music.class);
        }
        music.setLooping(true);
        music.setVolume(MUSIC_VOLUME);//0-1 range
        if (!musicMuted) {
            music.play();
        }//end if
    }

    public void stopMusic() {
        music.stop();
    }

    @Override
    public void dispose() {
        music.dispose();
        world.dispose();
        manager.dispose();
        platformMap.dispose();
        renderer.dispose();
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
