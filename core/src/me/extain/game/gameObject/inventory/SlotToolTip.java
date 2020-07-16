package me.extain.game.gameObject.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import me.extain.game.gameObject.item.Item;

public class SlotToolTip extends Window {

    private Skin skin;
    private Label description;


    public SlotToolTip(Skin skin) {
        super("", skin);
        this.skin = skin;

        description = new Label("", skin, "inventory-item-count");
        this.add(description);
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(false);
    }

    public void setVisible(Slot slot, boolean visible) {
        super.setVisible(visible);

        if (slot == null) return;

        if (!slot.hasItem()) super.setVisible(false);
    }

    public void updateDescription(Slot slot) {
        if (slot.hasItem()) {
            StringBuilder string = new StringBuilder();
            Item item = slot.getTopItem();
            string.append(item.getItemTypeID());
            string.append(System.getProperty("line.separator"));
            string.append("_____________________");
            if (item.getWeaponStats() != null) {
                string.append(System.getProperty("line.separator"));
                string.append("Damage: ").append(item.getWeaponStats().getDamage()).append(" / ").append(item.getWeaponStats().getMaxDamage());
                string.append(System.getProperty("line.separator"));
                string.append("_____________________");
            }
            string.append(System.getProperty("line.separator"));
            string.append(item.getItemShortDesc());
            string.append(System.getProperty("line.separator"));

            description.setText(string);
            this.pack();
        } else {
            description.setText("");
            this.pack();
        }
    }
}
