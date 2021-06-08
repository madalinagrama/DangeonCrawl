package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Hammer extends Item {
    public Hammer(Cell cell) {
        super(cell, "hammer");
    }

    @Override
    public String getTileName() {
        return "hammer";
    }
}
