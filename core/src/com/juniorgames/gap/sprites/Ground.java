package com.juniorgames.gap.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.juniorgames.gap.GapGame;

public class Ground extends InteractiveTileObject {
    public Ground(World world, TiledMap map, Rectangle bounds, GapGame game) {
        super(world, map, bounds, game);
    }

    @Override
    public void onHit() {
    }
}