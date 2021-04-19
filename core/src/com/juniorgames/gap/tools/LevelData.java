package com.juniorgames.gap.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelData {
    public int world = 0;// world number
    public int level = 0;// level number
    public Vector2 start = new Vector2();// coords where player appears at start
    public Vector2 exit = new Vector2();// coords of exit doors
    public Array<Vector2> plant1s = new Array<>();// array of plants version 1 coords to put on current map
    public Array<Vector2> plant2s = new Array<>();// array of plants version 2 coords to put on current map
    public Array<Vector2> switches = new Array<>();// array of switches coords to put on current map
    public Array<Vector2> enemies = new Array<>();
    public Array<Vector2> spikeEnemies = new Array<>();
    public Array<Vector2> bumps = new Array<>();
    public Array<Vector2> buffBombs = new Array<>();
    public Array<Vector2> buffJumps = new Array<>();
    public Array<Vector2> buffShields = new Array<>();
    public Vector2 tutorial = new Vector2();// position of tutorial image IF IT EXIST FOR CURRENT LEVEL!
}
