package me.extain.game.gameObject.inventory;

public interface SlotObserver {

    public static enum SlotEvent {
        ADDED_ITEM,
        REMOVED_ITEM
    }

    void onNotify(final Slot slot, SlotEvent event);

}
