package com.juniorgames.gap.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.juniorgames.gap.GapGame;

import static com.juniorgames.gap.GapGame.DOOR_BIT;

public class Door extends InteractiveTileObject {
    public Door(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(DOOR_BIT);
    }

    @Override
    public void onHit() {
    }
}
