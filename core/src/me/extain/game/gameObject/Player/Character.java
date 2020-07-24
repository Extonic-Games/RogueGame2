package me.extain.game.gameObject.Player;

import me.extain.game.gameObject.item.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class Character {

    public int accountID;
    public int id;

    public int charLevel;
    public float charExp;

    public HashMap<Integer, Item> equipItems;
    public HashMap<Integer, Item> inventoryItems;

    public PlayerStats playerStats;

    public Character() {
        equipItems = new HashMap<>();
        inventoryItems = new HashMap<>();
        playerStats = new PlayerStats();
    }

    public Character(int id, int accountID) {
        equipItems = new HashMap<>();
        inventoryItems = new HashMap<>();
        playerStats = new PlayerStats();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public void setEquipItems(HashMap<Integer, Item> equipItems) {
        this.equipItems = equipItems;
    }

    public void setInventoryItems(HashMap<Integer, Item> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public void setCharExp(float charExp) {
        this.charExp = charExp;
    }

    public void setCharLevel(int charLevel) {
        this.charLevel = charLevel;
    }

    public float getCharExp() {
        return charExp;
    }

    public int getCharLevel() {
        return charLevel;
    }

    public HashMap<Integer, Item> getEquipItems() {
        return equipItems;
    }

    public HashMap<Integer, Item> getInventoryItems() {
        return inventoryItems;
    }

    public void addEquipItem(int slot, Item itemName) {
        equipItems.put(slot, itemName);
    }

    public void addInventoryItem(int slot, Item itemName) {
        inventoryItems.put(slot, itemName);
    }

    public void removeEquipItem(int slot) {
        equipItems.remove(slot);
    }

    public void removeInventoryItem(int slot) {
        inventoryItems.remove(slot);
    }

    public int getId() {
        return id;
    }

    public int getAccountID() {
        return accountID;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }
}

