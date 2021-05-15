package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.LevelHUD;
import com.juniorgames.gap.sprites.*;
import com.juniorgames.gap.sprites.Player.State;
import com.juniorgames.gap.tools.LevelData;
import com.juniorgames.gap.tools.WorldContactListener;

public class LevelScreen extends ScreenAdapter {
    private GapGame game;
    private AssetManager manager;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;

    private LevelHUD levelHud;

    private Box2DDebugRenderer b2dr;
    //sprites
    public Player player;
    public Trail playerTrail;
    public Door door;
    public Array<Enemy> enemies;
    public Array<SpikeEnemy> spikeEnemies;
    public Array<Switch> switches;
    public Array<Bump> bumps;
    public Array<Buff> buffs;
    //sfx
    private float stepTime;//to make delay for stepping sounds
    private float trailTime;//to make delay for stepping sounds

    public LevelScreen(GapGame game, AssetManager manager) {
        this.game = game;
        this.manager = manager;
        game.savedGame.load();
        batch = new SpriteBatch();
        enemies = new Array<>();
        spikeEnemies = new Array<>();
        switches = new Array<>();
        bumps = new Array<>();
        buffs = new Array<>();

        camera = new OrthographicCamera();
        viewport = new FitViewport(game.GAME_WIDTH / game.GAME_PPM, game.GAME_HEIGHT / game.GAME_PPM, camera);

        game.levelData = new LevelData();
        game.levelData.loadLevel(game.savedGame.world, game.savedGame.level);

        game.maploader = new TmxMapLoader();
        game.bounds = new Rectangle();
        game.platformMap = game.maploader.load("level" + game.savedGame.world + "-" + game.savedGame.level + ".tmx");
        game.renderer = new OrthogonalTiledMapRenderer(game.platformMap, 1 / game.GAME_PPM);//scaling map with PPM

        levelHud = new LevelHUD(this.game, this.manager);
        if (game.levelData.tutorial != "") {
            levelHud.showTutorial(game.levelData.tutorial);
        }//if

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        game.playerAtlas = new TextureAtlas("player.pack");
        game.enemyAtlas = new TextureAtlas("enemy.pack");
        game.spikeEnemyAtlas = new TextureAtlas("senemy.pack");
        game.doorAtlas = new TextureAtlas("door.pack");
        game.switchAtlas = new TextureAtlas("switch.pack");
        game.bumpAtlas = new TextureAtlas("bump.pack");
        game.buffAtlas = new TextureAtlas("buffs.pack");

        game.world = new World(new Vector2(0, -10), true);

        game.contactListener = new WorldContactListener(game);
        game.world.setContactListener(game.contactListener);
        //===================================================================
        b2dr = new Box2DDebugRenderer();
        //===================================================================
        for (MapObject object : game.platformMap.getLayers().get("GroundObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new Ground(game);
        }//for
        //===================================================================
        for (MapObject object : game.platformMap.getLayers().get("CrumbleObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new Crumbles(game);
        }//for
        //===================================================================
        for (MapObject object : game.platformMap.getLayers().get("SpikesObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new Spikes(game);
        }//for
        //===================================================================
        for (MapObject object : game.platformMap.getLayers().get("ChangeEnemyDirectionObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new ChangeDirectionBox(game);
        }//for
        //===================================================================
        door = new Door(game);
        player = new Player(game, game.levelData.start.x, game.levelData.start.y);
        for (Vector2 v : game.levelData.enemies) {
            enemies.add(new Enemy(game, v.x, v.y));
        }//for
        for (Vector2 v : game.levelData.spikeEnemies) {
            spikeEnemies.add(new SpikeEnemy(game, v.x, v.y));
        }//for
        for (Vector2 v : game.levelData.bumps) {
            bumps.add(new Bump(game, v.x, v.y, player));
        }//for
        for (Vector2 v : game.levelData.switches) {
            switches.add(new Switch(game, v.x, v.y, door));
        }//for
        for (Vector2 buffBomb : game.levelData.buffBombs) {
            buffs.add(new Buff(game, buffBomb.x, buffBomb.y, Buff.BuffType.BOMB));
        }//for
        for (Vector2 buffJump : game.levelData.buffJumps) {
            buffs.add(new Buff(game, buffJump.x, buffJump.y, Buff.BuffType.JUMP));
        }//for
        for (Vector2 buffShield : game.levelData.buffShields) {
            buffs.add(new Buff(game, buffShield.x, buffShield.y, Buff.BuffType.SHIELD));
        }//for
        if (switches.size == 0) {
            door.isVisible = true;
        }// if no switches door is visible at start

        game.playMusic(game.savedGame.world);

    }//constructor

    public void update(float dt) {
        handleInput(dt);
        //camera.position.x = player.b2body.getPosition().x; //move camera with the character
        game.world.step(1 / 60f, 6, 4);//60 times per second 60 6 4
        door.update(dt);
        player.update(dt);
        if (player.buff != null && trailTime >= 0.2) {
            playerTrail = null;
            playerTrail = new Trail(game, player);
            trailTime = 0;
        }//if
        if (playerTrail!=null){
            playerTrail.update(dt);
        }//if
        for (Enemy enemy : enemies) {
            enemy.update(dt);
        }
        for (SpikeEnemy enemy : spikeEnemies) {
            enemy.update(dt);
        }
        for (Bump bump : bumps) {
            bump.update(dt);
        }
        for (Buff buff : buffs) {
            buff.update(dt);
        }
        for (Switch sw : switches) {// sw = switch
            sw.update(dt);
        }
        levelHud.update(dt);
        if (game.currentTask != null) {
            levelHud.showTask(game.currentTask);
            game.currentTask = null;
        }
        camera.update();
        game.renderer.setView(camera);
    }//update

    private void handleInput(float dt) {
        //jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || (Gdx.input.isTouched() && Gdx.input.getDeltaY() < -20 && Math.abs(Gdx.input.getDeltaX()) < 30)) {
            player.jump();
        }
        //move right
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && Gdx.input.getDeltaX() > 0)) && player.b2body.getLinearVelocity().x <= 2) {
            player.moveRight();
        }
        //move left
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && Gdx.input.getDeltaX() < 0)) && player.b2body.getLinearVelocity().x >= -2) {
            player.moveLeft();
        }
    }//handleInput

    @Override
    public void render(float delta) {
        if (!game.gamePaused) {
            update(delta);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //render game map
            game.renderer.render();
            //render Box2DDebugLines
            b2dr.render(game.world, camera.combined);

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            if (door.isVisible) {
                door.draw(batch); //begin draw
            }
            for (Enemy enemy : enemies) {
                enemy.draw(batch);
            }//for
            for (SpikeEnemy enemy : spikeEnemies) {
                enemy.draw(batch);
            }//for
            for (Switch sw : switches) {
                sw.draw(batch);
            }//for
            for (Bump bump : bumps) {
                bump.draw(batch);
            }//for
            for (Buff buff : buffs) {
                if(buff.isVisible) {
                    buff.draw(batch);
                }//if
            }//for
            if (playerTrail!=null) {
                playerTrail.draw(batch);
            }
            player.draw(batch);
            batch.end(); //end draw

            batch.setProjectionMatrix(levelHud.stage.getCamera().combined);
            levelHud.stage.draw();

            stepTime += delta;
            trailTime += delta;
            if (stepTime >= 0.3 && player.getState() == State.RUNNING && !game.soundsMuted) {
                game.playSound(game.stepSound);
                stepTime = 0;
            }//end if
        } else {//if game paused
            levelHud.stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }//resize

    @Override
    public void dispose() {
        b2dr.dispose();
        levelHud.dispose();
    }
}
