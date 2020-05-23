package me.extain.game.gameObject.Player;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.inventory.InventoryObserver;
import me.extain.game.gameObject.inventory.InventoryUI;
import me.extain.game.gameObject.inventory.LootbagUI;
import me.extain.game.network.Packets.MessagePacket;

public class PlayerHUD implements Screen, InventoryObserver {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private GameObject player;
    private InventoryUI inventoryUI;

    private Json json;

    private static final String INVENTORY_FULL = "Your inventory is full!";

    private CharSequence str;

    private Label hpBar;

    private LootbagUI lootbagUI;

    public PlayerHUD(OrthographicCamera camera, Viewport viewport, GameObject player) {
        this.camera = camera;
        this.player = player;
        this.viewport = viewport;
        stage = new Stage(viewport);

        this.json = new Json();

        str = "HP: " + player.getHealth() + "/" + player.getMaxHealth();

        inventoryUI = new InventoryUI();
        inventoryUI.setKeepWithinStage(true);
        inventoryUI.setMovable(true);
        inventoryUI.setVisible(false);
        inventoryUI.setPosition(0, 0);

        stage.addActor(this.inventoryUI);
        inventoryUI.validate();

        hpBar = new Label(str, Assets.getInstance().getStatusSkin(), "default");
        hpBar.setPosition(20, 20);
        hpBar.setSize(10, 10);
        hpBar.setVisible(true);
        stage.addActor(hpBar);
        hpBar.validate();

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
        str = "HP: " + player.getHealth() + "/" + player.getMaxHealth();
        stage.act(delta);

        if (lootbagUI != null && lootbagUI.isEmpty()) {
            destroyLootBag();
        }

        stage.draw();
        hpBar.setText(str);
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

    public void addShowLootbag(LootbagUI ui) {
        if (ui != null) {
            lootbagUI = ui;
            lootbagUI.addTargets(inventoryUI.getInventorySlotTable());
            lootbagUI.setVisible(true);

            stage.addActor(lootbagUI);

            Array<Actor> actors = lootbagUI.getLootActors();
            for (Actor actor : actors) {
                stage.addActor(actor);
            }
        }
    }

    public void hideLootbag() {
        if (lootbagUI != null) {
            lootbagUI.setVisible(false);
        }
    }

    public void destroyLootBag() {
        stage.getActors().removeAll(lootbagUI.getLootActors(), true);
        lootbagUI.setVisible(false);
        lootbagUI = null;
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
