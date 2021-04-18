package com.juniorgames.gap.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelData {
    public int world;// world number
    public int level;// level number
    public Vector2 start;// coords where player appears at start
    public Vector2 exit;// coords of exit doors
    public Array<Vector2> plant1s;// array of plants version 1 coords to put on current map
    public Array<Vector2> plant2s;// array of plants version 2 coords to put on current map
    public Array<Vector2> switches;// array of switches coords to put on current map
    public Array<Vector2> enemies;
    public Array<Vector2> spikeEnemies;
    public Array<Vector2> bumps;
    public Array<Vector2> buffBombs;
    public Array<Vector2> buffJumps;
    public Array<Vector2> buffShields;
    public Vector2 tutorial;// position of tutorial image IF IT EXIST FOR CURRENT LEVEL!

    public LevelData() {
    }//constructor
}
