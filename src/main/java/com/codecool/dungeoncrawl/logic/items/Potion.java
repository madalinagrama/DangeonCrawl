package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Potion extends ItemWithEffect {
    public Potion(Cell cell) {
        super(cell, "potion");
    }

    @Override
    public String getTileName() {
        return "potion";
    }

    @Override
    public void applyEffect(Player player) {
        player.setHealthUp(5);
    }
}
