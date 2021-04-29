package com.juniorgames.gap.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.sprites.Ground;

public class B2WorldCreator {
    public B2WorldCreator(GapGame game) {
        //ground platform
        for (MapObject object : game.platformMap.getLayers().get("GroundObjectsLayer").getObjects().getByType(RectangleMapObject.class)) {
            game.bounds = ((RectangleMapObject) object).getRectangle();
            new Ground(game);
        }//for
    }//constructor
}
