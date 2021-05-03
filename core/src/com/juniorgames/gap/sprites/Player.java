package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.GapGame;

public class Player extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DIEING}

    public State previousState;
    public State currentState;
    public Body b2body;
    private Filter filter;
    private Fixture fixture, sensorFixture;
    private Animation playerIdle;
    private float stateTimer;
    private boolean runningRight;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerFall;
    private GapGame game;
    private AssetManager manager;
    private BodyDef bdef;

    public Player(GapGame game) {
        super(game.playerAtlas.findRegion("player"));
        this.game = game;
        this.manager = game.manager;
        filter = new Filter();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<>();
        //run animation
        for (int i = 0; i < 13; i++) {
            frames.add(new TextureRegion(getTexture(), i * 64, 0, 64, 64));
        }//for
        playerRun = new Animation(0.05f, frames);
        frames.clear();
        //jump animation
        frames.add(new TextureRegion(getTexture(), 14 * 64, 0, 64, 64));
        playerJump = new Animation(0.1f, frames);
        frames.clear();
        //fall animation
        frames.add(new TextureRegion(getTexture(), 13 * 64, 0, 64, 64));
        playerFall = new Animation(0.1f, frames);
        frames.clear();
        //idle animation
        for (int i = 0; i < 13; i++) {
            frames.add(new TextureRegion(getTexture(), i * 64, 64, 64, 64));
        }//for
        playerIdle = new Animation(0.1f, frames);
        frames.clear();

        definePlayer();
        setBounds(0, 0, 64 / game.GAME_PPM, 64 / game.GAME_PPM);
        setRegion((TextureRegion) playerIdle.getKeyFrame(stateTimer, true));

    }//constructor

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        //=======================WRAP===========================
        if (b2body.getPosition().x * game.GAME_PPM < 0) {
            b2body.setTransform((b2body.getPosition().x * game.GAME_PPM + game.GAME_WIDTH) / game.GAME_PPM, b2body.getPosition().y, 0);
            game.savedGame.wrapped++;
            game.playSound(game.warpSound);
            game.tasksTracker.update(game.savedGame);
        }//if -x
        if (b2body.getPosition().x * game.GAME_PPM > game.GAME_WIDTH) {
            b2body.setTransform((b2body.getPosition().x * game.GAME_PPM - game.GAME_WIDTH) / game.GAME_PPM, b2body.getPosition().y, 0);
            game.savedGame.wrapped++;
            game.playSound(game.warpSound);
            game.tasksTracker.update(game.savedGame);
        }//if +x
        if (b2body.getPosition().y * game.GAME_PPM < 0) {
            b2body.setTransform( b2body.getPosition().x,(b2body.getPosition().y * game.GAME_PPM + game.GAME_HEIGHT) / game.GAME_PPM, 0);
            game.savedGame.wrapped++;
            game.playSound(game.warpSound);
            game.tasksTracker.update(game.savedGame);
        }//if -y
        if (b2body.getPosition().y * game.GAME_PPM > game.GAME_HEIGHT) {
            b2body.setTransform( b2body.getPosition().x,(b2body.getPosition().y * game.GAME_PPM - game.GAME_HEIGHT) / game.GAME_PPM, 0);
            game.savedGame.wrapped++;
            game.playSound(game.warpSound);
            game.tasksTracker.update(game.savedGame);
        }//if +y
        //======================================================

    }

    public TextureRegion getFrame(float dt) {
        currentState = getSate();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = (TextureRegion) playerFall.getKeyFrame(stateTimer);
                break;
            case STANDING:
            default:
                region = (TextureRegion) playerIdle.getKeyFrame(stateTimer, true);
                break;
        }//switch
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getSate() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y > 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else return State.STANDING;
    }

    private void definePlayer() {
        bdef = new BodyDef();
        bdef.position.set(game.levelData.start.x / game.GAME_PPM, game.levelData.start.y / game.GAME_PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = game.world.createBody(bdef);
        //fixture definition
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30 / game.GAME_PPM);
        fdef.filter.maskBits = (short) (game.GROUND_BIT | game.DOOR_BIT | game.DEFAULT_BIT);//with what fixtures player can collide with

        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);

        //create sensor
        CircleShape sensor = new CircleShape();
        sensor.setRadius(34 / game.GAME_PPM);
        fdef.shape = sensor;
        fdef.isSensor = true;
        sensorFixture = b2body.createFixture(fdef);
        sensorFixture.setUserData(this);
        setFilter(game.PLAYER_BIT);
    }

    public void setFilter(short bit) {
        filter.categoryBits = bit;
        sensorFixture.setFilterData(filter);
    }

}
