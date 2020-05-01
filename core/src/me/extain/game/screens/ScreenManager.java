package me.extain.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

import me.extain.game.gameObject.Player.RemotePlayer;

public class ScreenManager {

    private HashMap<String, Screen> screens;
    private Screen currentScreen = null;

    public ScreenManager() {
        screens = new HashMap<>();
    }

    public void addScreen(String name, Screen screen) {
        screens.put(name, screen);
    }

    public void changeScreen(String name) {
        currentScreen = screens.get(name);
        currentScreen.show();
    }

    public void renderScreen(float delta) {
        currentScreen.render(delta);
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void dispose() {
        for (HashMap.Entry<String, Screen> entry : screens.entrySet()) {
            entry.getValue().dispose();
        }
    }

}
