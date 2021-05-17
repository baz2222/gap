package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.screens.LevelScreen;

public class Player extends Sprite {

    public GapGame.State previousState;
    public GapGame.State currentState;
    public Body b2body;
    private Filter filter;
    private Fixture fixture;
    private Animation playerIdle;
    private float stateTimer;
    private boolean runningRight;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerFall;
    private Animation playerDie;
    private GapGame game;
    private AssetManager manager;
    private BodyDef bdef;
    private FixtureDef fdef;
    private TextureRegion region;
    public Buff.BuffType buff;
    public float jumpMultiplier;
    public boolean isRunningRight;
    public boolean isRunningLeft;

    public Player(GapGame game, float playerX, float playerY) {
        super(game.playerAtlas.findRegion("player"));
        this.game = game;
        this.manager = game.manager;
        filter = new Filter();
        currentState = GapGame.State.STANDING;
        previousState = GapGame.State.STANDING;
        stateTimer = 0;
        runningRight = true;
        jumpMultiplier = 1;
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
        //die animation
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 64, 128, 64, 64));
        }//for
        playerDie = new Animation(0.1f, frames);
        frames.clear();

        definePlayer(playerX, playerY);
        setBounds(0, 0, 64 / game.GAME_PPM, 64 / game.GAME_PPM);

    }//constructor

    public void setBuffJumpAbility() {
        buff = Buff.BuffType.JUMP;
        jumpMultiplier = jumpMultiplier * 2;
    }

    public void setBuffShieldAbility() {
        buff = Buff.BuffType.SHIELD;
    }

    public void setBuffBombAbility() {
        buff = Buff.BuffType.BOMB;
    }

    public void removeAllBuffAbility() {
        if (buff != null) {
            if (buff == Buff.BuffType.JUMP) {
                jumpMultiplier = 1;
                buff = null;
            }//if jump
            if (buff == Buff.BuffType.BOMB){
                buff = null;
            }//if bomb
            if (buff == Buff.BuffType.SHIELD){
                buff = null;
            }//if shield
        }// if not null
    }

    public void jump() {
        if (currentState != GapGame.State.JUMPING && currentState != GapGame.State.FALLING) {
            b2body.applyLinearImpulse(new Vector2(0, 6.5f * jumpMultiplier), b2body.getWorldCenter(), true);
            if (!game.soundsMuted) {
                game.playSound(game.jumpSound);
            }//if sounds muted
        }//if not jumping
    }//jump

    public void runRight() {
        if (b2body.getLinearVelocity().x <= 2)
            b2body.applyLinearImpulse(new Vector2(1f, 0), b2body.getWorldCenter(), true);
    }//moveRight

    public void runLeft() {
        if (b2body.getLinearVelocity().x >= -2)
            b2body.applyLinearImpulse(new Vector2(-1f, 0), b2body.getWorldCenter(), true);
    }//moveLeft

    public void die() {
        game.savedGame.died++;
        game.savedGame.save();
        game.tasksTracker.update(game.savedGame);
    }//die

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() * 0.4f);
        setRegion(getFrame(dt));
        //=======================DIE============================
        if (filter.categoryBits == game.DESTROYED_BIT) {
            if (b2body.getPosition().y * game.GAME_PPM > game.GAME_HEIGHT) {
                game.stopMusic();
                die();
                game.setScreen(new LevelScreen(game, manager));
            } else {// else flying up the screen
                b2body.setActive(false);
                b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y + dt * 2, 0);
            }//else
        }//if destroyed
        if(filter.categoryBits != game.DESTROYED_BIT) {
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
                b2body.setTransform(b2body.getPosition().x, (b2body.getPosition().y * game.GAME_PPM + game.GAME_HEIGHT) / game.GAME_PPM, 0);
                game.savedGame.wrapped++;
                game.playSound(game.warpSound);
                game.tasksTracker.update(game.savedGame);
            }//if -y
            if (b2body.getPosition().y * game.GAME_PPM > game.GAME_HEIGHT) {
                b2body.setTransform(b2body.getPosition().x, (b2body.getPosition().y * game.GAME_PPM - game.GAME_HEIGHT) / game.GAME_PPM, 0);
                game.savedGame.wrapped++;
                game.playSound(game.warpSound);
                game.tasksTracker.update(game.savedGame);
            }//if +y
        }//if not dead
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING_LEFT:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_RIGHT:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = (TextureRegion) playerFall.getKeyFrame(stateTimer);
                break;
            case DIEING:
                region = (TextureRegion) playerDie.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = (TextureRegion) playerIdle.getKeyFrame(stateTimer, true);
                break;
        }//switch
        updateDirection();
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    private void updateDirection() {
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }//if
    }

    public GapGame.State getState() {
        if (getFilterBit() == game.DESTROYED_BIT)
            return GapGame.State.DIEING;
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y > 0 && previousState == GapGame.State.JUMPING))
            return GapGame.State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return GapGame.State.FALLING;
        else if (b2body.getLinearVelocity().x > 0)
            return GapGame.State.RUNNING_RIGHT;
        else if (b2body.getLinearVelocity().x < 0)
            return GapGame.State.RUNNING_LEFT;
        else return GapGame.State.STANDING;
    }//getState

    private void definePlayer(float x, float y) {
        bdef = new BodyDef();
        bdef.position.set(x / game.GAME_PPM, y / game.GAME_PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = game.world.createBody(bdef);
        //fixture definition
        fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(14 / game.GAME_PPM, 24 / game.GAME_PPM);
        fdef.filter.maskBits = (short) (game.GROUND_BIT | game.DOOR_BIT | game.DEFAULT_BIT | game.SPIKES_BIT | game.CRUMBLES_BIT);//with what fixtures player can collide with
        fdef.shape = shape;
        fdef.restitution = 0f;
        fdef.friction = 0.5f;
        fdef.density = 0f;
        fixture = b2body.createFixture(fdef);
        setFilterBit(game.PLAYER_BIT);
        fixture.setUserData(this);
    }

    public void setFilterBit(short bit) {
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }

    public short getFilterBit() {
        return fixture.getFilterData().categoryBits;
    }

}
