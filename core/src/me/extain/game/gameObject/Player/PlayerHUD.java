package me.extain.game.gameObject.Player;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.inventory.InventoryObserver;
import me.extain.game.gameObject.inventory.InventoryUI;

public class PlayerHUD implements Screen, InventoryObserver {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private GameObject player;
    private InventoryUI inventoryUI;

    private Json json;

    private static final String INVENTORY_FULL = "Your inventory is full!";

    public PlayerHUD(OrthographicCamera camera, Viewport viewport, GameObject player) {
        this.camera = camera;
        this.player = player;
        this.viewport = viewport;
        stage = new Stage(viewport);

        this.json = new Json();

        inventoryUI = new InventoryUI();
        inventoryUI.setKeepWithinStage(true);
        inventoryUI.setMovable(true);
        inventoryUI.setVisible(false);
        inventoryUI.setPosition(0, 0);

        stage.addActor(this.inventoryUI);
        inventoryUI.validate();

        Array<Actor> actors = inventoryUI.getInventoryActors();
        for (Actor actor : actors) {
            stage.addActor(actor);
        }

        this.inventoryUI.addObserver(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void onNotify(String value, InventoryEvent event) {

    }

    public InventoryUI getInventoryUI() {
        return inventoryUI;
    }

    public void displayInventory() {
        inventoryUI.setVisible(inventoryUI.isVisible() ? false : true);
    }

    public boolean isInventory() {
        return inventoryUI.isVisible();
    }

    public Stage getStage() {
        return stage;
    }
}
