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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.LevelHUD;
import com.juniorgames.gap.sprites.Player;
import com.juniorgames.gap.sprites.Player.State;
import com.juniorgames.gap.tools.B2WorldCreator;
import com.juniorgames.gap.tools.LevelData;
import com.juniorgames.gap.tools.WorldContactListener;

import static com.juniorgames.gap.GapGame.GAME_PPM;

public class LevelScreen extends ScreenAdapter {
    private GapGame game;
    private AssetManager manager;
    private SpriteBatch batch;

    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private Viewport viewport;
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

        //define values by default when LevelScreen instance created
        currentWorld = 0;
        currentLevel = 0;

        atlas = new TextureAtlas("player.pack");
        camera = new OrthographicCamera();
        viewport = new FitViewport(this.game.GAME_WIDTH / GAME_PPM, this.game.GAME_HEIGHT / GAME_PPM, camera);
        levelHud = new LevelHUD(this.game, this.manager);

        maploader = new TmxMapLoader();
        map = maploader.load("level0-0.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / GAME_PPM);//scaling map with PPM

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map);
        player = new Player(world, this);
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

        currentLevelData = loadLevel(currentWorld, currentLevel);
    }//constructor

    public LevelData loadLevel(int world, int level) {
        LevelData data = new LevelData();
        JsonReader reader = new JsonReader();
        JsonValue value;
        JsonValue json = reader.parse(Gdx.files.internal("level" + world + "-" + level + ".json"));

        //int
        data.world = json.getInt("world");
        data.level = json.getInt("level");

        //Vector2
        value = json.getChild("start");
        data.start.x = value.asFloat();
        data.start.y = value.next.asFloat();
        value = json.getChild("exit");
        data.exit.x = value.asFloat();
        data.exit.y = value.next.asFloat();
        value = json.getChild("tutorial");
        data.tutorial.x = value.asFloat();
        data.tutorial.y = value.next.asFloat();

        //Array<Vector2> plant1s
        value = json.getChild("plant1s");
        while (value != null) {
            data.plant1s.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> plant2s
        value = json.getChild("plant2s");
        while (value != null) {
            data.plant2s.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> switches
        value = json.getChild("switches");
        while (value != null) {
            data.switches.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> enemies
        value = json.getChild("enemies");
        while (value != null) {
            data.enemies.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> spikeEnemies
        value = json.getChild("spikeEnemies");
        while (value != null) {
            data.spikeEnemies.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> bumps
        value = json.getChild("bumps");
        while (value != null) {
            data.bumps.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> buffBombs
        value = json.getChild("buffBombs");
        while (value != null) {
            data.buffBombs.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> buffJumps
        value = json.getChild("buffJumps");
        while (value != null) {
            data.buffJumps.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        //Array<Vector2> buffShields
        value = json.getChild("buffShields");
        while (value != null) {
            data.buffShields.add(new Vector2(value.get("x").asFloat(), value.get("y").asFloat()));
            value = value.next;
        }//end while

        return data;
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);//60 times per second
        //camera.position.x = player.b2body.getPosition().x; //move camera with the character
        player.update(dt);
        levelHud.update(dt);
        camera.update();
        renderer.setView(camera);
    }

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
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        Gdx.app.log("LevelScreen", "Resizing screen to: " + width + " x " + height);
    }

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
