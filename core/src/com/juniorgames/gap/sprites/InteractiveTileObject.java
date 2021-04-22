package com.juniorgames.gap.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.juniorgames.gap.GapGame.GAME_PPM;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / GAME_PPM, (bounds.getY() + bounds.getHeight() / 2) / GAME_PPM);
        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2) / GAME_PPM, (bounds.getHeight() / 2) / GAME_PPM);
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
        return layer.getCell((int) (body.getPosition().x * GAME_PPM / 32), (int) (body.getPosition().y * GAME_PPM / 32));
    }
}
