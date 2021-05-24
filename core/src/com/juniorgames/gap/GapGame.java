package com.juniorgames.gap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.screens.GPADSetupScreen;
import com.juniorgames.gap.screens.GameOverScreen;
import com.juniorgames.gap.screens.MenuScreen;
import com.juniorgames.gap.sprites.*;
import com.juniorgames.gap.tools.*;

public class GapGame extends Game {
    public enum State {FALLING, JUMPING, STANDING, RUNNING_RIGHT, RUNNING_LEFT, DIEING}

    //virtual screen width and height
    public final int GAME_WIDTH = 960;
    public final int GAME_HEIGHT = 544;
    public final float GAME_PPM = 100; //pixels per meter for Box2D
    public final float VOLUME = 0.2f;

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
    public LevelData levelData = new LevelData();

    public int selectedWorld;
    public int selectedLevel;

    public Rectangle bounds = new Rectangle();

    public Sound sound;
    public Music music;

    public boolean muted = false;//sound off
    public boolean gamePaused = false;//game paused

    public AssetManager manager;
    public Task currentTask;
    public TmxMapLoader loader;
    public Controller controller;

    public Viewport viewport;
    public Batch batch;
    public OrthographicCamera cam;
    public TiledMap map;
    public OrthogonalTiledMapRenderer renderer;
    public Stage stage;

    public Player player;
    public Door door;
    public Trail playerTrail;
    public Array<Enemy> enemies = new Array<>();
    public Array<SpikeEnemy> spikeEnemies = new Array<>();
    public Array<Switch> switches = new Array<>();
    public Array<Bump> bumps = new Array<>();
    public Array<Buff> buffs = new Array<>();

    public BitmapFont midFont, bigFont;
    public Texture menuBtnTex, playMenuBtnTex, backBtnTex;
    public Label.LabelStyle labelStyle, bigLabelStyle;
    public InputListener listener;

    @Override
    public void create() {
        loader = new TmxMapLoader();
        savedGame = new SavedGame();
        savedGame.load();
        tasksTracker = new TasksTracker(this);
        tasksTracker.update(savedGame);
        currentTask = null;

        manager = new AssetManager();
        for (Task task : tasksTracker.tasks) {
            manager.load(task.taskImagePath, Texture.class);
        }//for
        manager.load("sounds/world1.mp3", Music.class);
        manager.load("sounds/world2.mp3", Music.class);
        manager.load("sounds/world3.mp3", Music.class);
        manager.load("sounds/menu.mp3", Music.class);
        manager.load("sounds/jump.mp3", Sound.class);
        manager.load("sounds/exit.mp3", Sound.class);
        manager.load("sounds/warp.mp3", Sound.class);
        manager.load("sounds/step.mp3", Sound.class);
        manager.load("sounds/land.mp3", Sound.class);
        manager.load("sounds/die.mp3", Sound.class);
        manager.load("sounds/break.mp3", Sound.class);
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

        midFont = manager.get("fonts/mid-font.fnt", BitmapFont.class);
        bigFont = manager.get("fonts/big-font.fnt", BitmapFont.class);

        menuBtnTex = manager.get("menu-btn.png", Texture.class);
        playMenuBtnTex = manager.get("play-menu-btn.png", Texture.class);
        backBtnTex = manager.get("back-btn.png", Texture.class);

        labelStyle = new Label.LabelStyle(midFont, Color.WHITE);
        bigLabelStyle = new Label.LabelStyle(bigFont, Color.WHITE);

        cam = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, cam);
        batch = new SpriteBatch();
        map = loader.load("level0-0.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        stage = new Stage(viewport, batch);

        loadAtlases();
        createBox2DWorld();
        detectGamePAD();
    }//create()

    private void loadAtlases(){
        playerAtlas = new TextureAtlas("player.pack");
        enemyAtlas = new TextureAtlas("enemy.pack");
        spikeEnemyAtlas = new TextureAtlas("senemy.pack");
        doorAtlas = new TextureAtlas("door.pack");
        switchAtlas = new TextureAtlas("switch.pack");
        bumpAtlas = new TextureAtlas("bump.pack");
        buffAtlas = new TextureAtlas("buffs.pack");
    }

    private void createBox2DWorld() {
        world = new World(new Vector2(0, -20), true);
        contactListener = new WorldContactListener(this);
        world.setContactListener(contactListener);
        new Box2DDebugRenderer();
    }

    private void detectGamePAD() {
        if (Controllers.getControllers().size != 0) {
            controller = Controllers.getControllers().first();
            this.setScreen(new GPADSetupScreen(this));
        } else {
            this.setScreen(new MenuScreen(this));
        }
    }

    public void gameOver() {
        savedGame.reset();
        this.setScreen(new GameOverScreen(this));
    }

    public TextButton createTextMenuButton(String text, Texture texture, ScreenAdapter screen) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = midFont;
        style.down = new TextureRegionDrawable(texture);
        style.up = new TextureRegionDrawable(texture);
        style.checked = new TextureRegionDrawable(texture);
        TextButton button = new TextButton(text, style);
        button.addListener( new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setScreen(screen);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });//listener
        return button;
    }

    public void playSound(String soundName, boolean looping) {
        sound = manager.get("sounds/" + soundName + ".mp3", Sound.class);
        sound.setLooping(sound.play(VOLUME), looping);
    }

    public void playBackgroundMusic(String musicName, boolean looping) {
        music = manager.get("sounds/" + musicName + ".mp3", Music.class);
        music.setLooping(looping);
        music.setVolume(VOLUME);
        music.play();
    }

    public void stopBackgroundMusic() {
        if (music != null)
            music.stop();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
