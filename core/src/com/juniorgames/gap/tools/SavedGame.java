package com.juniorgames.gap.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class SavedGame {
    public int world;
    public int level;
    public int wrapped;
    public int died;
    public int killed;
    public int completed;

    public SavedGame() {
        initialize();
        //constructor
    }

    private void initialize() {
        world = 1;
        level = 1;
        wrapped = 0;
        died = 0;
        killed = 0;
        completed = 0;
    }

    public void load() {
        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(Gdx.files.internal("saved-game.json"));
        world = json.getInt("world");
        level = json.getInt("level");
        wrapped = json.getInt("wrapped");
        died = json.getInt("died");
        killed = json.getInt("killed");
        completed = json.getInt("completed");
    }
}
