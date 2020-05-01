package me.extain.game.gameObject.inventory;

public interface SlotSubject {

    public void addObserver(SlotObserver observer);
    public void removeObserver(SlotObserver observer);
    public void removeAllObservers();
    public void notify(Slot slot, SlotObserver.SlotEvent event);

}
