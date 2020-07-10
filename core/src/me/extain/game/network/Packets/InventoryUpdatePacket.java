package me.extain.game.network.Packets;

public class InventoryUpdatePacket {

    public int accountID;
    public boolean isEquipSlots;
    public int slotID;
    public String itemName;
    public boolean isAdded;

}
