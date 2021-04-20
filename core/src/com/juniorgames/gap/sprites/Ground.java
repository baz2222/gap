package com.juniorgames.gap.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.juniorgames.gap.GapGame;

public class Ground extends InteractiveTileObject{
    public Ground(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }

    @Override
    public void onHit() {
    }
}