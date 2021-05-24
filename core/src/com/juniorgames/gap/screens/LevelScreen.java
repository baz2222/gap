package com.juniorgames.gap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.scenes.LevelHUD;
import com.juniorgames.gap.sprites.*;

public class LevelScreen extends ScreenAdapter {
    private GapGame game;
    private LevelHUD levelHud;
    private Box2DDebugRenderer b2dr;

    private float stepTime;//to make delay for stepping sounds
    private float trailTime;//to make delay for player trail

    public LevelScreen(GapGame game, boolean newGame) {
        if (newGame) {
            game.savedGame.reset();
            game.tasksTracker.reset();
        }
        this.game = game;
        game.savedGame.load();
        game.levelData.loadLevel(game.savedGame.world, game.savedGame.level);
        game.map = game.loader.load("level" + game.savedGame.world + "-" + game.savedGame.level + ".tmx");

        levelHud = new LevelHUD(this.game);
        if (game.levelData.tutorial != "") {
            levelHud.showTutorial(game.levelData.tutorial);
        }//if

        //===================================================================
        for (MapObject object : game.map.getLayers().get("GroundObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new Ground(game);
        }//for
        //===================================================================
        for (MapObject object : game.map.getLayers().get("CrumbleObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new Crumbles(game);
        }//for
        //===================================================================
        for (MapObject object : game.map.getLayers().get("SpikesObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new Spikes(game);
        }//for
        //===================================================================
        for (MapObject object : game.map.getLayers().get("ChangeEnemyDirectionObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new ChangeDirectionBox(game);
        }//for
        //===================================================================
        game.door = new Door(game);
        game.player = new Player(game, game.levelData.start.x, game.levelData.start.y);
        for (Vector2 v : game.levelData.enemies) {
            game.enemies.add(new Enemy(game, v.x, v.y));
        }//for
        for (Vector2 v : game.levelData.spikeEnemies) {
            game.spikeEnemies.add(new SpikeEnemy(game, v.x, v.y));
        }//for
        for (Vector2 v : game.levelData.bumps) {
            game.bumps.add(new Bump(game, v.x, v.y, game.player));
        }//for
        for (Vector2 v : game.levelData.switches) {
            game.switches.add(new Switch(game, v.x, v.y, game.door));
        }//for
        for (Vector2 buffBomb : game.levelData.buffBombs) {
            game.buffs.add(new Buff(game, buffBomb.x, buffBomb.y, Buff.BuffType.BOMB));
        }//for
        for (Vector2 buffJump : game.levelData.buffJumps) {
            game.buffs.add(new Buff(game, buffJump.x, buffJump.y, Buff.BuffType.JUMP));
        }//for
        for (Vector2 buffShield : game.levelData.buffShields) {
            game.buffs.add(new Buff(game, buffShield.x, buffShield.y, Buff.BuffType.SHIELD));
        }//for

        if (game.switches.size == 0) {
            game.door.isVisible = true;
        }// if no switches door is visible at start

        game.stopBackgroundMusic();
        game.playBackgroundMusic("world" + game.savedGame.world, true);

    }//constructor

    public void update(float dt) {
        handleInput(dt);
        //camera.position.x = player.b2body.getPosition().x; //move camera with the character
        game.world.step(1 / 60f, 6, 4);//60 times per second 60 6 4
        game.door.update(dt);
        game.player.update(dt);
        if (game.player.buff != null && trailTime >= 0.2) {
            game.playerTrail = null;
            game.playerTrail = new Trail(game);
            trailTime = 0;
        }//if
        if (game.playerTrail != null) {
            game.playerTrail.update(dt);
        }//if
        for (Enemy enemy : game.enemies) {
            enemy.update(dt);
        }
        for (SpikeEnemy enemy : game.spikeEnemies) {
            enemy.update(dt);
        }
        for (Bump bump : game.bumps) {
            bump.update(dt);
        }
        for (Buff buff : game.buffs) {
            buff.update(dt);
        }
        for (Switch sw : game.switches) {// sw = switch
            sw.update(dt);
        }
        levelHud.update(dt);
        if (game.currentTask != null) {
            levelHud.showTask(game.currentTask);
            game.currentTask = null;
        }
        game.cam.update();
        game.renderer.setView(game.cam);
    }//update

    private void handleInput(float dt) {
        //jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || (Gdx.input.isTouched() && Gdx.input.getDeltaY() < -10)) {
            game.player.jump();
        }
        //move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && Gdx.input.getDeltaX() > 10)) {
            game.player.runRight();
        }
        //move left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && Gdx.input.getDeltaX() < -10)) {
            game.player.runLeft();
        }
    }//handleInput

    @Override
    public void render(float dt) {
        if (!game.gamePaused) {
            update(dt);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.renderer.render();
            b2dr.render(game.world, game.cam.combined);

            game.batch.setProjectionMatrix(game.cam.combined);
            game.batch.begin();
            if (game.door.isVisible) {
                game.door.draw(game.batch);
            }
            for (Enemy enemy : game.enemies) {
                if (enemy.isVisible)
                    enemy.draw(game.batch);
            }//for
            for (SpikeEnemy enemy : game.spikeEnemies) {
                if (enemy.isVisible)
                    enemy.draw(game.batch);
            }//for
            for (Switch sw : game.switches) {
                sw.draw(game.batch);
            }//for
            for (Bump bump : game.bumps) {
                bump.draw(game.batch);
            }//for
            for (Buff buff : game.buffs) {
                if (buff.isVisible) {
                    buff.draw(game.batch);
                }//if
            }//for
            if (game.playerTrail != null) {
                game.playerTrail.draw(game.batch);
            }
            game.player.draw(game.batch);
            game.batch.end();

            game.batch.setProjectionMatrix(levelHud.stage.getCamera().combined);
            levelHud.stage.draw();

            stepTime += dt;
            trailTime += dt;
            if (stepTime >= 0.3 && (game.player.getState() == GapGame.State.RUNNING_LEFT || game.player.getState() == GapGame.State.RUNNING_RIGHT)) {
                game.playSound("step", false);
                stepTime = 0;
            }//end if
        } else {//if game paused
            levelHud.stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void dispose() {
        b2dr.dispose();
        levelHud.dispose();
        game.world.dispose();
    }
}
