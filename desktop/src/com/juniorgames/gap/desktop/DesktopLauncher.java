package com.juniorgames.gap.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.juniorgames.gap.GapGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.width = 480 * 2;
        cfg.height = 272 * 2;
        cfg.fullscreen = false;
        cfg.forceExit = false;// to prevent error exit value -1
        new LwjglApplication(new GapGame(), cfg);
    }
}
