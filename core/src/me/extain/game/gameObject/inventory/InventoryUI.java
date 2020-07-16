package me.extain.game.gameObject.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.item.Item;
import me.extain.game.gameObject.item.ItemFactory;
import me.extain.game.network.Packets.InventoryUpdatePacket;

public class InventoryUI extends Window implements InventorySubject, SlotObserver {

    public final static int numSlots = 20;
    public static final String PLAYER_INVENTORY = "Player_Inventory";
    public static final String STORE_INVENTORY = "Store_Inventory";

    private int lengthSlotRow = 10;
    private Table inventorySlotTable;
    private Table playerSlotsTable;
    private Table equipSlots;
    private DragAndDrop dragAndDrop;
    private Array<Actor> inventoryActors;

    private final int slotWidth = 32;
    private final int slotHeight = 32;

    private SlotToolTip toolTip;

    private Array<InventoryObserver> observers;

    public InventoryUI() {
        super("Inventory", Assets.getInstance().getStatusSkin(), "solidbackground");

        observers = new Array<>();
        dragAndDrop = new DragAndDrop();
        inventoryActors = new Array<Actor>();
        toolTip = new SlotToolTip(Assets.getInstance().getStatusSkin());

        inventorySlotTable = new Table();
        inventorySlotTable.setName("Inventory_Slot_Table");

        this.getColor().a = 0.7f;

        playerSlotsTable = new Table();
        equipSlots = new Table();
        equipSlots.setName("Equipment_Slot_Table");

        equipSlots.defaults().space(10);
        Slot wepSlot = new Slot(Item.ItemUseType.WEAPON.getValue());
        wepSlot.addListener(new SlotToolTipListener(toolTip));
        equipSlots.add(wepSlot).size(slotWidth, slotHeight);

        Slot abiSlot = new Slot(Item.ItemUseType.ABILITY.getValue());
        abiSlot.addListener(new SlotToolTipListener(toolTip));
        equipSlots.add(abiSlot).size(slotWidth, slotHeight);

        Slot armSlot = new Slot(Item.ItemUseType.ARMOR_CHEST.getValue());
        armSlot.addListener(new SlotToolTipListener(toolTip));
        equipSlots.add(armSlot).size(slotWidth, slotHeight);

        Slot othSlot = new Slot(Item.ItemUseType.ARMOR_FEET.getValue());
        othSlot.addListener(new SlotToolTipListener(toolTip));
        equipSlots.add(othSlot).size(slotWidth, slotHeight);

        wepSlot.setId(0);
        abiSlot.setId(1);
        armSlot.setId(2);
        othSlot.setId(3);

        wepSlot.addObserver(this);
        abiSlot.addObserver(this);
        armSlot.addObserver(this);
        othSlot.addObserver(this);

        dragAndDrop.addTarget(new SlotTarget(wepSlot));
        dragAndDrop.addTarget(new SlotTarget(abiSlot));
        dragAndDrop.addTarget(new SlotTarget(armSlot));
        dragAndDrop.addTarget(new SlotTarget(othSlot));


        playerSlotsTable.setBackground(new Image(new NinePatch(Assets.getInstance().getAssets().get("skins/statusui/statusui.atlas", TextureAtlas.class).createPatch("dialog"))).getDrawable());

        for (int i = 1; i <= numSlots; i++) {
            Slot slot = new Slot();
            slot.setId(i + 10);
            slot.addListener(new SlotToolTipListener(toolTip));
            slot.addObserver(this);
            dragAndDrop.addTarget(new SlotTarget(slot));
            inventorySlotTable.add(slot).size(slotWidth, slotHeight);

            if (i % lengthSlotRow == 0) {
                inventorySlotTable.row();
            }
        }

        playerSlotsTable.add(equipSlots);
        inventoryActors.add(toolTip);

        this.row();
        this.add(playerSlotsTable).padBottom(20);
        this.row();
        this.add(inventorySlotTable).colspan(1);
        this.row();
        this.pack();

    }

    public boolean doesInventoryHaveSpace() {
        Array<Cell> sourceCells = inventorySlotTable.getCells();
        int index = 0;

        for (; index < sourceCells.size; index++) {
            Slot slot = ((Slot) sourceCells.get(index).getActor());
            if (slot == null) continue;

            int numItems = slot.getNumItems();

            if (numItems == 0) {
                return true;
            } else {
                index++;
            }
        }
        return false;
    }


    public void addEntityToInventory(String itemName) {
        Array<Cell> sourceCells = inventorySlotTable.getCells();
        int index = 0;

        for (; index < sourceCells.size; index++) {
            Slot slot = ((Slot) sourceCells.get(index).getActor());
            if (slot == null) break;

            int numItems = slot.getNumItems();

            if (slot.getTopItem() == null) {
                Item item = ItemFactory.instantiate().getItem(itemName);
                item.setName(itemName);
                slot.add(item);
                dragAndDrop.addSource(new SlotSource(slot, dragAndDrop));
                break;
            }
        }
    }

