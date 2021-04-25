package com.juniorgames.gap.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.GapGame;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected GapGame game;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, GapGame game) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        this.game = game;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / game.GAME_PPM, (bounds.getY() + bounds.getHeight() / 2) / game.GAME_PPM);
        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2) / game.GAME_PPM, (bounds.getHeight() / 2) / game.GAME_PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }//constructor

    public abstract void onHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ints");
        return layer.getCell((int) (body.getPosition().x * game.GAME_PPM / 32), (int) (body.getPosition().y * game.GAME_PPM / 32));
    }
}
