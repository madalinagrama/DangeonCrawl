package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.Tiles;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    Map<String, Integer> inventory;

    public Inventory() {
        this.inventory = new HashMap<>();
    }


    public void addItem(Map<String, Integer> inventory, Item item, String itemName, int amount) {
        int count = 0;
        if (inventory.containsKey(itemName)) {
            count = inventory.get(itemName) + 1;
        }
        if (!inventory.containsKey(item.getTileName())) {
            inventory.put(itemName, amount);
        } else {
            inventory.put(itemName, count);
        }
    }
    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }
}
