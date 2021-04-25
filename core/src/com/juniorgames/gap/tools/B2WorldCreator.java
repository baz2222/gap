package com.juniorgames.gap.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.sprites.Door;
import com.juniorgames.gap.sprites.Ground;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map, GapGame game) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //ground platform
        for (MapObject object : map.getLayers().get("GroundObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Ground(world, map, rect, game);
        }
        //door
        //for (MapObject object : map.getLayers().get("exito").getObjects().getByType(RectangleMapObject.class)) {
        //    Rectangle rect = ((RectangleMapObject) object).getRectangle();

        //    new Door(world, map, rect);
        //}
    }
}
