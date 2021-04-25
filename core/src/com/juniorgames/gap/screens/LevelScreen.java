package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.LevelHUD;
import com.juniorgames.gap.sprites.Player;
import com.juniorgames.gap.sprites.Player.State;
import com.juniorgames.gap.tools.B2WorldCreator;
import com.juniorgames.gap.tools.LevelData;
import com.juniorgames.gap.tools.WorldContactListener;

public class LevelScreen extends ScreenAdapter {
    private GapGame game;
    private AssetManager manager;
    private SpriteBatch batch;

    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private LevelHUD levelHud;

    //tiled map values
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //box2d values
    private World world;
    private Box2DDebugRenderer b2dr;
    //sprites
    private Player player;
    //sfx
    private Music music;
    private Sound jumpSound;
    private Sound stepSound;
    private float timeCount;//to make delay for stepping sounds

    //move this values to GAME STATE CLASS!!!!!!
    private int currentWorld;
    private int currentLevel;
    private LevelData currentLevelData;

    public LevelScreen(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        batch = new SpriteBatch();
        stage = new Stage();

        //define values by default when LevelScreen instance created
        this.currentWorld = game.savedGame.world;
        this.currentLevel = game.savedGame.level;

        atlas = new TextureAtlas("player.pack");
        camera = new OrthographicCamera();
        viewport = new FitViewport(game.GAME_WIDTH / game.GAME_PPM, game.GAME_HEIGHT / game.GAME_PPM, camera);

        levelHud = new LevelHUD(this.game, this.manager, currentWorld, currentLevel);

        maploader = new TmxMapLoader();
        map = maploader.load("level" + currentWorld + "-" + currentLevel + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / game.GAME_PPM);//scaling map with PPM

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map, game);
        player = new Player(world, this, game);
        world.setContactListener(new WorldContactListener());

        //music and sounds
        music = manager.get("audio/music/world1-music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);//0-1 range
        if (!game.musicMuted) {
            music.play();
        }//end if
        jumpSound = manager.get("audio/sounds/jump.mp3", Sound.class);
        stepSound = manager.get("audio/sounds/step.mp3", Sound.class);

        currentLevelData = new LevelData();
        currentLevelData.loadLevel(currentWorld, currentLevel);
    }//constructor

    public void update(float dt) {
        handleInput(dt);
        //camera.position.x = player.b2body.getPosition().x; //move camera with the character
        world.step(1 / 60f, 6, 4);//60 times per second 60 6 4
        player.update(dt);
        levelHud.update(dt);
        camera.update();
        renderer.setView(camera);
    }//update

    public TextureAtlas getAtlas() {
        return atlas;
    }

    private void handleInput(float dt) {
        //jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if (!game.soundsMuted) {
                jumpSound.setLooping(jumpSound.play(), false);
            }
        }
        //move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);
        }
        //move left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    @Override
    public void render(float delta) {
        if (!game.gamePaused) {
            update(delta);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //render game map
            renderer.render();
            //render Box2DDebugLines
            b2dr.render(world, camera.combined);

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            player.draw(batch);
            batch.end();

            batch.setProjectionMatrix(levelHud.stage.getCamera().combined);
            levelHud.stage.draw();

            timeCount += delta;
            if (timeCount >= 0.3 && player.getSate() == State.RUNNING && !game.soundsMuted) {
                stepSound.setLooping(stepSound.play(), false);
                timeCount = 0;
            }//end if
        } else {//if game paused
            levelHud.stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
            viewport.update(width, height);
            levelHud.resize(width, height);
    }//resize

    @Override
    public void dispose() {
        map.dispose();
        b2dr.dispose();
        renderer.dispose();
        world.dispose();
        //dispose sounds and music
        music.dispose();
        jumpSound.dispose();
        stepSound.dispose();
        levelHud.dispose();
    }
}
