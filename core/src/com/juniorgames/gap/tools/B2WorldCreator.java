package com.juniorgames.gap.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.sprites.Door;
import com.juniorgames.gap.sprites.Ground;

import static com.juniorgames.gap.GapGame.GAME_PPM;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //ground platform
        for (MapObject object : map.getLayers().get("gndo").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Ground(world, map, rect);
        }
        //door
        for (MapObject object : map.getLayers().get("exito").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Door(world, map, rect);
        }
    }
}
