package me.extain.game.gameObject.Player;

import me.extain.game.gameObject.item.Item;

import java.util.ArrayList;

public class Character {

    public int accountID;
    public int id;

    public ArrayList<String> equipItems;
    public ArrayList<String> inventoryItems;

    public Character() {
        equipItems = new ArrayList<>();
        inventoryItems = new ArrayList<>();
    }

    public Character(int id, int accountID) {
        equipItems = new ArrayList<>();
        inventoryItems = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public void setEquipItems(ArrayList<String> equipItems) {
        this.equipItems = equipItems;
    }

    public void setInventoryItems(ArrayList<String> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public ArrayList<String> getEquipItems() {
        return equipItems;
    }

    public ArrayList<String> getInventoryItems() {
        return inventoryItems;
    }

    public int getId() {
        return id;
    }

    public int getAccountID() {
        return accountID;
    }
}
