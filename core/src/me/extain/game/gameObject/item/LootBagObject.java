package me.extain.game.gameObject.item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.inventory.InventoryUI;
import me.extain.game.gameObject.inventory.LootbagUI;

public class LootBagObject extends GameObject {

    private LootbagUI lootBagUI;

    public LootBagObject(Vector2 position) {
        super(position, Box2DHelper.createSensorCircle(position, 2f, Box2DHelper.BIT_ITEM));

        this.getBody().setUserData(this);

        lootBagUI = new LootbagUI();
        lootBagUI.setVisible(false);
        lootBagUI.setMovable(false);
        lootBagUI.setKeepWithinStage(true);
        lootBagUI.setPosition(50, 20);

        lootBagUI.addEntityToLootbag("stick");

    }

    public void update(float deltaTime) {
        lootBagUI.act(deltaTime);
        if (lootBagUI.isEmpty()) this.setDestroy(true);
    }

    public void render(Batch batch) {

    }

    public LootbagUI getUI() {
        return lootBagUI;
    }

    public void showUI() {
        lootBagUI.setVisible(true);
    }

    public void hideUI() {
        lootBagUI.setVisible(false);
    }
}
