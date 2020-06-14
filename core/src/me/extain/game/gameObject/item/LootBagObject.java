package me.extain.game.gameObject.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import me.extain.game.Assets;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.inventory.InventoryUI;
import me.extain.game.gameObject.inventory.LootbagUI;

import java.util.ArrayList;

public class LootBagObject extends GameObject {

    private LootbagUI lootBagUI;

    private TextureRegion texture;

    public LootBagObject(Vector2 position) {
        super(position, Box2DHelper.createSensorCircle(position, 2f, Box2DHelper.BIT_ITEM));

        this.getBody().setUserData(this);

        TextureAtlas atlas = Assets.getInstance().getAssets().get("bag/lootbags.atlas");
        texture = atlas.findRegion("basic-bag");

        lootBagUI = new LootbagUI();
        lootBagUI.setVisible(false);
        lootBagUI.setMovable(false);
        lootBagUI.setKeepWithinStage(true);
        lootBagUI.setPosition(50, 20);

        /* TODO: Add a handler that sends out a packet when item has been taken out of bag. Check for the item on the server, and check to see if it is the
        *  same as the one on the server.
        * */

    }

    public void update(float deltaTime) {
        lootBagUI.act(deltaTime);
        if (lootBagUI.isEmpty()) this.setDestroy(true);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, this.getBody().getPosition().x - 4, this.getBody().getPosition().y - 4, 8, 8);
    }

    public void addItem(String item) {
        lootBagUI.addEntityToLootbag(item);
    }

    public void addItems(ArrayList<String> items) {
        for (int i = 0; i < items.size(); i++) {
            lootBagUI.addEntityToLootbag(items.get(i));
        }
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
