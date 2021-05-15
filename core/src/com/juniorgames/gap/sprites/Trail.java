package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.juniorgames.gap.GapGame;

public class Trail extends Sprite {

    private GapGame game;
    private Player player;
    private AssetManager manager;
    private Array<TextureRegion> trailFramesRegion;
    private Animation trailAnimation;

    private float stateTimer = 0;
    private Buff.BuffType type;

    public Trail(GapGame game, Player player) {
        this.player = player;
        type = player.buff;
        //============begin Sprite super()=====================
        if (type == Buff.BuffType.BOMB) {
            setRegion(game.buffAtlas.findRegion("bomb-trail"));
        }
        if (type == Buff.BuffType.SHIELD) {
            setRegion(game.buffAtlas.findRegion("shield-trail"));
        }
        if (type == Buff.BuffType.JUMP) {
            setRegion(game.buffAtlas.findRegion("jump-trail"));
        }
        setSize(getRegionWidth(), getRegionHeight());
        this.game = game;
        //defineTrail();
        trailFramesRegion = new Array<>();
        for (int i = 0; i < 15; i++) {
            trailFramesRegion.add(new TextureRegion(getTexture(), i * 64 + getRegionX(), getRegionY(), 64, 64));
        }//for
        trailAnimation = new Animation(0.1f, trailFramesRegion);
        trailFramesRegion.clear();

        setBounds(0, 0, 64 / game.GAME_PPM, 64 / game.GAME_PPM);
        setPosition(player.b2body.getPosition().x - getWidth() / 2, player.b2body.getPosition().y - getHeight() * 0.45f);
    }//constructor

    public void update(float dt) {
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        stateTimer = stateTimer + dt;
        return new TextureRegion((TextureRegion) trailAnimation.getKeyFrame(stateTimer, true));
    }//getFrame
}
