package com.juniorgames.gap.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.screens.PlayScreen;

import static com.juniorgames.gap.GapGame.GAME_PPM;

public class Player extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DIEING}

    public State previousState;
    public State currentState;
    public World world;
    public Body b2body;
    private Animation playerIdle;
    private float stateTimer;
    private boolean runningRight;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerFall;

    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("player"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
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
        setBounds(0, 0, 64 / GAME_PPM, 64 / GAME_PPM);
        setRegion((TextureRegion) playerIdle.getKeyFrame(stateTimer, true));
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y);
        setRegion(getFrame(dt));
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
        } else if ((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
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
        BodyDef bdef = new BodyDef();
        bdef.position.set(44 / GAME_PPM, 366 / GAME_PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30 / GAME_PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef);
        //create head sensor
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-10/GAME_PPM,32/GAME_PPM),new Vector2(10/GAME_PPM,32/GAME_PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");
    }
}
