package me.extain.game.gameObject.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.item.Item;

public class Slot extends Stack implements SlotSubject {

    private Stack background;

    private Array<SlotObserver> observers;

    private int numItemsVal = 0;

    private int filterItemType;

    private int id;


    public Slot() {
        filterItemType = 0;
        background = new Stack();
        this.observers = new Array<SlotObserver>();

        Image backg = new Image(new NinePatch(Assets.getInstance().getAssets().get("skins/statusui/statusui.atlas", TextureAtlas.class).createPatch("dialog")));

        background.add(backg);
        background.setName("background");

        this.add(background);

    }

    public Slot(int filter) {
        this();
        filterItemType = filter;
    }

    public void removeItem(boolean sendRemoveNoti) {
        numItemsVal--;
        if (sendRemoveNoti) {

            System.out.println("Sent removed item! ID: " + this.id);
            notify(this, SlotObserver.SlotEvent.REMOVED_ITEM);
        }
    }

    public void addItem(boolean sendAddNoti) {
        numItemsVal++;

        if (sendAddNoti) {
            System.out.println("Sent added item! ID: " + this.id);
            notify(this, SlotObserver.SlotEvent.ADDED_ITEM);
        }
    }

    public void add(Actor actor) {
        super.add(actor);

        if (!actor.equals(background)) {
            addItem(true);
        }

    }

    public void add(Array<Actor> array) {
        for (Actor actor : array) {
            super.add(actor);

            if (!actor.equals(background)) {
                addItem(true);
            }
        }
    }

    public Array<Actor> getItems() {
        Array<Actor> items = new Array<Actor>();

        if (hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numItems = arrayChildren.size - 1;
            for (int i = 0; i < numItems; i++) {
                removeItem(true);
                items.add(arrayChildren.pop());
            }
        }

        return items;
    }


    public boolean hasItem() {
        if (hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            if (items.size > 1) {
                return true;
            }
        }

        return false;
    }

    public void remove(Actor actor) {
        super.removeActor(actor);

        if (!actor.equals(background)) {
            removeItem(true);
        }
    }

    public Item getTopItem() {
        Item actor = null;
        if (hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            if (items.size > 1) {
                actor = (Item) items.peek();
            }
        }

        return actor;
    }

    public int getNumItems() {
        if (hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            return items.size - 1;
        }
        return 0;
    }

    static public void swapSlots(Slot slotSource, Slot slotTarget, Item dragActor) {

        if (!slotTarget.doesAcceptItemType(dragActor.getItemUseType()) || !slotSource.doesAcceptItemType(slotTarget.getTopItem().getItemUseType())) {
            slotSource.add(dragActor);
            return;
        }

        Array<Actor> tempArray = slotSource.getItems();
        tempArray.add(dragActor);
        slotSource.add(slotTarget.getItems());
        slotTarget.add(tempArray);
    }

    public boolean doesAcceptItemType(int itemType) {
        if (filterItemType == 0) {
            return true;
        } else {
            return ((filterItemType & itemType) == itemType);
        }
    }

    @Override
    public void addObserver(SlotObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(SlotObserver observer) {
        observers.removeValue(observer, true);
    }

    @Override
    public void removeAllObservers() {
        for (SlotObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(Slot slot, SlotObserver.SlotEvent event) {
        for (SlotObserver observer : observers) {
            observer.onNotify(slot, event);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
