package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.GapGame;

public class Buff extends Sprite {

    public enum BuffType {BOMB, JUMP, SHIELD}

    private GapGame game;
    private AssetManager manager;
    private Array<TextureRegion> buffFramesRegion;
    private Animation buffAnimation;

    private BodyDef bdef;
    public Body b2body;
    private FixtureDef fdef;
    private PolygonShape buffShape;
    private Fixture fixture;
    private Filter filter;
    private float x, y;
    private float stateTimer = 0;
    public BuffType type;
    public boolean isVisible = true;

    public Buff(GapGame game, float buffX, float buffY, BuffType type) {
        this.type = type;
        //============begin Sprite super()=====================
        if (type == BuffType.BOMB) {
            setRegion(game.buffAtlas.findRegion("bomb-buff"));
        }
        if (type == BuffType.SHIELD) {
            setRegion(game.buffAtlas.findRegion("shield-buff"));
        }
        if (type == BuffType.JUMP) {
            setRegion(game.buffAtlas.findRegion("jump-buff"));
        }
        setSize(getRegionWidth(), getRegionHeight());
        this.game = game;
        this.x = buffX;
        this.y = buffY;
        filter = new Filter();
        defineBuff();
        buffFramesRegion = new Array<>();
        for (int i = 0; i < 7; i++) {
            buffFramesRegion.add(new TextureRegion(getTexture(), i * 64 + getRegionX(), getRegionY(), 64, 64));
        }//for
        buffAnimation = new Animation(0.1f, buffFramesRegion);
        buffFramesRegion.clear();

        setBounds(0, 0, 64 / game.GAME_PPM, 64 / game.GAME_PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() * 0.35f);
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
        }//else
    }//update

    public TextureRegion getFrame(float dt) {
        stateTimer = stateTimer + dt;
        return new TextureRegion((TextureRegion) buffAnimation.getKeyFrame(stateTimer, true));
    }

    private void defineBuff() {
        bdef = new BodyDef();
        bdef.position.set(x / game.GAME_PPM, y / game.GAME_PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = game.world.createBody(bdef);

        buffShape = new PolygonShape();
        buffShape.setAsBox(24 / game.GAME_PPM, 30 / game.GAME_PPM);
        fdef = new FixtureDef();
        fdef.filter.maskBits = (short) (game.PLAYER_BIT);//with what fixtures can collide with

        fdef.shape = buffShape;
        fdef.isSensor = true;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        setFilter(game.BUFF_BIT);
    }//defineBuff

    public void onHit() {
        isVisible = false;
        game.player.removeAllBuffAbility();
        if (type == BuffType.JUMP){
            game.player.setBuffJumpAbility();
        }
        if (type == BuffType.BOMB){
            game.player.setBuffBombAbility();
        }
        if (type == BuffType.SHIELD){
            game.player.setBuffShieldAbility();
        }
    }//onHit

    public void setFilter(short bit) {
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }
}