    public void addEntityToInventory(int slotID, String itemName) {
        Array<Cell> sourceCells = inventorySlotTable.getCells();
        int index = 0;

        for (; index < sourceCells.size; index++) {
            Slot slot = ((Slot) sourceCells.get(index).getActor());
            if (slot == null) break;

            int numItems = slot.getNumItems();

            if (itemName != "") {
                if (slot.getId() == slotID && slot.getTopItem() == null) {
                    Item item = ItemFactory.instantiate().getItem(itemName);
                    item.setName(itemName);
                    slot.add(item);
                    dragAndDrop.addSource(new SlotSource(slot, dragAndDrop));
                }
                break;
            }
        }
    }

    public void addItemToEquip(String itemName) {
        Array<Cell> sourceCells = equipSlots.getCells();
        int index = 0;

        for (; index < sourceCells.size; index++) {
            Slot slot = ((Slot) sourceCells.get(index).getActor());
            if (slot == null) break;

            int numItems = slot.getNumItems();

            if (slot.getTopItem() == null && slot.doesAcceptItemType(ItemFactory.instantiate().getItem(itemName).getItemUseType())) {
                Item item = ItemFactory.instantiate().getItem(itemName);
                item.setName(itemName);
                slot.add(item);
                dragAndDrop.addSource(new SlotSource(slot, dragAndDrop));
                break;
            } else {
                System.out.println("Slot doesn't accept item!");
            }
        }
    }

    public void addItemToEquip(int slotID, String itemName) {
        Array<Cell> sourceCells = equipSlots.getCells();
        int index = 0;

        for (; index < sourceCells.size; index++) {
            Slot slot = ((Slot) sourceCells.get(index).getActor());
            if (slot == null) break;

            int numItems = slot.getNumItems();

            if (slot.getId() == slotID && itemName != "") {
                if (slot.getTopItem() == null && slot.doesAcceptItemType(ItemFactory.instantiate().getItem(itemName).getItemUseType())) {
                    Item item = ItemFactory.instantiate().getItem(itemName);
                    item.setName(itemName);
                    slot.add(item);
                    dragAndDrop.addSource(new SlotSource(slot, dragAndDrop));
                    break;
                } else {
                    System.out.println("Slot doesn't accept item!");
                }
            }
        }
    }

    public Array<Actor> getInventoryActors() {
        return inventoryActors;
    }

    public Table getInventorySlotTable() {
        return inventorySlotTable;
    }


    @Override
    public void addObserver(InventoryObserver inventoryObserver) {
        observers.add(inventoryObserver);
    }

    @Override
    public void removeObserver(InventoryObserver inventoryObserver) {
        observers.removeValue(inventoryObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for (InventoryObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(String value, InventoryObserver.InventoryEvent event) {
        for (InventoryObserver observer : observers) {
            observer.onNotify(value, event);
        }
    }

    public boolean isInInventory(float x, float y) {
        Rectangle rectangle = new Rectangle();

        float iWidth = this.getWidth();
        float iHeight = this.getHeight();
        float iX = this.getX();
        float iY = this.getY();

        rectangle.set(iX, iY, iWidth, iHeight);

        //this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;

        if (this.isVisible() && iX <= x && iX + iWidth >= x && iY <= y && iY + iHeight >= y) return true;

        return false;
    }

    @Override
    public void onNotify(Slot slot, SlotEvent event) {

        if (event == SlotEvent.ADDED_ITEM) {
            InventoryUpdatePacket updatePacket = new InventoryUpdatePacket();
            if (slot.getId() <= 10) {
                updatePacket.isEquipSlots = true;
                updatePacket.slotID = slot.getId();
            } else if (slot.getId() >= 10) {
                updatePacket.isEquipSlots = false;
                updatePacket.slotID = slot.getId();
            }
            updatePacket.itemName = slot.getTopItem().getItemTypeID();
            updatePacket.isAdded = true;

            updatePacket.accountID = RogueGame.getInstance().getAccount().getId();
            RogueGame.getInstance().getClient().sendUDP(updatePacket);
        }

        if (event == SlotEvent.REMOVED_ITEM) {
            InventoryUpdatePacket updatePacket = new InventoryUpdatePacket();
            if (slot.getId() <= 10) {
                updatePacket.isEquipSlots = true;
                updatePacket.slotID = slot.getId();
            } else if (slot.getId() >= 10) {
                updatePacket.isEquipSlots = false;
                updatePacket.slotID = slot.getId();
            }
            updatePacket.itemName = slot.getTopItem().getItemTypeID();
            updatePacket.isAdded = false;

            updatePacket.accountID = RogueGame.getInstance().getAccount().getId();
            RogueGame.getInstance().getClient().sendUDP(updatePacket);
        }
    }
}
