package com.juniorgames.gap.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.GapGame;

public class Crumbles {
    private Body body;
    private BodyDef bdef;
    private FixtureDef fdef;
    private PolygonShape shape;
    private Fixture fixture;
    private Filter filter;
    private GapGame game;
    private AssetManager manager;

    public Crumbles(GapGame game) {
        this.game = game;
        this.manager = game.manager;
        defineCrumbles();
    }

    private void defineCrumbles() {
        bdef = new BodyDef();
        fdef = new FixtureDef();
        shape = new PolygonShape();
        filter = new Filter();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((game.bounds.getX() + game.bounds.getWidth() / 2) / game.GAME_PPM, (game.bounds.getY() + game.bounds.getHeight() / 2) / game.GAME_PPM);
        body = game.world.createBody(bdef);
        fdef.filter.maskBits = (short) (game.PLAYER_BIT | game.DEFAULT_BIT);//with what fixtures door can collide with

        shape.setAsBox((game.bounds.getWidth() / 2) / game.GAME_PPM, (game.bounds.getHeight() / 2) / game.GAME_PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        setFilter(game.CRUMBLES_BIT);
    }

    public void onHit() {
    }

    public void setFilter(short bit) {
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }

}