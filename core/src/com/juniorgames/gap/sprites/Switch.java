package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.GapGame;

public class Switch extends Sprite {
    private GapGame game;
    private AssetManager manager;
    private Array<TextureRegion> switchFramesRegion;
    private Animation checkedSwitchAnimation, uncheckedSwitchAnimation;

    private BodyDef bdef;
    public Body b2body;
    private FixtureDef fdef;
    private PolygonShape switchShape;
    private Fixture fixture;
    private Filter filter;
    private float x, y;
    public Door target;

    private float stateTimer = 0;
    public boolean isChecked = false;

    public Switch(GapGame game, float switchX, float switchY, Door target) {
        super(game.switchAtlas.findRegion("switch"));
        this.game = game;
        this.manager = game.manager;
        this.x = switchX;
        this.y = switchY;
        this.target = target;
        filter = new Filter();
        defineSwitch();
        switchFramesRegion = new Array<>();
        for (int i = 0; i < 10; i++) {
            switchFramesRegion.add(new TextureRegion(getTexture(), i * 64, 0, 64, 76));
        }//for
        uncheckedSwitchAnimation = new Animation(0.08f, switchFramesRegion);
        switchFramesRegion.clear();

        for (int i = 0; i < 10; i++) {
            switchFramesRegion.add(new TextureRegion(getTexture(), i * 64, 76, 64, 76));
        }//for
        checkedSwitchAnimation = new Animation(0.08f, switchFramesRegion);
        switchFramesRegion.clear();

        setBounds(0, 0, 64 / game.GAME_PPM, 76 / game.GAME_PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }//constructor

    public void update(float dt) {
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        stateTimer = stateTimer + dt;
        if (isChecked) {
            return new TextureRegion((TextureRegion) checkedSwitchAnimation.getKeyFrame(stateTimer, true));
        } else {
            return new TextureRegion((TextureRegion) uncheckedSwitchAnimation.getKeyFrame(stateTimer, true));
        }//if - else
    }

    private void defineSwitch() {
        bdef = new BodyDef();
        bdef.position.set(x / game.GAME_PPM, y / game.GAME_PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = game.world.createBody(bdef);

        switchShape = new PolygonShape();
        switchShape.setAsBox(24 / game.GAME_PPM, 36 / game.GAME_PPM);
        fdef = new FixtureDef();
        fdef.filter.maskBits = (short) (game.PLAYER_BIT);//with what fixtures can collide with

        fdef.shape = switchShape;
        fdef.isSensor = true;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        setFilter(game.SWITCH_BIT);
    }//defineSwitch

    public void onHit() {
    }//onHit

    public void setFilter(short bit) {
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }
}
