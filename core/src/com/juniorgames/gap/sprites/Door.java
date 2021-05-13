package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.LevelScreen;

public class Door extends Sprite {
    private GapGame game;
    private AssetManager manager;
    private Array<TextureRegion> doorFramesRegion;
    private Animation doorAnimation;

    private BodyDef bdef;
    public Body b2body;
    private FixtureDef fdef;
    private PolygonShape doorShape;
    private PolygonShape sensor;
    private Fixture fixture, sensorFixture;
    private Filter filter;

    private float stateTimer = 0;
    public boolean isVisible = false;

    public Door(GapGame game) {
        super(game.doorAtlas.findRegion("door"));
        this.game = game;
        this.manager = game.manager;
        filter = new Filter();
        defineDoor();
        doorFramesRegion = new Array<>();
        for (int i = 0; i < 8; i++) {
            doorFramesRegion.add(new TextureRegion(getTexture(), i * 64, 0, 64, 96));
        }//for
        doorAnimation = new Animation(0.08f, doorFramesRegion);
        doorFramesRegion.clear();

        setBounds(0, 0, 64 / game.GAME_PPM, 96 / game.GAME_PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }//constructor

    public void update(float dt) {
        if (isVisible) {
            setRegion(getFrame(dt));
            if (!b2body.isActive()){
                b2body.setActive(true);
            }//inner if
        }else{
            if (b2body.isActive()){
                b2body.setActive(false);
            }//inner if
        }
    }

    public TextureRegion getFrame(float dt) {
        stateTimer = stateTimer + dt;
        return new TextureRegion((TextureRegion) doorAnimation.getKeyFrame(stateTimer, true));
    }

    private void defineDoor() {
        bdef = new BodyDef();
        bdef.position.set(game.levelData.exit.x / game.GAME_PPM, game.levelData.exit.y / game.GAME_PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = game.world.createBody(bdef);

        doorShape = new PolygonShape();
        doorShape.setAsBox(20 / game.GAME_PPM, 45 / game.GAME_PPM);
        fdef = new FixtureDef();
        fdef.shape = doorShape;
        fdef.isSensor = true;
        fdef.filter.maskBits = (short) (game.PLAYER_BIT);//with what fixtures door can collide with
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        setFilter(game.DOOR_BIT);
    }

    public void onHit() {
        setFilter(game.DESTROYED_BIT);
        game.playSound(game.exitSound);
        if (game.savedGame.level == 10) {
            if (game.savedGame.world == 3) {
                game.stopMusic();
                game.gameOver();
                return;
            } else {
                game.savedGame.world++;
            }//else
            game.savedGame.level = 1;
        } else {
            game.savedGame.level++;
            game.savedGame.completed++;
        }//else
        game.savedGame.save();
        game.tasksTracker.update(game.savedGame);
        game.stopMusic();
        game.setScreen(new LevelScreen(game, manager));
    }

    public void setFilter(short bit) {
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }
}
