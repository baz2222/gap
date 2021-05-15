package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.GapGame;

public class Bump extends Sprite {
    private GapGame game;
    private AssetManager manager;
    private Array<TextureRegion> bumpFramesRegion;
    private Animation bumpAnimation, hittedBumpAnimation;

    private BodyDef bdef;
    public Body b2body;
    private FixtureDef fdef;
    private PolygonShape bumpShape;
    private Fixture fixture;
    private Filter filter;
    private float x, y;
    public boolean isHitted;
    public Player target;

    private float stateTimer = 0;
    private float hitTimer = 0;

    public Bump(GapGame game, float bumpX, float bumpY, Player target) {
        super(game.bumpAtlas.findRegion("bump"));
        this.game = game;
        this.manager = game.manager;
        this.x = bumpX;
        this.y = bumpY;
        isHitted = false;
        this.target = target;
        filter = new Filter();
        defineBump();
        bumpFramesRegion = new Array<>();
        for (int i = 1; i < 7; i++) {
            bumpFramesRegion.add(new TextureRegion(getTexture(), i * 64, 0, 64, 64));
        }//for
        hittedBumpAnimation = new Animation(0.1f, bumpFramesRegion);
        bumpFramesRegion.clear();

        bumpFramesRegion.add(new TextureRegion(getTexture(), 0, 0, 64, 64));
        bumpAnimation = new Animation(0.1f, bumpFramesRegion);
        bumpFramesRegion.clear();

        setBounds(0, 0, 64 / game.GAME_PPM, 64 / game.GAME_PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() * 0.03f);
    }//constructor

    public void update(float dt) {
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        stateTimer = stateTimer + dt;
        if (isHitted == true) {
            hitTimer = hitTimer + dt;
            //===Hit animation duration handling
            if (hitTimer > 0.5){
                isHitted = false;
                hitTimer = 0;
            }//if
            //================
            return new TextureRegion((TextureRegion) hittedBumpAnimation.getKeyFrame(stateTimer, true));
        } else {
            return new TextureRegion((TextureRegion) bumpAnimation.getKeyFrame(stateTimer, false));
        }//if - else
    }

    private void defineBump() {
        bdef = new BodyDef();
        bdef.position.set(x / game.GAME_PPM, y / game.GAME_PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = game.world.createBody(bdef);

        bumpShape = new PolygonShape();
        bumpShape.setAsBox(24 / game.GAME_PPM, 2 / game.GAME_PPM);
        fdef = new FixtureDef();
        fdef.filter.maskBits = (short) (game.PLAYER_BIT);//with what fixtures can collide with

        fdef.shape = bumpShape;
        fdef.isSensor = true;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        setFilter(game.BUMP_BIT);
    }//defineSwitch

    public void onHit() {
    }//onHit

    public void setFilter(short bit) {
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }
}
