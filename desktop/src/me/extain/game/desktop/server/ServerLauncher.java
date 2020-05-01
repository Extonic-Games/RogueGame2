package me.extain.game.desktop.server;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Server.RogueGameServer;

public class ServerLauncher {

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
        new LwjglApplication(new RogueGameServer(), config);
    }

}
