package com.juniorgames.gap.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SavedGame {
    public int world;
    public int level;
    public int wrapped;
    public int died;
    public int killed;
    public int completed;

    private Preferences prefs;

    public SavedGame() {
        prefs = Gdx.app.getPreferences("GapGame");
        load();
        //constructor
    }

    public void reset() {
        world = 1;
        level = 1;
        wrapped = 0;
        died = 0;
        killed = 0;
        completed = 0;
        save();
    }

    public void load() {
        world = prefs.getInteger("world", 1);
        level = prefs.getInteger("level", 1);
        wrapped = prefs.getInteger("wrapped", 0);
        died = prefs.getInteger("died", 0);
        killed = prefs.getInteger("killed", 0);
        completed = prefs.getInteger("completed", 0);
    }

    public void save() {
        prefs.putInteger("world", world);
        prefs.putInteger("level", level);
        prefs.putInteger("wrapped", wrapped);
        prefs.putInteger("died", died);
        prefs.putInteger("killed", killed);
        prefs.putInteger("completed", completed);
        prefs.flush();
    }
}
