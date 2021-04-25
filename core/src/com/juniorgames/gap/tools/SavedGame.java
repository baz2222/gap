package com.juniorgames.gap.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class SavedGame {
    public int world = 1;
    public int level = 1;
    public SavedGame() {
    //constructor
    }
    public void load(){
        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(Gdx.files.internal("saved-game.json"));
        world = json.getInt("world");
        level = json.getInt("level");
    }
}
