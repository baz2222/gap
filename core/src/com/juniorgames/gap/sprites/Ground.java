package com.juniorgames.gap.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.juniorgames.gap.GapGame;

public class Ground extends InteractiveTileObject{
    private Sound landSound;
    public Ground(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        landSound = GapGame.manager.get("audio/sounds/land.mp3", Sound.class);
    }

    @Override
    public void onHit() {
        landSound.setLooping(landSound.play(),false);
    }
}