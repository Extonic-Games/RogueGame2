package me.extain.game.gameObject.inventory;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import me.extain.game.Assets;
import me.extain.game.gameObject.item.Item;
import me.extain.game.gameObject.item.ItemFactory;

public class LootbagUI extends Window implements InventorySubject, SlotObserver{

    private int lengthSlotRow = 4;
    public final static int numSlots = 7;
    private DragAndDrop dragAndDrop;
    private Table lootBagSlotTable;
    private Array<Actor> lootActors;

    private final int slotWidth = 32;
    private final int slotHeight = 32;

    private SlotToolTip toolTip;

    public LootbagUI() {
        super("Lootbag", Assets.getInstance().getStatusSkin(), "solidbackground");

        dragAndDrop = new DragAndDrop();
        lootActors = new Array<>();

        lootBagSlotTable = new Table();
        lootBagSlotTable.setName("Loot_Bag_Table");

        lootBagSlotTable.setBackground(new Image(new NinePatch(Assets.getInstance().getAssets().get("skins/statusui/statusui.atlas", TextureAtlas.class).createPatch("dialog"))).getDrawable());

        toolTip = new SlotToolTip(Assets.getInstance().getStatusSkin());

        for (int i = 0; i <= numSlots; i++) {
            Slot slot = new Slot();
            slot.addListener(new SlotToolTipListener(toolTip));
            dragAndDrop.addTarget(new SlotTarget(slot));
            lootBagSlotTable.add(slot).size(slotWidth, slotHeight);

            if (i % lengthSlotRow == 0) {
                lootBagSlotTable.row();
            }
        }

        lootActors.add(toolTip);

        this.row();
        this.add(lootBagSlotTable).colspan(1);
        this.row();
        this.pack();

    }

    public void addEntityToLootbag(String itemName) {
        Array<Cell> sourceCells = lootBagSlotTable.getCells();
        int index = 0;

        for (; index < sourceCells.size; index++) {
            Slot slot = ((Slot) sourceCells.get(index).getActor());
            if (slot == null) break;

            int numItems = slot.getNumItems();

            if (slot.getTopItem() == null) {
                Item item = ItemFactory.instantiate().getItem(itemName);
                slot.add(item);
                dragAndDrop.addSource(new SlotSource(slot, dragAndDrop));
                break;
            }
        }
    }

    public void addTargets(Table table) {
        for (int i = 0; i < table.getCells().size; i++) {
            Slot slot = ((Slot) table.getCells().get(i).getActor());

            dragAndDrop.addTarget(new SlotTarget(slot));
        }
    }

    public boolean isEmpty() {
        Array<Cell> sourceCells = lootBagSlotTable.getCells();

        for (int i = 0; i < sourceCells.size; i++) {
            Slot slot = ((Slot) sourceCells.get(i).getActor());

            if (slot.getTopItem() == null) return true;
            else return false;
        }

        return false;
    }

    public Array<Actor> getLootActors() {
        return lootActors;
    }

    @Override
    public void addObserver(InventoryObserver inventoryObserver) {

    }

    @Override
    public void removeObserver(InventoryObserver inventoryObserver) {

    }

    @Override
    public void removeAllObservers() {

    }

    @Override
    public void notify(String value, InventoryObserver.InventoryEvent event) {

    }

    @Override
    public void onNotify(Slot slot, SlotEvent event) {

    }
}
