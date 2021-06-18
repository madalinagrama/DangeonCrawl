package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public abstract class ItemWithEffect extends Item {

    public ItemWithEffect(Cell cell, String name) {
        super(cell, name);
    }

    public abstract void applyEffect(Player player);
}
