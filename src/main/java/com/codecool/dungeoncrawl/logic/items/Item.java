package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Item implements Drawable {

    private Cell cell;
    private String name;

    public Item(Cell cell, String name) {
        this.cell = cell;
        this.name = name;
        this.cell.setItem(this);
    }

}
