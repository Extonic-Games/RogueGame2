package me.extain.game.gameObject.Player;

import com.badlogic.gdx.Gdx;
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
import me.extain.game.ui.ChatUI;
import me.extain.game.ui.PauseUI;

public class PlayerHUD implements Screen, InventoryObserver {

    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private GameObject player;
    private InventoryUI inventoryUI;
    private PauseUI pauseUI;
    private ChatUI chatUI;

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
        inventoryUI.setPosition(80, 0);
        inventoryUI.toFront();

        stage.addActor(this.inventoryUI);
        inventoryUI.validate();

        pauseUI = new PauseUI();
        pauseUI.setKeepWithinStage(true);
        pauseUI.setMovable(false);
        pauseUI.setVisible(false);
        pauseUI.setPosition((Gdx.graphics.getWidth() / 2) - (pauseUI.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (pauseUI.getHeight() / 2));
        pauseUI.validate();

        hpBar = new Label(str, Assets.getInstance().getStatusSkin(), "default");
        hpBar.setPosition(20, 20);
        hpBar.setSize(10, 10);
        hpBar.setVisible(true);
        hpBar.toFront();

        chatUI = new ChatUI();
        chatUI.setPosition(0, 0);
        chatUI.setVisible(true);
        chatUI.setMovable(false);

        Array<Actor> actors = inventoryUI.getInventoryActors();
        for (Actor actor : actors) {
            stage.addActor(actor);
        }

        this.inventoryUI.addObserver(this);


        stage.addActor(chatUI);
        stage.addActor(hpBar);
        stage.addActor(pauseUI);
        hpBar.validate();
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

    public void showPauseUI() {
        pauseUI.setVisible(pauseUI.isVisible() ? false : true);
    }

    public void showChat(boolean vis) {
        chatUI.showTextBox(vis);
    }

    public boolean isChatVis() {
        return chatUI.isTextBox();
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

    public void addChatMessage(String username, String message) {
        chatUI.addMessage(username + ": " + message);
    }
}
