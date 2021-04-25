package com.juniorgames.gap.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.juniorgames.gap.GapGame;

public class Door extends InteractiveTileObject {
    protected GapGame game;
    public Door(World world, TiledMap map, Rectangle bounds, GapGame game) {
        super(world, map, bounds, game);
        this.game = game;
        fixture.setUserData(this);
        setCategoryFilter(game.DOOR_BIT);
    }

    @Override
    public void onHit() {
    }
}
